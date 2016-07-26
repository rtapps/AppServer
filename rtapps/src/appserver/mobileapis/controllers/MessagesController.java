package appserver.mobileapis.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import appserver.db.mongo.data.Message;
import appserver.db.mongo.repository.MessageRepository;
import appserver.mobileapis.controllers.data.MessageResponse;

@Controller
public class MessagesController {
	
	
	@Autowired MessageRepository messageRepository;
	
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public MessageResponse messages(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "fromTime", required = true) long fromTime,
			Model model) {
		
		 List<Message> messageList = messageRepository.findByApplicationIdAndLastUpdateDateGreaterThanOrderByLastUpdateDateDesc(applicationId, fromTime);
		 long lastUpdateTime = messageList.size() > 0? messageList.get(0).getLastUpdateDate(): fromTime;
		
		return new MessageResponse(messageList, lastUpdateTime);
	}
}
