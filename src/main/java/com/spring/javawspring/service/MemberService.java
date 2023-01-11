package com.spring.javawspring.service;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.vo.MemberVO;

public interface MemberService {

	public MemberVO getMemberIdCheck(String mid);

	public MemberVO getMemberNickNameCheck(String nickName);

	public int setMemberJoinOk(MultipartFile fName, MemberVO vo);

	public void setMemberVisitProcess(MemberVO vo);

	public int totRecCnt();

	public int totTermRecCnt(String mid);
	
	public ArrayList<MemberVO> getMemberList(int startIndexNo, int pageSize, String mid);

	public ArrayList<MemberVO> getTermMemberList(int startIndexNo, int pageSize, String mid);
	
	public ArrayList<MemberVO> memberSearchPost(String mSearch, String memberSearchC);

	public MemberVO memberInforGet(String mid);

	public void setMemberPwdUpdate(String mid, String pwd);

	public void memberDeleteOk(String mid);

	public MemberVO memberUpdate(String mid);

	public MemberVO setMemberUpdate(MemberVO vo);

	// public ArrayList<MemberVO> adminMemberSearch(String mid);



	

	
	
	

	

}
