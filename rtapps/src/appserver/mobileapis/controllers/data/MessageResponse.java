package appserver.mobileapis.controllers.data;
import java.util.List;

import appserver.db.mongo.data.Message;

public class MessageResponse {
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