<%--
  Created by IntelliJ IDEA.
  User: Teomant
  Date: 11.04.2018
  Time: 19:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page errorPage="" %>
<html>
  <head>
    <title>Login</title>
  </head>
  <body>
    <h3>Login with username and password</h3>
    <form action="Login" method="post">
      <strong>Username</strong>:<input type="text" name="username"><br>
      <strong>Password</strong>:<input type="password" name="password"><br>
      <input type="submit" value="Login">
    </form>
    <br>
    If you are new user, please <a href="Register">register</a>.
  </body>
</html>
