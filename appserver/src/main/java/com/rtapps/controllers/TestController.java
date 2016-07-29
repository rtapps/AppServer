package com.rtapps.controllers;

import java.util.List;

import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Servlet implementation class TestServlet
 */

@Controller
public class TestController {

	@Autowired
	AdminUserRepository adminUserReposiroty;

	@RequestMapping("/test")
	@ResponseBody
	public List<AdminUser> test(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
								Model model) {

		List<AdminUser> userList = adminUserReposiroty.findByFirstName("Tzachi");

		return userList;
	}

}