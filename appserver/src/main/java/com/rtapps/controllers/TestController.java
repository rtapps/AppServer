package com.rtapps.controllers;

import java.util.ArrayList;
import java.util.List;

import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.data.PushToken;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import com.rtapps.db.mongo.repository.PushTokenRepository;
import com.rtapps.gcm.GCMNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@Autowired
	PushTokenRepository pushTokenRepository;

	@RequestMapping(value = "/testSendPush", method = RequestMethod.POST)
	@ResponseBody
	public String messages(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "message", required = true) String message,
			Model model) {


		List<PushToken> pushTokens = pushTokenRepository.findByApplicationId(applicationId);
		ArrayList<String> tokenIds = new ArrayList<>();

		for (PushToken pushToken : pushTokens){
			tokenIds.add(pushToken.getPushToken());
		}
		GCMNotificationService gcmNotificationService = new GCMNotificationService();

		AdminUser adminUser = adminUserReposiroty.findByApplicationId(applicationId);

		gcmNotificationService.pushNotificationToGCM(adminUser.getGoogleApiKey(), tokenIds, message);

		return "";

	}

}