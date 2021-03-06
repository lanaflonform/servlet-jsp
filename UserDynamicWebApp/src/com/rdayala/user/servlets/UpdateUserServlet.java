package com.rdayala.user.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateUserServlet
 */
@WebServlet("/updateServlet")
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	// this gets called ONLY ONCE
	public void init(ServletConfig config) {
		try {

			ServletContext context = config.getServletContext();
			
			Enumeration<String> parameterNames = context.getInitParameterNames();
			while(parameterNames.hasMoreElements()){
				String eachName = parameterNames.nextElement();
				System.out.println("Context param name : " +  eachName);
				System.out.println("Context param value : " + context.getInitParameter(eachName));
			}
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(context.getInitParameter("dbConnectionString"),
					context.getInitParameter("dbUserName"), context.getInitParameter("dbPassWord"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			Statement statement = connection.createStatement();
			int result = statement
					.executeUpdate("update user set password = '" + password + "' where email = '" + email + "'");
			PrintWriter out = response.getWriter();
			if (result > 0) {
				out.print("<h1>Password Updated..</h1>");
			} else {
				out.print("<h1>Error updating user.</h1>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
