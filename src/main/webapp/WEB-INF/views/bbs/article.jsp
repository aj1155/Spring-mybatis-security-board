<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
    div.article-header span.title { font-size: 14pt; font-weight: bold; }
    div.article-header span.lbl { color: #999; }
    div.article-header span.txt { margin-right: 40px; }
    div.body { min-height: 400px; }
</style>

<div class="pull-right">
    <a href="list.do?${ pagination.queryString }" class="btn">
        <i class="icon-list"></i> 목록으로
    </a>
</div>

<h2>${ pagination.boardId == 1 ? "공지사항" : "자유게시판" }</h2>
<hr />

<div class="article-header">
    <span class="lbl">제목:</span> <span class="title">${ article.title }</span>
    <hr />
    <span class="lbl">글번호:</span> <span class="txt">${ article.no }</span>
    <span class="lbl">작성자:</span> <span class="txt">${ article.name }</span>
    <span class="lbl">작성일:</span> <span class="txt">
        <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${ article.writeTime }" /></span>
    <span class="lbl">조회수:</span> <span class="txt">${ article.readCount }</span>
    <hr />
</div>

<div class="body">${ article.body }</div>
