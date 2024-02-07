package com.psj.itembrowser.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.psj.itembrowser.member.domain.dto.request.MemberSignUpRequestDTO;
import com.psj.itembrowser.member.domain.vo.Member;

@Mapper
public interface MemberMapper {

	Member findByEmail(@Param("email") String email);

	boolean insertMember(MemberSignUpRequestDTO requestDTO);

	Member findById(Long id);
}