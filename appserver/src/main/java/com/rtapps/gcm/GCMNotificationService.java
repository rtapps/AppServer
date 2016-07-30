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

	public void pushNotificationToGCM(String googleApiKey, List<String> gcmRegId,String message){

		final int retries = 3;
		Sender sender = new Sender(googleApiKey);
		Message msg = new Message.Builder().addData("message",message).build();


		try {
			MulticastResult results = sender.send(msg, gcmRegId, retries);

			for (Result result: results.getResults()){
				if (StringUtils.isEmpty(result.getErrorCodeName())) {
					System.out.println("GCM Notification is sent successfully" + result.toString());
				}
				else{
					System.out.println("Error occurred while sending push notification :" + result.getErrorCodeName());
				}
			}
		} catch (InvalidRequestException e) {
			System.out.println("Invalid Request");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
	}
}