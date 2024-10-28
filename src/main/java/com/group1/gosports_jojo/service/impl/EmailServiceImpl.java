package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.exception.EmailSendFailedException;
import com.group1.gosports_jojo.service.EmailService;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendMail(String to, String subject, String messageText)  {
        try {
            // 設定使用SSL連線至 Gmail smtp Server
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            final String myGmail = "ixlogic.wu@gmail.com";
            final String myGmail_password = "ddjomltcnypgcstn";
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myGmail, myGmail_password);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myGmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            // 設定信中的主旨
            message.setSubject(subject);
            // 設定信中的內容
            message.setText(messageText);
            Transport.send(message);
            System.out.println("傳送成功!");
        }catch (MessagingException e){
            System.out.println("傳送失敗!");
            e.printStackTrace();
            throw new EmailSendFailedException("郵件寄送失敗", e);
        }
    }


}
