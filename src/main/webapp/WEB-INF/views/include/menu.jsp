<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<div class="navbar navbar-static-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="/bbs1">BBS1</a>
            
                <ul class="nav">
                    <li><a href="/bbs1/bbs/list.do?bd=1"> 공지사항 </a>
                    <li><a href="/bbs1/bbs/list.do?bd=2"> 자유게시판 </a>
                    <li><a href="/bbs1/user/list.do">사용자 관리</a></li>
                	<li class="dropdown">
                		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
                			테스트<b class="caret"></b></a>
                		<ul class="dropdown-menu">
                			<li><a href="/bbs1/mybatis/cacheTest.do">myBatis Cache Test</a>
                			<li><a href="/bbs1/mybatis/departmentUserList1.do">departmentUserList1</a></li>
                			<li><a href="/bbs1/mybatis/departmentUserList2.do">departmentUserList2</a></li>
                			<li><a href="/bbs1/mybatis/dynamicSQL.do">dynamicSQL</a></li>
                		</ul>
                	</li>
                </ul>
                <ul class="nav pull-right">
                    <sec:authorize access="not authenticated">
                        <li><a href="/bbs1/home/login.do">로그인</a></li>
                    </sec:authorize>
                    <sec:authorize access="authenticated">
                        <li><a href="/bbs1/user/myinfo.do">
                            <sec:authentication property="user.name" /> 정보수정</a></li>
                        <li><a href="/bbs1/home/logout.do">로그아웃</a></li>
                    </sec:authorize>
                </ul>
            
        </div>
    </div>
</div>
