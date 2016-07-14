<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script src="/bbs1/res/se2/js/HuskyEZCreator.js" type="text/javascript"></script>
<script src="/bbs1/res/se2/init.js" type="text/javascript"></script>
<style>
  form > div { margin-top: 5px; }
  input[name=title] { width: 700px; border-style: groove; margin: 4px;}
  textarea { width: 95%; height: 600px; }
  input[type=file] { width: 600px; margin-bottom: 5px; }
</style>
<script>
$(function() {
    $("button#addFile").click(function() {
        var tag = "<span>파일:</span> <input type='file' name='file' multiple /><br/>";
        $(tag).appendTo("div#files");
    });
    $("button[data-id]").click(function() {
        $("input#fileId").val($(this).attr("data-id"));
    });
});
</script>

<h2>${ pagination.boardId == 1 ? "공지사항" : "자유게시판" }</h2>
<hr />

<form:form method="post" modelAttribute="article" enctype="multipart/form-data">
    <div>
        <span>제목:</span>
        <form:input path="title" />
    </div>
    <form:textarea path="body" class="smarteditor2" />
    <div>
        <span class="lbl">첨부파일:</span>
        <c:forEach var="file" items="${ files }">
            <button type="submit" class="btn btn-small" name="cmd" value="deleteFile" 
                    data-id="${file.id}">
                <i class="icon icon-remove"></i> ${ file.fileName } 파일 삭제
            </button>
        </c:forEach>
        <input type="hidden" id="fileId" name="fileId" value="0" />
    </div>
    <div id="files">
        <span>파일:</span>
        <input type="file" name="file" multiple /><br />
    </div>
    <div>
        <button type="submit" class="btn btn-primary" name="cmd" value="save">
            <i class="icon-ok icon-white"></i> 저장하기
        </button>
        <a href="article.do?id=${ article.id }&${ pagination.queryString }" class="btn">
            <i class="icon-ban-circle"></i> 취소
        </a>
        <button type="button" class="btn" id="addFile">
            <i class="icon-plus"></i> 파일추가
        </button>
    </div>
</form:form>
