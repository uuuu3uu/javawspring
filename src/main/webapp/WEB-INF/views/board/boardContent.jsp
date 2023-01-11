<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <title>boContent.jsp</title>
  <meta name="viewport" content="width=device-width, initial-scale=1"> 
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
  	'use strict';
  	function goodCheck() {
  		let gFlag = 1;
  		if('${sGFlag}' != '1') gFlag = -1; //참 // (sGFlag가 1 != 1) 1과 1이 같은데 !=(아니다)라고 말하고 있으니 그냥 let gFlag = 1; 인 상태로 boardGood 컨트롤러로 갑니다
			$.ajax({
				type : "post",
				url	 : "${ctp}/board/boardGood",
				data : {idx : ${vo.idx},
								gFlag : gFlag},  //문자열일때는 data : {idx : '${vo.idx}'}
				success:function() {
	    			location.reload();
	    		},
	    		error : function() {
	    			alert("전송 오류~~");
	    		}
	    	});
	    }
  	
    
  	// 게시글 삭제처리
  	function boardDeleteCheck() {
    	let ans = confirm("현 게시글을 삭제하시겠습니까?");// ${ctp}/board/ 빼도 상관은 없음
    	if(ans) location.href = "${ctp}/board/boardDeleteOk?idx=${vo.idx}&pag=${pag}&pageSize=${pageSize}&mid=${vo.mid}";
    }
  	
		// 댓글달기
		function replyCheck() {
    	let content = $("#content").val();
    	if(content.trim() == "") {
    		alert("댓글을 입력하세요");
    		$("#content").focus();
    		return false;
    	}
    	let query = {
    			boardIdx  : ${vo.idx},
    			mid				: '${sMid}',
    			nickName  : '${sNickName}',
    			content   : content,
    			hostIp    : '${pageContext.request.remoteAddr}'
    	}
    	
    	$.ajax({
    		type : "post",
    		url  : "${ctp}/boReplyInput.bo",
    		data : query,
    		success:function(res) {
    			if(res == "1") {
    				alert("댓글이 입력되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("댓글 입력 실패~~~");
    			}
    		},
  			error  : function() {
  				alert("전송 오류!!");
  			}
    	});
    }
		
		// 댓글 삭제
		function replyDelCheck(idx) {
			let ans = confirm("현재 댓글을 삭제하시겠습니까?");
			if(!ans) return false;
		
			$.ajax({
				type  : "post",
				url		: "${ctp}/boReplyDeleteOk.bo",
				data  : {idx : idx},
				success : function(res) {
					if(res == "1") {
						alert("댓글이 삭제되었습니다");
						location.reload();
					}
					else {
						alert("댓글 삭제 실패");
					}
				},
				error : function() {
					alert("전송오류")
				}
			});
		}

  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<h2 class=text-center>글 내 용 보 기</h2>
	<br/>
	<table class="table table-borderless">
		<tr>
			<td class="text-right">hostIp : ${vo.hostIp}</td>
		</tr>
	</table>
	<table class="table table-bordered">
		<tr>
			<th>글쓴이</th>
			<td>${vo.nickName}</td>
			<th>글쓴날짜</th>
			<td>${fn:substring(vo.WDate,0,fn:length(vo.WDate)-2)}</td>
		</tr>
		<tr>
			<th>글제목</th>
			<td colspan="3">${vo.title}</td>
		</tr>
		<tr>
			<th>전자메일</th>
			<td>${vo.email}</td>
			<th>조회수</th>
			<td>${vo.readNum}</td>
		</tr>
		<tr>		
			<th>홈페이지</th>
			<td>${vo.homePage}</td>
			<th>좋아요</th>
			<td><a href="javascript:goodCheck()">
            <c:if test="${sGFlag == 1}"><font color="red">❤</font></c:if>
            <c:if test="${sGFlag != 1}">❤</c:if>
            ${vo.good}
          </a>
          <a href="javascript:goodDBCheck()">
            <c:if test="${goodVo.goodSw == 'Y'}"><font color="red">❤</font></c:if>
            <c:if test="${goodVo.goodSw != 'Y'}">❤</c:if>
            ${vo.good}
          </a>
          <!-- ${vo.good} ,
          <a href="javascript:goodCheckPlus()">👍</a>
          <a href="javascript:goodCheckMinus()">👎</a> -->
      </td>
		</tr>
		<tr>
			<th>글내용</th>
			<td colspan="3" style="height:220px">${fn:replace(vo.content, newLine, "<br/>")}</td>
		</tr>
		<tr>
			<td colspan="4" class="text-center">	<!-- 물음표 뒤에 변수를 보내준다. // ${pag}값을 pag라는 이름의 변수를 boList.bo로 보내주고 &  -->	
				<c:if test="${flag == 'search'}"><input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardSearch?search=${search}&searchString=${searchString}&pageSize=${pageSize}&pag=${pag}';" class="btn btn-secondary"/></c:if>
        <c:if test="${flag != 'search'}">
					<input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardList?pag=${pag}&pageSize=${pageSize}';" class="btn btn-secondary"/>
				<!-- 나와 관리자만.. -->
					<c:if test="${sMid == vo.mid || sLevel == 0}"> <!-- 올린 글이 나하고 같거나 관리자레벨 -->
						<input type="button" value="수정하기" onclick="location.href='${ctp}/board/boardUpdate?idx=${vo.idx}&pag=${pag}&pageSize=${pageSize}';" class="btn btn-info"/>
						<input type="button" value="삭제하기" onclick="boardDeleteCheck()" class="btn btn-danger"/>
					</c:if>
				</c:if>
			</td>
		</tr>		
	</table>
	
	<c:if test="${flag != 'search'}">
		<!-- 이전글/다음글 처리 -->
		<table class="table table-borderless">
	    <tr>
	      <td>
	        <%-- 
	        <c:if test="${preVo.preIdx != 0}">
	          👈 <a href="${ctp}/board/boardContent?idx=${preVo.preIdx}&pageSize=${pageSize}&pag=${pag}">이전글 : ${preVo.preTitle}</a><br/>
	        </c:if>
	        <c:if test="${nextVo.nextIdx != 0}">
	          👉 <a href="${ctp}/board/boardContent?idx=${nextVo.nextIdx}&pageSize=${pageSize}&pag=${pag}">다음글 : ${nextVo.nextTitle}</a>
	        </c:if>
	        --%>
	        <c:if test="${!empty pnVos[1]}">
	          다음글 : <a href="${ctp}/board/boardContent?idx=${pnVos[1].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[1].title}</a><br/>
	        </c:if>
	        <c:if test="${vo.idx < pnVos[0].idx}">
	          다음글 : <a href="${ctp}/board/boardContent?idx=${pnVos[0].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[0].title}</a><br/>
	        </c:if>
	        <c:if test="${vo.idx > pnVos[0].idx}">
	          이전글 : <a href="${ctp}/board/boardContent?idx=${pnVos[0].idx}&pageSize=${pageSize}&pag=${pag}">${pnVos[0].title}</a><br/>
	        </c:if>
	      </td>
	    </tr>
	  </table>
	</c:if>
</div>
<br/>
<!-- 댓글리스트 보여주기 -->
<div class="container">
	<table class="table table-hover text-center">
		<tr>
			<th>작성자</th>
			<th>댓글내용</th>
			<th class="text-">작성일자</th>
			<th>접속IP</th>
		</tr>
		<c:forEach var="replyVo" items="${replyVos}">
			<tr>
				<td>${replyVo.nickName}
					<c:if test="${sMid == replyVo.mid || sLevel == 0}">
						(<a href="javascript:replyDelCheck(${replyVo.idx})" title="삭제하기">x</a>)
					</c:if>
				</td>
				<td>
					<b>${fn:replace(replyVo.content, newLine, "<br/>")}</b>
				</td>
				<td>${replyVo.wDate}</td>
				<td>${replyVo.hostIp}</td>
			</tr>
		</c:forEach>
	</table>
	<!-- 댓글 입력창 -->
	<form name="replyForm" method="post" action="${ctp}/boardRlyInput}">
		<table class="table text-center">
			<tr>
				<td style="width:85%" class="text-left">
					글내용 : 
					<textarea rows="4" name="content" id="content" class="form-control"></textarea>				
				</td>
				<td style="width:15%">
					<br/>
					<p>작성 : ${sNickName}</p>
	        <p>
	        	<input type="button" value="댓글달기" onclick="replyCheck()" class="btn btn-info btn-sm"/>
	        </p>
				</td>
			</tr>
		</table>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>