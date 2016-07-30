package com.rtapps.gcm;

import com.google.android.gcm.server.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by vivex on 12/6/15.
 */
@Service
public class GCMNotificationService {

	final String GCM_API_KEY = "AIzaSyBkn3RG7Cv7j_edkYw5ohf7Y0WvuS3tX3Y";

	public boolean pushNotificationToGCM(List<String> gcmRegId,String message){

		final int retries = 3;
		Sender sender = new Sender(GCM_API_KEY);
		Message msg = new Message.Builder().addData("message",message).build();


		try {
			Result result = sender.send(msg, gcmRegId.get(0), retries);
			/**
			 * if you want to send to multiple then use below method
			 * send(Message message, List<String> regIds, int retries)
			 **/


			if (StringUtils.isEmpty(result.getErrorCodeName())) {
				System.out.println("GCM Notification is sent successfully" + result.toString());
				return true;
			}

			System.out.println("Error occurred while sending push notification :" + result.getErrorCodeName());

		} catch (InvalidRequestException e) {
			System.out.println("Invalid Request");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
		return false;

	}
}