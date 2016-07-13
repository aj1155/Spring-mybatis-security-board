<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<script src="/bbs1/res/se2/js/HuskyEZCreator.js" type="text/javascript"></script>
<script src="/bbs1/res/se2/init.js" type="text/javascript"></script>
<style>
  input[name=title] { width: 700px; border-style: groove; margin: 4px;}
  textarea { width: 95%; height: 600px; }
</style>

<h2>${ pagination.boardId == 1 ? "공지사항" : "자유게시판" }</h2>
<hr />

<form method="post">
    <div>
        <span>제목:</span>
        <input type="text" name="title" />
    </div>
    <textarea id="body" name="body" class="smarteditor2"></textarea>

    <div>
        <button type="submit" class="btn btn-primary">
            <i class="icon-ok icon-white"></i> 저장하기
        </button>
        <a href="list.do?${ pagination.queryString }" class="btn">
            <i class="icon-ban-circle"></i> 취소
        </a>
    </div>
</form>
