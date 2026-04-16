package com.life.member.service;

import java.util.List;

import com.life.member.dto.LoginDTO;
import com.life.member.dto.LoginResponseDTO;
import com.life.member.dto.SignupDTO;
import com.life.member.vo.MemberVO;

public interface MemberService {

    // 이메일 인증
	void sendAuthCode(String email);

    boolean verifyCode(String email, String code);

    // 회원 기능
    int signup(SignupDTO dto);
    
    MemberVO selectByEmail(String email);

    LoginResponseDTO login(LoginDTO dto);

    MemberVO getMyInfo(Long memberId);

    int updateMember(MemberVO vo);

    int changePassword(Long memberId, String newPassword);

    String findEmail(String name, String phone);

    String issueTempPassword(String email);

    int deleteMember(Long memberId);

    // 관리자
    List<MemberVO> getAllMembers();

    int updateRole(Long memberId, Integer role);

    int updateStatus(Long memberId, String status);
}