package appserver.test;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import appserver.db.mongo.data.AdminUser;
import appserver.db.mongo.repository.AdminUserRepository;

/**
 * Servlet implementation class TestServlet
 */

@Controller
public class TestServlet {
	
	@Autowired AdminUserRepository adminUserReposiroty;

	@RequestMapping("/test")
	@ResponseBody
	public List<AdminUser> test(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		
		 List<AdminUser> userList = adminUserReposiroty.findByFirstName("Tzachi");
		
		return userList;
	}

}

// @WebServlet("/TestServlet")
// public class TestServlet extends HttpServlet {
// private static final long serialVersionUID = 1L;
//
// /**
// * @see HttpServlet#HttpServlet()
// */
// public TestServlet() {
// super();
//
// // TODO Auto-generated constructor stub
// }
//
// @Autowired AdminUserReposiroty adminUserReposiroty;
//
// /**
// * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
// response)
// */
// protected void doGet(HttpServletRequest request, HttpServletResponse
// response) throws ServletException, IOException {
// //
//
// List<AdminUser> userList = adminUserReposiroty.findByFirstName("Tzachi");
//
//
// for (int i=0;i< userList.size(); i++){
// AdminUser adminUser = userList.get(i);
//
// response.getWriter().append(adminUser.toString());
// }
// }
//
// /**
// * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
// response)
// */
// protected void doPost(HttpServletRequest request, HttpServletResponse
// response) throws ServletException, IOException {
// // TODO Auto-generated method stub
// doGet(request, response);
// }
//
// }
