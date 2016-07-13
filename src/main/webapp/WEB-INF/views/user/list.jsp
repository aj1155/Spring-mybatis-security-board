<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<h2>사용자 목록</h2>
<hr />

<form:form method="get" modelAttribute="pagination" class="pagination">
    <input type="hidden" name="pg" value="1" />

    <div class="form-inline">
        <span>정렬:</span>
        <form:select path="od" data-auto-submit="true">
            <form:option value="0" label="ID 순서" />
            <form:option value="1" label="로그인ID 순서" />
            <form:option value="2" label="이름 순서" />
        </form:select>
        <form:select path="ss">
            <form:option value="0" label="검색조건" />
            <form:option value="1" label="아이디" />
            <form:option value="2" label="이름" />
            <form:option value="3" label="학과" />
        </form:select>
        <form:input path="st" />
        <button type="submit" class="btn btn-small">검색</button>
        <c:if test="${ pagination.ss != 0 }">
            <a href="list.do" class="btn btn-small">취소</a>
        </c:if>
    </div>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>id</th>
                <th>loingId</th>
                <th>name</th>
                <th>email</th>
                <th>userType</th>
                <th>department</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${ list }">
                <tr data-url="edit.do?id=${ user.id }&${ pagination.queryString }">
                    <td>${ user.id }</td>
                    <td>${ user.loginId }</td>
                    <td>${ user.name }</td>
                    <td>${ user.email }</td>
                    <td>${ user.userType }</td>
                    <td>${ user.departmentName }</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <form:select path="sz" data-auto-submit="true">
        <form:option value="10" />
        <form:option value="15" />
        <form:option value="30" />
        <form:option value="100" />
    </form:select>

    <div class="pagination pagination-small pagination-centered">
        <ul>
            <c:forEach var="page" items="${ pagination.pageList }">
                <li class='${ page.cssClass }'><a data-page="${ page.number }">${ page.label }</a></li>
            </c:forEach>
        </ul>
    </div>
</form:form>
