package com.rtapps.controllers;

import java.util.Date;
import java.util.List;

import com.rtapps.aws.S3Wrapper;
import com.rtapps.controllers.webdata.MessageResponse;
import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.data.Message;
import com.rtapps.db.mongo.data.PushToken;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import com.rtapps.db.mongo.repository.MessageRepository;
import com.rtapps.db.mongo.repository.PushTokenRepository;
import com.rtapps.gcm.GCMNotificationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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

	@RequestMapping(value = "/deleteMessage", method = RequestMethod.POST)
	@ResponseBody
	public Message deleteMessage(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "messageId", required = true) String messageId){

		Message message = messageRepository.findByApplicationIdAndId(applicationId, messageId);
		message.setExists(false);

		Date date = new Date();
		long now = date.getTime();

		message.setLastUpdateDate(now);
		messageRepository.save(message);

		if (message != null)
		{
			String fullImageName = "images/" + applicationId + "/" + message.getId() + "/" + message.getFullImageName();
			s3Wrapper.delete(fullImageName);
			String previewImageName = "images/" + applicationId + "/" + message.getId() + "/" + message.getPreviewImageName();
			s3Wrapper.delete(previewImageName);
		}

		return message;
	}

	@RequestMapping(value = "/putMessage", method = RequestMethod.POST)
	@ResponseBody
	public Message putMessage(
			@RequestParam(value = "applicationId") String applicationId,
			@RequestParam(value = "messageHeader") String messageHeader,
			@RequestParam(value = "messageBody") String messageBody,
			@RequestParam(value = "sendPush", required = false) boolean sendPush,
			@RequestParam(value = "fullImage") MultipartFile fullImage,
			@RequestParam(value = "previewImage") MultipartFile previewImage) {

		if (fullImage.getSize() == 0 || previewImage.getSize() == 0){
			throw new FileEmptyTypeException();
		}

		Date date = new Date();
		long now = date.getTime();
		ObjectId objectId = new ObjectId();

		Message message = new Message(objectId.toHexString(), applicationId, messageHeader, messageBody, myFileServerPath, fullImage.getOriginalFilename(), previewImage.getOriginalFilename(), now, now, true);

		s3Wrapper.upload(fullImage, "images/" + applicationId + "/" + message.getId() + "/");
		s3Wrapper.upload(previewImage, "images/" + applicationId + "/" + message.getId() + "/");

		message = messageRepository.save(message);

		if (sendPush){
			List<PushToken> pushTokens = pushTokenRepository.findByApplicationId(applicationId);
			AdminUser adminUser = adminUserReposiroty.findByApplicationId(applicationId);
			GCMNotificationService gcmNotificationService = new GCMNotificationService();

			gcmNotificationService.sendAsyncPushNotification(adminUser, pushTokens, messageHeader, pushTokenRepository);
		}

		return message;
	}
}
