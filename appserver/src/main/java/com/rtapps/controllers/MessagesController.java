package com.rtapps.controllers;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.rtapps.aws.S3Wrapper;
import com.rtapps.controllers.webdata.MessageResponse;
import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.data.Message;
import com.rtapps.db.mongo.data.PushToken;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import com.rtapps.db.mongo.repository.MessageRepository;
import com.rtapps.db.mongo.repository.PushTokenRepository;
import com.rtapps.gcm.GCMNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;


@Controller
public class MessagesController {

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	AdminUserRepository adminUserReposiroty;

	@Autowired
	PushTokenRepository pushTokenRepository;

	@Autowired
	private S3Wrapper s3Wrapper;

	@Value("${cloud.aws.s3.myFileServerPath}")
	private String myFileServerPath;

	@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Uploaded file must be an image")
	public class WrongFileTypeException extends RuntimeException {

	}
	@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="File is empty")
	public class FileEmptyTypeException extends RuntimeException {

	}


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

	@RequestMapping(value = "/putMessage", method = RequestMethod.POST)
	@ResponseBody
	public Message putMessage(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "messageHeader", required = true) String messageHeader,
			@RequestParam(value = "messageBody", required = true) String messageBody,
			@RequestParam(value = "sendPush", required = false) boolean sendPush,
			@RequestParam(value = "file", required = true) MultipartFile[] multipartFiles) {

		if (multipartFiles.length == 0){
			throw new FileEmptyTypeException();
		}

		for (int i=0;i<multipartFiles.length;i++){
			if (!multipartFiles[i].getContentType().contains("image")){
				throw new WrongFileTypeException();
			}
		}

		Date date = new Date();
		long now = date.getTime();
		Message message = new Message(applicationId, messageHeader, messageBody, myFileServerPath, multipartFiles[0].getOriginalFilename(), now, now, true);
		message = messageRepository.save(message);

		s3Wrapper.upload(multipartFiles, "images/" + applicationId + "/" + message.getId() + "/");

		if (sendPush){
			List<PushToken> pushTokens = pushTokenRepository.findByApplicationId(applicationId);
			AdminUser adminUser = adminUserReposiroty.findByApplicationId(applicationId);
			GCMNotificationService gcmNotificationService = new GCMNotificationService();

			gcmNotificationService.sendAsyncPushNotification(adminUser, pushTokens, messageHeader, pushTokenRepository);
		}

		return message;
	}
}
