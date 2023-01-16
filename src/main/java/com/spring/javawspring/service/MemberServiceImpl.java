package com.spring.javawspring.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.common.JavawspringProvide;
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
	public int setMemberJoinOk(MultipartFile fName, MemberVO vo) {
		// 업로드 된 사진을 서버 파일 시스템에 저장시켜준다.
		int res = 0;
		try {
			String oFileName = fName.getOriginalFilename();
			if(oFileName.equals("")) {
				vo.setPhoto("noimage.jpg");
			}
			else {
				UUID uid = UUID.randomUUID();	// 중복된 이름을 주지 않으려고
				String saveFileName = uid + "_" + oFileName;
				
				JavawspringProvide ps = new JavawspringProvide();
				ps.writeFile(fName, saveFileName, "member");
				vo.setPhoto(saveFileName);
			}
			memberDAO.setMemberJoinOk(vo);
			res = 1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;

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
		
		return memberDAO.totRecCnt("");
	}

	@Override
	public ArrayList<MemberVO> getMemberList(int startIndexNo, int pageSize, String mid) {
		
		return memberDAO.getMemberList(startIndexNo, pageSize, mid);
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
	

	
	
	@Override
	public int totTermRecCnt(String mid) {
		
		return memberDAO.totTermRecCnt(mid);
	}

	@Override
	public ArrayList<MemberVO> getTermMemberList(int startIndexNo, int pageSize, String mid) {
		
		return memberDAO.getTermMemberList(startIndexNo, pageSize, mid);
	}

	@Override
	public void setMemberPwdUpdate(String mid, String pwd) {
		memberDAO.setMemberPwdUpdate(mid, pwd);
		
	}

	// 회원 삭제하기
	@Override
	public void memberDeleteOk(String mid) {
		memberDAO.memberDeleteOk(mid);
		
	}
	
	//비밀번호 확인하기

	
	// 회원 수정하기
	@Override
	public MemberVO memberUpdate(String mid) {
		return memberDAO.memberUpdate(mid);
	}
//회원 수정하기 처리
	@Override
	public MemberVO setMemberUpdate(MemberVO vo) {
		
		return memberDAO.setMemberUpdate(vo);
		
	}

	@Override
	public MemberVO getMemberNickNameEmailCheck(String nickName, String email) {
		return memberDAO.getMemberNickNameEmailCheck(nickName, email);
	}

	@Override
	public void setKakaoMemberInputOk(String mid, String pwd, String nickName, String email) {
		memberDAO.setKakaoMemberInputOk(mid, pwd, nickName, email);
		
	}

	@Override
	public void setMemberUserDelCheck(String mid) {
		memberDAO.setMemberUserDelCheck(mid);
		
	}
	


	



	
	
	

	
}
