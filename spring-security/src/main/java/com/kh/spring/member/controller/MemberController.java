package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	/**
	 * form-login.login-path와 대응
	 */
	@GetMapping("/memberLogin.do")
	public void memberLogin() {}
	
	/**
	 * 1. Authentication DI받기
	 */
//	@GetMapping("/memberDetail.do")
	public void memberDetail(Authentication authentication) {
		log.debug("authentication = {}", authentication);
	}
	
	/**
	 * 2. SecurityContextHolder로부터 가져오기
	 */
//	@GetMapping("/memberDetail.do")
	public void memberDetail() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		
		Member principal = (Member) authentication.getPrincipal();
		Object credentical = authentication.getCredentials();
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)authentication.getAuthorities();
		
		log.debug("principal = {}", principal);
		log.debug("credential = {}", credentical); // null
		log.debug("authorities = {}", authorities);
	}

	/**
	 * @AuthenticationPrincipal
	 *  - org.springframework.security.core.annotation.AuthenticationPrincipal;
	 * 	
	 */
	@GetMapping("/memberDetail.do")
	public void memberDetial(@AuthenticationPrincipal Member member) {
		log.debug("member = {}", member);
	}
	
	@PostMapping("/memberUpdate.do")
	public ResponseEntity<?> memberUpdate(
			Member updateMember, 
			@AuthenticationPrincipal Member oldMember){
		log.debug("updateMember = {}", updateMember);
		log.debug("oldMember = {}", oldMember);
		
		// 1. DB record 갱신
		int result = memberService.updateMember(updateMember);
		
		// 2. security context의 authentication 갱신
		oldMember.setName(updateMember.getName());
		oldMember.setBirthday(updateMember.getBirthday());
		oldMember.setEmail(updateMember.getEmail());
		oldMember.setPhone(updateMember.getPhone());
		oldMember.setAddress(updateMember.getAddress());
		oldMember.setGender(updateMember.getGender());
		oldMember.setHobby(updateMember.getHobby());
		oldMember.setAuthorities((List<SimpleGrantedAuthority>)updateMember.getAuthorities());
		
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(
						oldMember, oldMember.getPassword(), oldMember.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		
		return ResponseEntity.ok("success!");
	}
	
	
	// birthday 변환 문자열 -> Date객체
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// boolean allowEmpty - true 빈문자열 ""인 경우 null 변환함. 빈문자열인 경우 오류남(false)
		PropertyEditor editor = new CustomDateEditor(sdf, true);
		// java.util.Date 변환시 editor객체 사용
		binder.registerCustomEditor(Date.class, editor);
	}
	
}





