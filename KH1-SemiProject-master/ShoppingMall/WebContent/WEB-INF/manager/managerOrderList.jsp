<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= ctxPath %>/css/style.css" />
<title>managerOrderList.jsp</title>
<!-- 차트 링크 --> 
<script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script> 
<style type="text/css">
	.sideMenu{
		margin-top:10px;
		display: inline-block;
		width: 150px;
		float:left;
	}
	.orderList{
		display:inline-block;
		width:800px;
		margin-top: 10px;
	}
	
	.order-table{
		width: 100%;
		text-align: center;
	}
	
	.member-search{
		width:100%;
		margin-bottom:5px;
	}
	
	.goods-add{
		float: right;
		margin-bottom:5px;
	}
	
	.board-title{
		width: 400px;
	}
	
	.type{
		border:solid 1px purple;
		margin-left:10px;
		padding:10px 20px;
		color:purple;
	}
	
	.type:hover{
		cursor: pointer;
		background-color: purple;
		color:white;
	}
	
	
</style>
<!-- 부트스트랩 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/ShoppingMall/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/ShoppingMall/util/myutil.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		
	});
	
</script>
</head>
<body>
	<div class="Mycontainer">
		<jsp:include page="../include/header.jsp"></jsp:include>
		<div class="section" align="center">
			<div class="contents">
				<div class="sideMenu">
					<jsp:include page="../include/managerSide.jsp"></jsp:include>
				</div>
				<div class="orderList" align="left">
					<div class="member-search">
						<h4>주문관리</h4>
						검색 : <input type="text" />
						<select>
							<option>선택</option>
							<option>주문자</option>
							<option>주문번호</option>
						</select>
					</div>
					<table class="table order-table" style="border-top:solid 2px purple;">
						<tr>
							<td>선택</td>
							<td>No.</td>
							<td>주문번호</td>
							<td>주문자</td>
							<td>주소</td>
							<td>금액</td>
							<td>주문상태</td>
						</tr>
						<tr>
							<td><input type="checkbox" /></td>
							<td>No.</td>
							<td>701050</td>
							<td>test</td>
							<td class="board-title">인천시 남동구</td>
							<td>15,000</td>
							<td>상품출하</td>
						</tr>
					</table>
				</div>
				<div style="clear:both;"></div>
				<div class="paging">
					
				</div>
			</div>
		</div>
		<jsp:include page="../include/footer.jsp"></jsp:include>
	</div>
</body>
</html>