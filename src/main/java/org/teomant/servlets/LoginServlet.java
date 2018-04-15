package org.teomant.servlets;

import org.teomant.util.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Вход в игру. Проверяем логин/пароль, сохраняем пользователя в атрибутах сервлета
 */
@WebServlet(name = "Login", urlPatterns = { "/Login" })
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String errorMsg = null;
        if(username == null || username.equals("")){
            errorMsg ="Username can't be null or empty";
        }
        if(password == null || password.equals("")){
            errorMsg = "Password can't be null or empty";
        }

        if(errorMsg != null){
            RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/index.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>"+errorMsg+"</font>");
            rd.include(request, response);
        }else{

            Connection con = (Connection) request.getServletContext().getAttribute("DBConnection");
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = con.prepareStatement("select id, username, password, attempts, correct, task, result  from public.\"Users\" where username=? and password=? limit 1");
                ps.setString(1, username);
                ps.setString(2, password);
                rs = ps.executeQuery();

                if(rs != null && rs.next()){

                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setId(rs.getLong("id"));
                    user.setAttempts(rs.getInt("attempts"));
                    user.setCorrect(rs.getInt("correct"));
                    user.setPassword(rs.getString("password"));
                    user.setTask(rs.getString("task"));
                    user.setResult(rs.getString("result"));

                    HttpSession session = request.getSession();
                    session.setAttribute("User", user);
                    response.sendRedirect("game.jsp");;
                }else{
                    RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/index.jsp");
                    PrintWriter out= response.getWriter();
                    out.println("<font color=red>No user found with given username, please register first.</font>");
                    rd.include(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("DB Connection problem.");
            }finally{
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                }

            }
        }
    }
}
