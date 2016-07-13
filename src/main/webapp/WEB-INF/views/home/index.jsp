<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<h2>Home</h2>
<hr />

<h3>현재 사용자</h3>

<sec:authorize access="authenticated">
    <table class="table table-bordered" style="width:500px;">
        <tr>
            <td>로그인ID</td>
            <td><sec:authentication property="user.loginId" /></td>
        </tr>
        <tr>
            <td>이름</td>
            <td><sec:authentication property="user.name" /></td>
        </tr>
        <tr>
            <td>이메일</td>
            <td><sec:authentication property="user.email" /></td>
        </tr>
    </table>
</sec:authorize>

<sec:authorize access="not authenticated">
    <p style="font-size:14pt;">익명의 사용자</p>
</sec:authorize>

