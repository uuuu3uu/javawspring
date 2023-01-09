<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>aJaxTest3.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
    'use strict';
    
    function idCheck() {
    	let mid = myform.mid.value;
    	if(mid.trim()==""){
    		alert("아이디를 입력하세요!");
    		myform.mid.focus();
    		return false;
    	}
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/study/ajax/ajaxTest3_1",
    		data  : {mid : mid},
    		success:function(vo) {
    			let str = '<hr/><b>전송결과</b><br/>';
    			if(vo != '') {
    				str += '성명 : ' + vo.name + '<br/>';
    				str += '이메일 : ' + vo.email + '<br/>';
    				str += '홈페이지 : ' + vo.homePage + '<br/>';
    				str += '방문IP : ' + vo.hostIp + '<br/>';
    			}
    			else {
    				str += '<font color="red"><b>찾는 자료가 없습니다.</b></font>';
    			}
    			
    			$("#demo").html(str);
    		},
    		error : function() {
    			alert("전송오류!");
    		}
    	});
    }
    
    function nameCheck() {
    	let mid = myform.mid.value;
    	if(mid.trim() == "") {
    		alert("아이디를 입력하세요!");
    		myform.name.focus();
    		return false;
    	}
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/study/ajax/ajaxTest3_2",
    		data  : {mid : mid},
    		success:function(vos) {
    			let str = '<b>전송결과</b><hr/>';
    			if(vos != '') {
    				str += '<table class="table table-hover">';
    				str += '<tr class="table-dark text-dark">';
    				str += '<th>성명</th><th>이메일</th><th>홈페이지></th><th>방문IP</th>';
    				str += '</tr>';
    				for(let i=0; i<vos.length; i++) {
	    				str += '<tr class="text-center">';
	    				str += '<td>' + vos[i].name + '</td>';
	    				str += '<td>' + vos[i].email + '</td>';
	    				str += '<td>' + vos[i].homePage + '</td>';
	    				str += '<td>' + vos[i].hostIp + '</td>';
	    				str += '</tr>';
    				}
    				str += '</table>';
    			}
    			else {
    				str += '<font color="red"><b>찾는 자료가 없습니다.</b></font>';
    			}
    			
    			$("#demo").html(str);
    		},
    		error : function() {
    			alert("전송오류!");
    		}
    	});
    }
    
    function guestSearchCheck() {
    	let guestSearch = myform.guestSearch.value;
    	let guestSearchInput = myform.guestSearchInput.value;
    	if(guestSearchInput.trim() == "") {
    		alert("검색어를 입력하세요");
    		myform.guestSearchInput.focus();
    		return false;
    	}
    	
    	let query = {
    		guestSearch : guestSearch,
    		guestSearchInput : guestSearchInput
    	};
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/study/ajax/ajaxTest3_3",
    		data  : query,
    		success:function(vos) {
    			let str = '<b>전송결과</b><hr/>';
    			if(vos != '') {
    				str += '<table class="table table-hover">';
    				str += '<tr class="table-dark text-dark">';
    				str += '<th class="text-center">성명</th><th class="text-center">이메일</th><th class="text-center">홈페이지</th><th class="text-center">방문IP</th>';
    				str += '</tr>';
    				for(let i=0; i<vos.length; i++) {
	    				str += '<tr class="text-center">';
	    				str += '<td>' + vos[i].name + '</td>';
	    				str += '<td>' + vos[i].email + '</td>';
	    				str += '<td>' + vos[i].homePage + '</td>';
	    				str += '<td>' + vos[i].hostIp + '</td>';
	    				str += '</tr>';
    				}
    				str += '</table>';
    			}
    			else {
    				str += '<font color="red"><b>찾는 자료가 없습니다.</b></font>';
    			}
    			
    			$("#demo").html(str);
    		},
    		error : function() {
    			alert("전송오류!");
    		}
    	});
    }
    	
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
<div class="container">
	<h2>aJax를 활용한 '회원아이디' 검색하기</h2>
	<hr/>
	<form name="myform" method="post">
		<p>
		이름 :
			<input type="text" name="mid" autofocus />
			<input type="button" value="이름 검색" onclick="idCheck()" class="btn btn-secondary" /> &nbsp;
			<input type="button" value="성명 부분 검색" onclick="nameCheck()" class="btn btn-secondary" /> &nbsp;
			<input type="reset" value="다시입력" onclick="idCheck()" class="btn btn-secondary" /> &nbsp;
			<input type="button" value="돌아가기" onclick="Location.href='${ctp}/study/ajax/ajaxMenu';" class="btn btn-secondary" /> &nbsp;
 		</p>
	<hr/>
	<select name="guestSearch" id="guestSearch">
		<option value="name">성명검색</option>
		<option value="email">이메일검색</option>
		<option value="content">방명록검색</option>
	</select>
	<input type="text" name="guestSearchInput" />
	<input type="button" value="검색" onclick="guestSearchCheck()" class="btn btn-info" /> &nbsp;
	<p id="demo"></p>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>