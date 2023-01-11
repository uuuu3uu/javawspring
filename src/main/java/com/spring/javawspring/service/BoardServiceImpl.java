package com.spring.javawspring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardDAO boardDAO;

	@Override
	public List<BoardVO> getBoardList(int startIndexNo, int pageSize) {
		return boardDAO.getBoardList(startIndexNo, pageSize);
	}

	@Override
	public int setBoardInput(BoardVO vo) {
		return boardDAO.setBoardInput(vo);
	}

	@Override
	public BoardVO getBoardContent(int idx) {
		return boardDAO.getBoardContent(idx);
	}

	@Override
	public void setBoardReadNum(int idx) {
		boardDAO.setBoardReadNum(idx);
	}

//	@Override
//	public void getBoardGood(int idx) {
//		boardDAO.getBoardGood(idx);
//	}

	@Override
	public void getBoardGoodUpdate(int idx, int gFlag) {
		boardDAO.getBoardGoodUpdate(idx, gFlag);
	}

	@Override
	public GoodVO getBoardGoodCheck(int partIdx, String part, String mid) {
		return boardDAO.getBoardGoodCheck(partIdx, part, mid);
	}

	@Override
	public ArrayList<BoardVO> getPrevNext(int idx) {
		return boardDAO.getPrevNext(idx);
	}

	@Override
	public void imgCheck(String content) {
		//			0					1					2					3					4					5					6
		// 			0123456789012345678901234567890123456789012345678901234567890
		// <img src="/javawspring/data/ckeditor/230111121348_pop.jpg" style="height:436px; width:564px" /><img src="/javawspring/data/ckeditor/230111121348_sub5.jpg" style="height:300px; width:700px" />
		
		// content안에 그림파일이 존재할때만 작업을 수행할 수 있도록 한다(src="/__~~)
		if(content.indexOf("src=\"/") == -1) return;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/");
		
		int position = 32;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw =true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));
			
			
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = uploadPath + "board/" + imgFile;
			
			fileCopyCheck(origFilePath, copyFilePath); // board폴더에 파일을 복사하고자 한다.
			
			if(nextImg.indexOf("src=\"/") == -1) {		// 나머지내용에서 찾았는데 이게 있느냐
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
		
		
	}
	
	
	private void fileCopyCheck(String origFilePath, String copyFilePath) {
		File origFile = new File(origFilePath);
		File copyFile = new File(copyFilePath);
		
		try {
			FileInputStream fis = new FileInputStream(origFile);
			FileOutputStream fos = new FileOutputStream(copyFile);
			
			byte[] buffer = new byte[2048];	//2KByte만 보내겠다
			int cnt = 0;
			while((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}
			fos.flush();
			fos.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setBoardDeleteOk(int idx) {
		boardDAO.setBoardDeleteOk(idx);
		
	}

	@Override
	public void imgDelete(String content) {
		//			0					1					2					3				! 4					5					6
		// 			0123456789012345678901234567890123456789012345678901234567890
		// <img src="/javawspring/data/ckeditor/board/230111121348_pop.jpg" style="height:436px; width:564px" /><img src="/javawspring/data/ckeditor/230111121348_sub5.jpg" style="height:300px; width:700px" />
		
		// content안에 그림파일이 존재할때만 작업을 수행할 수 있도록 한다(src="/__~~)
		if(content.indexOf("src=\"/") == -1) return;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/");
		
		int position = 38;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw =true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\"")); // 그림파일명 꺼내오기
			
			
			String origFilePath = uploadPath + imgFile;
			
			fileDelete(origFilePath); // board폴더에 파일을 삭제하고자 한다.
			
			if(nextImg.indexOf("src=\"/") == -1) {		// 나머지내용에서 찾았는데 이게 있느냐
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
	}
	
	
	private void fileDelete(String origFilePath) {
		File delFile = new File(origFilePath);
		if(delFile.exists()) delFile.delete();
	}

	@Override
	public void imgCheckUpdate(String content) {
		//			0					1					2					3					4					5					6
		// 			0123456789012345678901234567890123456789012345678901234567890
		// <img src="/javawspring/data/ckeditor/board/230111121348_pop.jpg" style="height:436px; width:564px" /><img src="/javawspring/data/ckeditor/230111121348_sub5.jpg" style="height:300px; width:700px" />
		// <img src="/javawspring/data/ckeditor/230111121348_pop.jpg" style="height:436px; width:564px" /><img src="/javawspring/data/ckeditor/230111121348_sub5.jpg" style="height:300px; width:700px" />
		
		// content안에 그림파일이 존재할때만 작업을 수행할 수 있도록 한다(src="/__~~)
		if(content.indexOf("src=\"/") == -1) return;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/");
		
		int position = 38;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw =true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));
			
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/" + imgFile);
			
			fileCopyCheck(origFilePath, copyFilePath);  // board폴더에 파일을 복사하고자 한다.
			
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
		
	}

	@Override
	public void setBoardUpdateOk(BoardVO vo) {
		boardDAO.setBoardUpdateOk(vo);
	}
}
