package com.life.member.service;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.life.member.dto.LoginDTO;
import com.life.member.dto.LoginResponseDTO;
import com.life.member.dto.SignupDTO;
import com.life.member.mapper.EmailAuthMapper;
import com.life.member.mapper.MemberMapper;
import com.life.member.vo.EmailAuthVO;
import com.life.member.vo.MemberVO;
import com.life.util.JwtUtil;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    // DB 접근 객체
    private final MemberMapper memberMapper;
    private final EmailAuthMapper emailAuthMapper;

    // 이메일 발송
    private final JavaMailSender mailSender;

    // 비밀번호 암호화
    private final BCryptPasswordEncoder encoder;

    // JWT 토큰 생성
    private final JwtUtil jwtUtil;


    // 6자리 인증코드 생성
    private String createCode() {
        return String.valueOf((int)(Math.random()*900000)+100000);
    }


    // 이메일 인증코드 발송
    @Override
    public void sendAuthCode(String email) {

        // 기존 인증코드 삭제 (재사용 방지)
        emailAuthMapper.deleteByEmail(email);

        String code = createCode();

        // DB에 인증코드 저장
        EmailAuthVO vo = new EmailAuthVO();
        vo.setEmail(email);
        vo.setAuthCode(code);

        int result = emailAuthMapper.insertAuth(vo);

        if(result == 0) {
            throw new RuntimeException("인증코드 저장 실패");
        }

        try {
            // 이메일 전송
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(email);
            helper.setSubject("인증코드");
            helper.setText("인증코드: " + code, true);

            mailSender.send(msg);

        } catch (Exception e) {
            throw new RuntimeException("메일 발송 실패");
        }
    }


    // 인증코드 검증
    @Override
    public boolean verifyCode(String email, String code) {

        // 입력한 이메일과 코드로 DB 조회
        EmailAuthVO vo = new EmailAuthVO();
        vo.setEmail(email);
        vo.setAuthCode(code);

        EmailAuthVO result = emailAuthMapper.checkAuth(vo);

        if(result != null) {

            // 인증 성공 시 상태 변경 후 삭제 (재사용 방지)
            int update = emailAuthMapper.updateVerified(email);
            int delete = emailAuthMapper.deleteByEmail(email);

            if(update == 0 || delete == 0) {
                throw new RuntimeException("인증 처리 실패");
            }

            return true;
        }

        return false;
    }


    // 회원가입
    @Override
    public int signup(SignupDTO dto) {

        // 이메일 인증 여부 확인
        if(emailAuthMapper.isVerified(dto.getEmail()) == 0) {
            throw new RuntimeException("이메일 인증 필요");
        }

        // 이메일 중복 체크
        if(memberMapper.selectByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("이미 존재하는 이메일");
        }

        // 비밀번호 암호화
        dto.setPassword(encoder.encode(dto.getPassword()));

        int result = memberMapper.insertMember(dto);

        if(result == 0) {
            throw new RuntimeException("회원가입 실패");
        }

        // 인증 데이터 삭제
        emailAuthMapper.deleteByEmail(dto.getEmail());

        return result;
    }


    // 로그인 (JWT 발급)
    @Override
    public LoginResponseDTO login(LoginDTO dto) {

        // 이메일로 회원 조회
        MemberVO member = memberMapper.selectByEmail(dto.getEmail());

        if(member == null) {
            throw new RuntimeException("회원 없음");
        }

        // 비밀번호 비교 (암호화)
        if(!encoder.matches(dto.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호 오류");
        }

        // 계정 상태 확인
        if(!"ACTIVE".equals(member.getStatus())) {
            throw new RuntimeException("비활성 계정");
        }

        // JWT 생성
        String token = jwtUtil.createToken(member.getEmail());

        // 토큰 + 사용자 정보 반환
        return new LoginResponseDTO(token, member);
    }


    // 내 정보 조회
    @Override
    public MemberVO getMyInfo(Long memberId) {
        return memberMapper.selectById(memberId);
    }


    // 회원 정보 수정
    @Override
    public int updateMember(MemberVO vo) {

        int result = memberMapper.updateMember(vo);

        if(result == 0) {
            throw new RuntimeException("회원 수정 실패");
        }

        return result;
    }


    // 비밀번호 변경
    @Override
    public int changePassword(Long memberId, String newPassword) {

        MemberVO vo = new MemberVO();
        vo.setMemberId(memberId);

        // 암호화 후 저장
        vo.setPassword(encoder.encode(newPassword));

        int result = memberMapper.updatePassword(vo);

        if(result == 0) {
            throw new RuntimeException("비밀번호 변경 실패");
        }

        return result;
    }


    // 이메일(아이디) 찾기
    @Override
    public String findEmail(String name, String phone) {

        MemberVO vo = memberMapper.findEmail(name, phone);

        return vo != null ? vo.getEmail() : "없음";
    }


    // 임시 비밀번호 발급
    @Override
    public String issueTempPassword(String email) {

        MemberVO member = memberMapper.selectByEmail(email);

        if(member == null) {
            throw new RuntimeException("회원 없음");
        }

        String tempPw = createCode();

        MemberVO vo = new MemberVO();
        vo.setMemberId(member.getMemberId());

        // 반드시 암호화 필요
        vo.setPassword(encoder.encode(tempPw));

        memberMapper.updatePassword(vo);

        return tempPw;
    }


    // 회원 탈퇴 (soft delete)
    @Override
    public int deleteMember(Long memberId) {

        int result = memberMapper.deleteMember(memberId);

        if(result == 0) {
            throw new RuntimeException("회원 탈퇴 실패");
        }

        return result;
    }


    // 관리자 - 전체 회원 조회
    @Override
    public List<MemberVO> getAllMembers() {
        return memberMapper.selectAll();
    }


    // 관리자 - 권한 변경
    @Override
    public int updateRole(Long memberId, Integer role) {

        MemberVO vo = new MemberVO();
        vo.setMemberId(memberId);
        vo.setRole(role);

        int result = memberMapper.updateRole(vo);

        if(result == 0) {
            throw new RuntimeException("등급 변경 실패");
        }

        return result;
    }


    // 관리자 - 상태 변경
    @Override
    public int updateStatus(Long memberId, String status) {

        MemberVO vo = new MemberVO();
        vo.setMemberId(memberId);
        vo.setStatus(status);

        int result = memberMapper.updateStatus(vo);

        if(result == 0) {
            throw new RuntimeException("상태 변경 실패");
        }

        return result;
    }
}