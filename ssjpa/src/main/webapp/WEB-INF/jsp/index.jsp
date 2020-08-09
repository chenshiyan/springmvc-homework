<%--
  Created by IntelliJ IDEA.
  User: chen
  Date: 2020/8/9
  Time: 15:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Spring MVC 列表查询</h2>
<fieldset>
    <a href="/resume/toAddResume">新增简历</a>
    <form action="/resume/findAllResume" method="get">
        <p>测试用例：列表查询</p>
        <input type="submit" value="点击查询">
    </form>
</fieldset>
</body>
</html>
