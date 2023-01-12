package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

public interface BoardDAO {

	public List<BoardVO> getBoardList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize, @Param("part") String part, @Param("SearchString") String SearchString);

	public int totRecCnt(String part, String searchString);

	public int setBoardInput(@Param("vo") BoardVO vo);

	public BoardVO getBoardContent(@Param("idx") int idx);

	public void setBoardReadNum(@Param("idx") int idx);

//	public void getBoardGood(@Param("idx") int idx);

	public void getBoardGoodUpdate(@Param("idx") int idx, @Param("gFlag") int gFlag);

	public GoodVO getBoardGoodCheck(@Param("partIdx") int partIdx, @Param("part") String part, @Param("mid") String mid);

	public ArrayList<BoardVO> getPrevNext(@Param("idx") int idx);

	public void imgCheck(@Param("content") String content);

	public void setBoardDeleteOk(@Param("idx") int idx);

	public void setBoardUpdateOk(@Param("vo") BoardVO vo);

	public void setBoardReplyInput(@Param("replyVo") BoardReplyVO replyVo);

	public List<BoardReplyVO> getBoardReply(@Param("idx") int idx);

	public void setBoardReplyDeleteOk(@Param("idx") int idx);

	public String getMaxLevelOrder(@Param("boardIdx") int boardIdx);

	public void setLevelOrderPlusUpdate(@Param("replyVo") BoardReplyVO replyVo);

	public void setBoardReplyInput2(@Param("replyVo") BoardReplyVO replyVo);

	
	
}
