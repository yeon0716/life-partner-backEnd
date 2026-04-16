package com.life.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.life.member.dto.LoginDTO;
import com.life.member.dto.LoginResponseDTO;
import com.life.member.dto.SignupDTO;
import com.life.member.service.MemberService;
import com.life.member.vo.MemberVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/send-email")
    public String send(@RequestParam String email) {
        memberService.sendAuthCode(email);
        return "메일 발송";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String email,
                         @RequestParam String code) {

        return memberService.verifyCode(email, code) ? "성공" : "실패";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDTO dto) {
        try {
            memberService.signup(dto);
            return ResponseEntity.ok("SUCCESS");
        } catch (RuntimeException e) {
        	// 이메일 중복 에러 
            if ("EMAIL_DUPLICATE".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body("EMAIL_DUPLICATE");
            }
            // 이메일 인증 에러
            if ("EMAIL_NOT_VERIFIED".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body("EMAIL_NOT_VERIFIED");
            }
            return ResponseEntity.badRequest().body("FAIL");
        }
    }

    
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO dto) {
        return memberService.login(dto);
    }

    @GetMapping("/me")
    public MemberVO me(@RequestParam Long memberId) {
        return memberService.getMyInfo(memberId);
    }

    @PutMapping("/update")
    public String update(@RequestBody MemberVO vo) {
        memberService.updateMember(vo);
        return "수정 완료";
    }

    @PutMapping("/password")
    public String changePassword(@RequestParam Long memberId,
                                 @RequestParam String newPassword) {
        memberService.changePassword(memberId, newPassword);
        return "비밀번호 변경 완료";
    }

    @GetMapping("/find-email")
    public String findEmail(@RequestParam String name,
                            @RequestParam String phone) {
        return memberService.findEmail(name, phone);
    }

    @PostMapping("/temp-password")
    public String tempPassword(@RequestParam String email) {
        return memberService.issueTempPassword(email);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam Long memberId) {
        memberService.deleteMember(memberId);
        return "탈퇴 완료";
    }

    // 관리자
    @GetMapping("/admin/list")
    public List<MemberVO> list() {
        return memberService.getAllMembers();
    }

    @PutMapping("/admin/role")
    public String role(@RequestParam Long memberId,
                       @RequestParam Integer role) {
        memberService.updateRole(memberId, role);
        return "등급 변경 완료";
    }

    @PutMapping("/admin/status")
    public String status(@RequestParam Long memberId,
                         @RequestParam String status) {
        memberService.updateStatus(memberId, status);
        return "상태 변경 완료";
    }
}