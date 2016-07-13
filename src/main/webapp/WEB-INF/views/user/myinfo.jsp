<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<style>
    label { margin-top: 10px; }
</style>

<h2>내 정보 수정</h2>
<hr />

<form:form method="post" modelAttribute="user">

    <label>로그인ID</label>
    <form:input path="loginId" />

    <label>이름</label>
    <form:input path="name" />

    <label>이메일</label>
    <form:input path="email" />

    <label>사용자 유형</label>
    <form:select path="userType">
        <form:option value="관리자" />
        <form:option value="교수" />
        <form:option value="학생" />
    </form:select>

    <label>학과</label>
    <form:select path="departmentId">
        <form:option value="" label="없음" />
        <form:options itemValue="id" itemLabel="departmentName" items="${ departments }" />
    </form:select>
    <hr />

    <div>
        <button type="submit" class="btn btn-primary">
            <i class="icon-ok icon-white"></i> 저장
        </button>
    </div>
</form:form>

