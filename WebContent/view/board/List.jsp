<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<h1>리 스 트 으</h1>

<table border="" >
	<tr>
		<td width="50px">번호</td>
		<td width="300px">제목</td>
		<td width="200px">작성자</td>
		<td width="200px">작성일</td>
		<td width="50px">조회수</td>
	</tr>
<c:forEach var="dto" items="${data}" varStatus="no">	
	<tr>
		<td>${start+no.index+1 }</td> <!-- 여기가 페이지 글의 번호 나오는곳 -->
		<td>
			<c:if test="${dto.level>0 }">
				<c:forEach begin="1" end="${dto.level }" step="1">
				&nbsp;&nbsp;
				</c:forEach>
			└</c:if>
			<a href="Detail?bid=${dto.bid }&page=${nowpage}">${dto.title }</a> 
		</td>
		<td>${dto.pname }</td>
		<td>${dto.regdateStr }</td>
		<td>${dto.no }</td>
	</tr>
</c:forEach>
	<tr>
		<td colspan="5" align="center">
		<c:if test="${startpage>1 }">       <!-- 스타트 페이지가 1보다클때만 나와라 -->
			<a href="?page=${startpage-1 }">[이전]</a> 
		</c:if>
		<c:forEach begin="${startpage }" end="${endpage }" var="i">
				<c:choose>
				<c:when test="${i==nowpage }"> <!-- 지금 페이지가 내 페이지라면 -->
				[${i }]
				</c:when>
				<c:otherwise>
					<a href="?page=${i }">${i }</a> <!-- 자기 자신으로 돌아오는데 페이지가 i -->
				</c:otherwise>
				</c:choose>			
			</c:forEach>
			<c:if test="${endpage<totalpage }">  <!-- 마지막 페이지가 토탈페이지보다 작을때만 다음이 나오게-->
			<a href="?page=${endpage+1 }">[다음]</a> 
		</c:if>
		</td>
	</tr>
	
		<tr>
		<td colspan="5" align="right">
			<a href="WriteForm?page=${nowpage }">글쓰기</a>
		</td>
	</tr>
</table>
