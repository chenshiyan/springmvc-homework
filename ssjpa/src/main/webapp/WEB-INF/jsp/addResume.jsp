<%--
  Created by IntelliJ IDEA.
  User: chen
  Date: 2020/8/9
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>chenshiyan</title>
    <script type="text/javascript" src="/js/jquery.min.js"></script>

    <script>
        $(function () {

            $("#ajaxBtn").bind("click",function () {
                var name = $("#name").val();
                var address = $("#address").val();
                var phone = $("#phone").val();

                var params = {
                    "name":name,
                    "address":address,
                    "phone":phone
                };
                // 发送ajax请求
                $.ajax({
                    url: 'http://localhost:8080/resume/saveResume',
                    type: 'POST',
                    data: {
                        "name":name,
                        "address":address,
                        "phone":phone
                    },
                    // contentType: 'application/JSON',
                    // dataType: 'JSON',
                    success: function (data) {
                        alert("删除成功");
                    }
                })

            })


        })
    </script>
</head>
<body>
<h2>新增信息</h2>
<fieldset>
<%--    <%--%>
<%--        String id = request.getParameter("id");--%>
<%--        String phone = request.getParameter("phone");--%>
<%--        String address = request.getParameter("address");--%>
<%--        String username = request.getParameter("name");--%>
<%--    %>--%>
    姓名：<input type="text" id="name" name="name">
    地址：<input type="text" id="address" name="address">
    电话：<input type="text" id="phone" name="phone">
    <input type="submit" value="提交" id="ajaxBtn">
</fieldset>
</body>
</html>
