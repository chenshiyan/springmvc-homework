<%@ page import="com.csy.edu.pojo.Resume" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: chen
  Date: 2020/8/9
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>chenshiyan</title>
</head>
<body>
<%
    List<Resume> resumes = (List<Resume>) request.getAttribute("data");
%>
<table>
    <tr>
        <th>地址</th>
        <th>姓名</th>
        <th>手机号</th>
        <th>修改</th>
        <th>删除</th>
    </tr>
    <%
        for (int i = 0; i < resumes.size(); i++) {
            Resume resume = resumes.get(i);
    %>
        <tr>
            <td><%=resume.getAddress()%></td>
            <td><%=resume.getName()%></td>
            <td><%=resume.getPhone()%></td>
            <td><a href="/resume/toUpdateResume?id=<%=resume.getId()%>&name=<%=resume.getName()%>&address=<%=resume.getAddress()%>&phone=<%=resume.getPhone()%>">修改</a></td>
            <td><a href="/resume/deleteResume?id=<%=resume.getId()%>">删除</a><%=resume.getId()%></td>
        </tr>
    <%
        }
    %>
</table>
</body>
</html>
