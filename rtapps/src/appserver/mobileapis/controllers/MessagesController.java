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

@Controller
public class MessagesController {
	
	private static class MessageResponse {
		private long lastUpdateTime;
		private List<Message> messageList;
		
		public MessageResponse(List<Message> messageList, long lastUpdateTime){
			this.lastUpdateTime = lastUpdateTime;
			this.messageList = messageList;
		}
		public long getLastUpdateTime(){
			return this.lastUpdateTime;
		}
		
		public List<Message> getMessageList(){
			return this.messageList;
		}
	}
	
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
