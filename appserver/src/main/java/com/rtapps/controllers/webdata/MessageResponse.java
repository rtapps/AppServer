package com.rtapps.controllers.webdata;


import com.rtapps.db.mongo.data.Message;

import java.util.List;

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
