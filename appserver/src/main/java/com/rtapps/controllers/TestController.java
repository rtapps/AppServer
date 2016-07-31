package com.rtapps.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.rtapps.aws.S3Wrapper;
import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.data.PushToken;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import com.rtapps.db.mongo.repository.PushTokenRepository;
import com.rtapps.gcm.GCMNotificationService;
import org.bson.types.ObjectId;
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

	@Autowired
	PushTokenRepository pushTokenRepository;

	@RequestMapping("/test")
	@ResponseBody
	public List<AdminUser> test(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
								Model model) {

		List<AdminUser> userList = adminUserReposiroty.findByFirstName("Tzachi");

		return userList;
	}

	@RequestMapping(value = "/testSendPush", method = RequestMethod.POST)
	@ResponseBody
	public String messages(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "message", required = true) String message,
			Model model) {


		List<PushToken> pushTokens = pushTokenRepository.findByApplicationId(applicationId);
		AdminUser adminUser = adminUserReposiroty.findByApplicationId(applicationId);
		GCMNotificationService gcmNotificationService = new GCMNotificationService();

		gcmNotificationService.sendAsyncPushNotification(adminUser, pushTokens, message, pushTokenRepository);

		return "";

	}

	@Autowired
	private S3Wrapper s3Wrapper;

	@RequestMapping(value = "/testList", method = RequestMethod.GET)
	@ResponseBody
	public List<S3ObjectSummary> list() throws IOException {
		ObjectId id1 = new ObjectId();
		ObjectId id2 = new ObjectId();

		return s3Wrapper.list();
	}

}