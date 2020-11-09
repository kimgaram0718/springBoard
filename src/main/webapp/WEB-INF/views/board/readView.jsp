<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
	<head>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
		<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	 	<title>게시판</title>
	</head>
	
	<script type="text/javascript">
		$(document).ready(function(){
			var formObj = $("form[name='readForm']");
			
			// 수정 
			$(".update_btn").on("click", function(){
				formObj.attr("action", "/board/updateView");
				formObj.attr("method", "get");
				formObj.submit();				
			})
			
			// 삭제
			$(".delete_btn").on("click", function(){
				
				var deleteYN = confirm("삭제하시겠습니까?");
				if(deleteYN == true){
					formObj.attr("action", "/board/delete");
					formObj.attr("method", "post");
					formObj.submit();
				}
			})
			
			// 취소
			$(".list_btn").on("click", function(){
				
				location.href = "/board/list";
			})
			
			// 목록
			$(".list_btn").on("click", function(){

				location.href = "/board/list?page=${scri.page}"
				+"&perPageNum=${scri.perPageNum}"
				+"&searchType=${scri.searchType}&keyword=${scri.keyword}";
			})
			
			//======
			//댓글관련
			//댓글 작성
			$(".replyWriteBtn").on("click", function() {
				var formObj = $("form[name='replyForm']");
				formObj.attr("action", "/board/replyWrite");
				formObj.submit();
			})
			//댓글수정view
			$(".replyUpdateBtn").on("click", function() {
				location.href = "/board/replyUpdateView?bno=${read.bno}"
						       +"&page=${scri.page}"
						       +"&perPageNum=${scri.perPageNum}"
						       +"&searchType=${scri.searchType}"
						       +"&keyword=${scri.keyword}"
						       +"&rno=" + $(this).attr("data-rno");
			})
			//댓글삭제view
			$(".replyDeleteBtn").on("click", function(){
				location.href = "/board/replyDeleteView?bno=${read.bno}"
					+ "&page=${scri.page}"
					+ "&perPageNum=${scri.perPageNum}"
					+ "&searchType=${scri.searchType}"
					+ "&keyword=${scri.keyword}"
					+ "&rno="+$(this).attr("data-rno");
			})
		});
	</script>
	
	<body>
	
		<div class="container">
			<header>
				<h1> 게시판</h1>
			</header>
			<hr />
			 
			<div>
				<%@include file="nav.jsp" %>
			</div>
			<hr />
			
			<section id="container">
				<form name="readForm" role="form" method="post">
					<input type="hidden" id="bno" name="bno" value="${read.bno}" />
					<input type="hidden" id="page" name="page" value="${scri.page}"> 
  					<input type="hidden" id="perPageNum" name="perPageNum" value="${scri.perPageNum}"> 
  					<input type="hidden" id="searchType" name="searchType" value="${scri.searchType}"> 
  					<input type="hidden" id="keyword" name="keyword" value="${scri.keyword}">
				</form>
				
				<!-- add1 -->
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">제목</label>
					<input type="text" id="title" name="title" class="form-control" value="${read.title}" readonly="readonly" />
				</div>
				
				<div class="form-group">
					<label for="content" class="col-sm-2 control-label">내용</label>
					<textarea id="content" name="content" class="form-control" readonly="readonly">
						<c:out value="${read.content}" />
					</textarea>
				</div>
				
				<div class="form-group">
					<label for="writer" class="col-sm-2 control-label">작성자</label>
					<input type="text" id="writer" name="writer" class="form-control" value="${read.writer}" readonly="readonly" />
				</div>
				
				<div class="form-group">
					<label for="regdate" class="col-sm-2 control-label">작성날짜</label>
					<fmt:formatDate value="${read.regdate}" pattern="yyyy-MM-dd" />
				</div>
				<!-- add2 -->
				
				<!-- table 범위 대신 위처럼 부트스트랩 형태로 적용함 -->
				<!--  
				<table>
					<tbody>
						<tr>
							<td>
								<label for="title">제목</label><input type="text" id="title" name="title" value="${read.title}" readonly="readonly" />
							</td>
						</tr>	
						<tr>
							<td>
								<label for="content">내용</label><textarea id="content" name="content" readonly="readonly"><c:out value="${read.content}" /></textarea>
							</td>
						</tr>
						<tr>
							<td>
								<label for="writer">작성자</label><input type="text" id="writer" name="writer" value="${read.writer}"  readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td>
								<label for="regdate">작성날짜</label>
								<fmt:formatDate value="${read.regdate}" pattern="yyyy-MM-dd" />					
							</td>
						</tr>		
					</tbody>			
				</table>
				-->
				
				<div>
					<button type="button" class="update_btn btn btn-warning">수정</button>
					<button type="button" class="delete_btn btn btn-danger">삭제</button>
					<button type="button" class="list_btn btn btn-primary">목록</button>	
				</div>
				
				<!-- add1 -->
				<!-- 댓글 -->
				<div id="reply">
					<ol class="replyList">
						<!-- 
						class => BoardController,
						method => read();
						ex => model.addAttribute("replyList", replyList);
						-->
						<c:forEach items="${replyList}" var="replyList">
							<li>
								<p>
								작성자 : ${replyList.writer}<br />
								작성 날짜 : <fmt:formatDate value="${replyList.regdate}" pattern="yyyy-MM-dd" />
								</p>
								
								<p>
									${replyList.content}<br />
								</p>
								
								<!-- add1 -->
								<!-- readView에서 댓글의 수정,삭제 버튼 생성  -->
								<div>
									<!-- 원래 class에  replyUpdateBtn 만 있었으나 부트스트랩 버전으로 변경-->
									<button type="button" class="replyUpdateBtn btn btn-warning" data-rno="${replyList.rno}">수정</button>
									<button type="button" class="replyDeleteBtn btn btn-danger" data-rno="${replyList.rno}">삭제</button>
									
								</div>
								<!-- add2 -->
							</li>
						</c:forEach>
					</ol>
				</div>
				<!-- add2 -->
				
				<!-- add1 -->
				<!-- 댓글작성 위한 form 태그 생성 -->
				<!-- value의 read, scri는 어떤 것과 연동된 것인지 잘 모르겠음  -->
				<form name="replyForm" method="post" class="form-horizontal">
					<input type="hidden" id="bno" name="bno" value="${read.bno}" />
					<input type="hidden" id="page" name="page" value="${scri.page}" />
					<input type="hidden" id="perPageNum" name="perPageNum" value="${scri.perPageNum}" />
					<input type="hidden" id="searchType" name="searchType" value="${scri.searchType}" />
					<input type="hidden" id="keyword" name="keyword" value="${scri.keyword}" />
					
					<div class="form-group">
						<label for="writer" class="col-sm-2 control-label">댓글 작성자</label>
						<div class="col-sm-10">
							<input type="text" id="writer" name="writer" class="form-control" />
						</div>
					</div>
					
					<div class="form-group">
						<label for="content" class="col-sm-2 control-label">댓글 내용</label>
						<div class="col-sm-10">
							<input type="text" id="content" name="content" class="form-control" />
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" class="replyWriteBtn btn btn-success">작성</button>
						</div>
					</div>
					<!-- ====== -->
				</form>
				<!-- add2 -->
				
			</section>
			<hr />
		</div>
	</body>
</html>