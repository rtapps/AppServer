package com.rtapps.gcm;

import com.google.android.gcm.server.*;
import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.data.PushToken;
import com.rtapps.db.mongo.repository.PushTokenRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by vivex on 12/6/15.
 */
@Service
public class GCMNotificationService
{


	public void sendAsyncPushNotification(AdminUser adminUser, List<PushToken> pushTokens, String message, PushTokenRepository pushTokenRepository) {

		ArrayList<String> tokenIds = new ArrayList<>();

		for (PushToken pushToken : pushTokens){
			tokenIds.add(pushToken.getPushToken());
		}

		ApplicationContext context = new AnnotationConfigApplicationContext(GCMConfig.class);
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("gcmTaskExecutor");

		GCMNotificationTask gcmNotificationTask = new GCMNotificationTask(adminUser.getGoogleApiKey(), tokenIds, message, pushTokenRepository);

		taskExecutor.execute(gcmNotificationTask);
	}

}