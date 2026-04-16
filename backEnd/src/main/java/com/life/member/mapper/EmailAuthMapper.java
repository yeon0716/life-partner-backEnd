package com.life.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.life.member.vo.EmailAuthVO;

@Mapper
public interface EmailAuthMapper {

    int insertAuth(EmailAuthVO vo);

    EmailAuthVO checkAuth(EmailAuthVO vo);

    int updateVerified(String email);

    int isVerified(String email);

    int deleteByEmail(String email);
}