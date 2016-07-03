<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
				<th>视频标题</th>
				<th>视频编号</th>
				<th>弹幕编号</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th>${bili.title}</th>
				<th><a href="http://www.bilibili.com/video/av${bili.aid}">${bili.aid}</a></th>
				<th><a href="http://www.bilibili.com/video/av${bili.cid}">${bili.cid}</a></th>
			</tr>
		</tbody>
	</table>
</body>
</html>