package com.wicc.personalassistant.utils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Component;
@Component
public class EmailServiceUtil {

    public void send(String receiver,String subject,String message){
        try{
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator("saphalpathak35@gmail.com", "gpgpqmklroafijox"));
            email.setSSLOnConnect(true);
            email.setFrom("saphalpathak35@gmail.com");
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(receiver);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}


