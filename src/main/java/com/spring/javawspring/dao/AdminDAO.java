package com.spring.javawspring.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.MemberVO;

public interface AdminDAO {

	public int setMemberLevelCheck(@Param("idx") int idx, @Param("level") int level);

	public void adminMemberDel(@Param("idx") int idx);

	public ArrayList<MemberVO> adminMemberSearch(@Param("mid") String mid);

}
