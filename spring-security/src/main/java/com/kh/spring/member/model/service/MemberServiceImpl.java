package com.kh.spring.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;
import com.kh.spring.member.model.vo.MemberEntity;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Override
	public int insertMember(MemberEntity member) {
		return 0;
	}

	@Override
	public int updateMember(Member updateMember) {
		return 0;
	}
    
}
