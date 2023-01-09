<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <title>memberList.jsp</title>
  <meta name="viewport" content="width=device-width, initial-scale=1"> 
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	'use strict';
  	
  	function midSearch() {
			let mSearch = myform.mSearch.value;
			let memberSearchC = myform.memberSearchC.value;
			if(mSearch.trim() == '') {
				alert("아이디를 입력하세요");
				myform.mSearch.focus();
			}
			
			else {
				myform.submit();
			}
	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
<div class="container">
	<h2>전체 회원 리스트</h2>
	<br/>
	<form name="myform">
		<div class="row mb-2">
			<div class="col form-inline">
				<select name="memberSearchC" id="memberSearchC" class="form-control mr-2">
					<option value="mid">아이디검색</option>
					<option value="nickName">닉네임검색</option>
					<option value="name">성명검색</option>
				</select>
				<input type="text" name="mSearch" class="form-control" autofocus />
				<input type="button" value="아이디 검색" onclick="midSearch();" class="btn btn-secondary ml-2"/>
			</div>
			<div class="col text-right"><button type="button" onclick="location.href='${ctp}/member/memberList';'" class="btn btn-secondary">전체검색</button></div>
		</div>
	</form>
	<table class="table table-hover text-center">
		<tr class="table-dark text-dark">
			<th>번호</th>
			<th>아이디</th>
			<th>닉네임</th>
			<th>성명</th>
			<th>성별</th>
		</tr>
	 	<c:forEach var="vo" items="${vos}" varStatus="st">
	 		<tr>
	 			<td>${vo.idx}</td>			<!-- 겟방식으로 ${vo.mid}의 값을 mid라는 이름의 변수로 memInfor.mem 로 보내준다   -->
	 			<td><a href="${ctp}/member/memberInfor?mid=${vo.mid}&pag=${pag}">${vo.mid}</a></td>
	 			<td>${vo.nickName}</td>
	 			<td>${vo.name}<c:if test="${sLevel == 0 && vo.userInfor == '비공개'}"><font color="red">(비공개)</font></c:if></td> 
	 			<td>${vo.gender}</td>
	 		</tr>
		</c:forEach>
		<tr><td colspan="5" class="m-0 p-0"></td></tr>
	</table>
</div>
<!-- 과제 페이징 처리  -->
<div class="text-center">
	<ul class="pagination justify-content-center">
		<c:if test="${pag > 1}">
			<li class="page-item"><a class="page-link text-secondary" href="${ctp}/member/memberList?pag=1"> 첫페이지</a></li>
		</c:if>
		<c:if test="${curBlock > 0}">
			<li class="page-item"><a class="page-link text-secondary" href="${ctp}/member/memberList?pag=${(curBlock-1)*blockSize + 1}">이전블록</a></li>
		</c:if>
		<c:forEach var="i" begin="${(curBlock)*blockSize + 1}" end="${(curBlock)*blockSize + blockSize}" varStatus="st">
			<c:if test="${i <= totPage && i == pag}">
				<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/member/memberList?pag=${i}">${i}</a></li>
			</c:if>
			<c:if test="${i <= totPage && i != pag}">
				<li class="page-item"><a class="page-link text-secondary" href="${ctp}/member/memberList?pag=${i}">${i}</a></li>
			</c:if>
		</c:forEach>
		<c:if test="${curBlock < lastBlock}">
			<li class="page-item"><a class="page-link text-secondary" href="${ctp}/member/memberList?pag=${(curBlock+1)*blockSize + 1}">다음블록</a></li>
		</c:if>
		<c:if test="${pag < totPage}">
			<li class="page-item"><a class="page-link text-secondary" href="${ctp}/member/memberList?pag=${totPage}"> 마지막페이지</a></li>
		</c:if>	
	</ul>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>