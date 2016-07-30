package com.rtapps.gcm;

import com.google.android.gcm.server.*;
import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.data.PushToken;
import com.rtapps.db.mongo.repository.PushTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
@Configurable
@Scope("prototype")
public class GCMNotificationTask implements Runnable{

    private final String googleApiKey;

    private final List<String> gcmRegId;

    private final String message;

    PushTokenRepository pushTokenRepository;

    public GCMNotificationTask(String googleApiKey, List<String> gcmRegId,String message, PushTokenRepository pushTokenRepository){
        this.googleApiKey = googleApiKey;
        this.gcmRegId = gcmRegId;
        this.message = message;
        this.pushTokenRepository = pushTokenRepository;
    }

    @Override
    public void run() {

        pushNotificationToGCM(this.googleApiKey, this.gcmRegId, this.message);

    }

    public void pushNotificationToGCM(String googleApiKey, List<String> gcmRegId,String message){

        final int retries = 3;
        Sender sender = new Sender(googleApiKey);
        Message msg = new Message.Builder().addData("message",message).build();


        try {
            MulticastResult multicastResult = sender.send(msg, gcmRegId, retries);
            List<Result> results = multicastResult.getResults();
            for (int i=0; i< results.size(); i++){
                if (StringUtils.isEmpty(results.get(i).getErrorCodeName())) {
                    System.out.println("GCM Notification is sent successfully" + results.get(i).toString());
                }
                else{
                    handleSendPushError(results.get(i), gcmRegId.get(i));
                }
            }
        } catch (InvalidRequestException e) {
            System.out.println("Invalid Request");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    private void handleSendPushError(Result result, String registrationId) {
        System.out.println("Error occurred while sending push notification :" + result.getErrorCodeName());
        if (result.getErrorCodeName().equals("NotRegistered")){
            long l = pushTokenRepository.deleteByPushToken(registrationId);
            System.out.println("Deleted: " + l + " token: " + registrationId +" ");
        }
    }
}