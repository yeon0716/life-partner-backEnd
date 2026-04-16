package com.life.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.life.member.dto.LoginDTO;
import com.life.member.dto.SignupDTO;
import com.life.member.vo.MemberVO;

@Mapper
public interface MemberMapper {

    int insertMember(SignupDTO dto);

    MemberVO selectByEmail(String email);

    MemberVO selectById(Long memberId);

    MemberVO findEmail(@Param("name") String name,
                       @Param("phone") String phone);

    int updateMember(MemberVO vo);

    int updatePassword(MemberVO vo);

    int deleteMember(Long memberId);

    List<MemberVO> selectAll();

    int updateRole(MemberVO vo);

    int updateStatus(MemberVO vo);
}