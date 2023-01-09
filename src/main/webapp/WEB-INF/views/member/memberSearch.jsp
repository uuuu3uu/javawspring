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
  
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
<div class="container">
	<h2>회원 검색 리스트</h2>
	<br/>
	<div class="text-right mb-2">
		<input type="button" value="돌아가기" onclick="location.href='${ctp}/member/memberList';" class="btn btn-secondary">
	</div>
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
	 			<td>${vo.idx}</td>
	 			<td><a href="${ctp}/member/memInfor?mid=${vo.mid}">${vo.mid}</a></td>
	 			<td>${vo.nickName}</td>
	 			<td>${vo.name}<c:if test="${sLevel == 0 && vo.userInfor == '비공개'}"><font color="red">(비공개)</font></c:if></td> 
	 			<td>${vo.gender}</td>
	 		</tr>
		</c:forEach>
		<tr><td colspan="5" class="m-0 p-0"></td></tr>
	</table>
</div>

<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>