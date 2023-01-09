package com.spring.javawspring.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.MemberDAO;
import com.spring.javawspring.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	MemberDAO memberDAO;
	
	@Override
	public MemberVO getMemberIdCheck(String mid) {
		
		return memberDAO.getMemberIdCheck(mid);
	}

	@Override
	public MemberVO getMemberNickNameCheck(String nickName) {
		return memberDAO.getMemberNickNameCheck(nickName);
	}


	@Override
	public int setMemberJoinOk(MemberVO vo) {
		
		return memberDAO.setMemberJoinOk(vo);
	}

	@Override
	public void setMemberVisitProcess(MemberVO vo) {
		// 오늘 날짜 편집
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strNow = sdf.format(now);
		
		// 오늘 처음 방문 시는 오늘 방문카운트(todayCont)를 0으로 셋팅한다.
		if(!vo.getLastDate().substring(0,10).equals(strNow)) {  // 마지막방문날짜와 오늘 날짜가 같지 않다 -> 오늘 처음 방문했따
			//memberDAO.setTodayContUpdate(vo.getMid());  
			vo.setTodayCnt(0);		// 오늘 첫 방문 0으로 세팅
		}
		
		int todayCnt = vo.getTodayCnt() + 1;
		
		int nowTodayPoint = 0; // (오늘 방문포인트)
		if(vo.getTodayCnt() >= 5) {
			nowTodayPoint = vo.getPoint();
		}
		else {
			nowTodayPoint = vo.getPoint() + 10;
		}
		
		// 오늘 재방문이라면 '총 방문수', '오늘 방문수' '포인트' 누적처리
		memberDAO.setMemTotalUpdate(vo.getMid(), nowTodayPoint, todayCnt);
		
	}

	@Override
	public int totRecCnt() {
		
		return memberDAO.totRecCnt();
	}

	@Override
	public ArrayList<MemberVO> getMemberList(int startIndexNo, int pageSize) {
		
		return memberDAO.getMemberList(startIndexNo, pageSize);
	}

	@Override
	public ArrayList<MemberVO> memberSearchPost(String mSearch, String memberSearchC) {

		return memberDAO.memberSearchPost(mSearch, memberSearchC);
	}

	// 회원 상세 보기
	@Override
	public MemberVO memberInforGet(String mid) {
		return memberDAO.memberInforGet(mid);
	}
	
	// 회원 삭제하기
	@Override
	public void memberDeleteOkGet(String mid) {
		memberDAO.memberDeleteOkGet(mid);
	}
	
	
	@Override
	public int totTermRecCnt(String mid) {
		
		return memberDAO.totTermRecCnt(mid);
	}

	@Override
	public ArrayList<MemberVO> getTermMemberList(int startIndexNo, int pageSize, String mid) {
		
		return memberDAO.getTermMemberList(startIndexNo, pageSize, mid);
	}
	



	
	
	

	
}
