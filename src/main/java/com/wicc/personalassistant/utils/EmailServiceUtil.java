package com.wicc.personalassistant.utils;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Component;
@Component
public class EmailServiceUtil {
    public void send(String receiver,String subject,String message){
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthentication("saphalpathak35@gmail.com","jfuufbexzmxjxvzv");
            email.setSSL(true);
            email.setFrom("saphalpathak35@gmail.com");
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(receiver);
            email.send();
            System.out.println("Sent");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed");
        }
    }
}


