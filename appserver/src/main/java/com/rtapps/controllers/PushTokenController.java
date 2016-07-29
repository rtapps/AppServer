package com.rtapps.controllers;

import com.rtapps.db.mongo.data.PushToken;
import com.rtapps.db.mongo.repository.PushTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PushTokenController {


	@Autowired
	PushTokenRepository pushTokenRepository;
	@RequestMapping(value = "/pushToken", method = RequestMethod.POST)
	@ResponseBody
	public PushToken messages(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "pushToken", required = true) String pushTokenStr,
			@RequestParam(value = "pushTokenId", required = false) String pushTokenId,
			Model model) {

		PushToken pushToken = null;
		if (pushTokenId != null){
			pushToken = pushTokenRepository.findByIdAndApplicationId(pushTokenId, applicationId);
			if (pushTokenId != null){
				pushToken.setPushToken(pushTokenStr);
			}
		}
		if (pushToken == null){
			pushToken = new PushToken(applicationId, pushTokenStr);
		}
		pushTokenRepository.save(pushToken);

		return pushToken;
	}
}
