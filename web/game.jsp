<%@ page import="org.teomant.util.User" %><%--
  Created by IntelliJ IDEA.
  User: Teomant
  Date: 12.04.2018
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Let`s play!</title>
</head>
<body>
    <%
        User user =(User) session.getAttribute("User");
        String result = user.getResult();
        boolean solved = (result.contains(": RIGHT!</br>"));
        String buttonName="I feel lucky";
        if (solved){
            buttonName="Get new";
        }
    %>
    <h3>Previous:</h3>
    <%=result%>
    <form action="Game" method="post">
        <strong>Your number</strong>:<input type="text" name="number"><br>
        <input type="submit" value="<%=buttonName%>">
    </form>
    <form action="Logout" method="post">
        <input type="submit" value="Logout" >
    </form>
</body>
</html>

