<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>哔哩哔哩</title>
</head>
<body>
	<table border=1>
		<thead>
			<tr>
				<th>视频编号</th>
				<th>弹幕编号</th>
				<th>分P</th>
				<th>分P标题</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="list" items="${list}">
				<tr>
					<th><a href="http://www.bilibili.com/video/av${list.aid}">${list.aid}</a></th>
					<th><a href="http://comment.bilibili.com/${list.cid}.xml">${list.cid}</a></th>
					<th>${list.partid}</th>
					<th>${list.partname}</th>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>