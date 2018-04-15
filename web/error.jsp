<%--
  Created by IntelliJ IDEA.
  User: Teomant
  Date: 11.04.2018
  Time: 19:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@page errorPage="error.jsp" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
what have you done?!
<br/>
Message:
<%=exception.getMessage()%> <br/>
<%=exception.printStackTrace()%>
</body>
</html>
