<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<style>
    td:nth-child(5) { width: 40px; text-align: right; }
</style>

<h2>${ pagination.boardId == 1 ? "공지사항" : "자유게시판" }</h2>
<hr />

<form:form method="get" modelAttribute="pagination" class="pagination">
    <input type="hidden" name="pg" value="1" />
    <form:hidden path="bd" />

    <div class="form-inline">
        <form:select path="ss">
            <form:option value="0" label="검색조건" />
            <form:option value="1" label="작성자" />
            <form:option value="2" label="제목" />
            <form:option value="3" label="제목+내용" />
        </form:select>
        <form:input path="st" />
        <button type="submit" class="btn btn-small">검색</button>
        <c:if test="${ pagination.ss != 0 }">
            <a href="list.do?bd=${pagination.bd}" class="btn btn-small">취소</a>
        </c:if>
    </div>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>no</th>
                <th>제목</th>
                <th>작성자</th>
                <th>시각</th>
                <th>조회</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="article" items="${ list }">
                <tr data-url="article.do?id=${article.id}&${pagination.queryString}">
                    <td>${ article.no }</td>
                    <td>${ article.title }</td>
                    <td>${ article.name }</td>
                    <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
                                        value="${ article.writeTime }" /></td>
                    <td>${ article.readCount }</td>
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
