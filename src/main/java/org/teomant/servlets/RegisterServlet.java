package org.teomant.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * регистрируем пользователя, добавляем в базу данных
 */

@WebServlet(name = "Register", urlPatterns = { "/Register" })
public class RegisterServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/register.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String errorMsg = null;
        if(username == null || username.equals("")){
            errorMsg = "Email ID can't be null or empty.";
        }
        if(password == null || password.equals("")){
            errorMsg = "Password can't be null or empty.";
        }

        Connection con = (Connection) getServletContext().getAttribute("DBConnection");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("select * from public.\"Users\" where username=?");
            ps.setString(1, username);
            rs = ps.executeQuery();

            if(rs != null && rs.next()){
                errorMsg = "User are exist in the database.";
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

        if(errorMsg != null){
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.jsp");
            PrintWriter out;
            out = response.getWriter();
            out.println("<font color=red>"+errorMsg+"</font>");
            rd.include(request, response);
        }else{



            try {
                ps = con.prepareStatement("INSERT INTO public.\"Users\"(username, password, task, result, attempts, correct)" +
                        "VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, username);
                ps.setString(2, password);

                Random rand = new Random();
                int  n = rand.nextInt(9999);
                String task = String.format("%04d",n);
                ps.setString(3, task);
                ps.setString(4,"");
                ps.setInt(5,0);
                ps.setInt(6,0);

                ps.execute();

                RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
                PrintWriter out= response.getWriter();
                out.println("<font color=green>Registration successful, please login below.</font>");
                rd.include(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("DB Connection problem.");
            }finally{
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        }

    }

}
