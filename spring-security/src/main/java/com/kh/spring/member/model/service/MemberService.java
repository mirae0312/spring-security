package com.kh.spring.member.model.service;

import com.kh.spring.member.model.vo.Member;
import com.kh.spring.member.model.vo.MemberEntity;

public interface MemberService {

	int insertMember(MemberEntity member);

	int updateMember(Member updateMember);

}
