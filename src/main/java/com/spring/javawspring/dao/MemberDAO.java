package com.spring.javawspring.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.MemberVO;

public interface MemberDAO {

	public MemberVO getMemberIdCheck(@Param("mid") String mid);

	public MemberVO getMemberNickNameCheck(@Param("nickName") String nickName);

	public int setMemberJoinOk(@Param("vo") MemberVO vo);

	
	public void setMemTotalUpdate(@Param("mid") String mid, @Param("nowTodayPoint") int nowTodayPoint, @Param("todayCnt") int todayCnt);

	public int totRecCnt(String searchString);

	public ArrayList<MemberVO> getMemberList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize, @Param("mid") String mid);

	public ArrayList<MemberVO> memberSearchPost(@Param("mSearch") String mSearch, @Param("memberSearchC") String memberSearchC);

	public MemberVO memberInforGet(@Param("mid") String mid);

	public void memberDeleteOkGet(@Param("mid") String mid);

	public int totTermRecCnt(@Param("mid") String mid);

	public ArrayList<MemberVO> getTermMemberList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize, @Param("mid") String mid);

	public void setMemberPwdUpdate(@Param("mid") String mid, @Param("pwd") String pwd);

	public void memberDeleteOk(@Param("mid") String mid);

	public MemberVO memberUpdate(@Param("mid") String mid);

	public MemberVO setMemberUpdate(@Param("vo") MemberVO vo);

	public MemberVO getMemberNickNameEmailCheck(@Param("nickName") String nickName, @Param("email") String email);

	public void setKakaoMemberInputOk(@Param("mid") String mid, @Param("pwd") String pwd, @Param("nickName") String nickName, @Param("email") String email);

	public void setMemberUserDelCheck(@Param("mid") String mid);


	





	
		
	

}
