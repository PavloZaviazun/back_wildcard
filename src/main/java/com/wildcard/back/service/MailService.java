package com.wildcard.back.service;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@PropertySource("classpath:application.properties")
public class MailService {
    private JavaMailSender javaMailSender;
    @Autowired
    Environment env;

    public void sendEmailToEnableUser(String email, int id) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("email.username")));
            helper.setTo(email);
            helper.setSubject("Activate your account on wildcard.in.ua");
            String emailForm =
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                            " <head>\n" +
                            "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                            "  <title>This is automatically generated Email</title>\n" +
                            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                            "</head>\n" +
                            "<body style=\"margin: 0; padding: 0; font-size: 13pt;\">\n" +
                            "<div>Welcome to the Wildcard website!</div>" +
                            "<div>Please, follow the <b><a href='http://localhost:8080/activate/" + id + "'>link</a></b> to activate your account.</div>" +
                            "</body>" +
                            "</html>";
            helper.setText(emailForm, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendEmailToChangeMail(String email, int id) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("email.username")));
            helper.setTo(email);
            helper.setSubject("Change your e-mail address on wildcard.in.ua");
            String emailForm =
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                            " <head>\n" +
                            "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                            "  <title>This is automatically generated Email</title>\n" +
                            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                            "</head>\n" +
                            "<body style=\"margin: 0; padding: 0; font-size: 13pt;\">\n" +
                            "<div>You are going to change your e-mail address on the Wildcard</div>" +
                            "<div>Please, follow the <b><a href='http://localhost:8080/update/" + id + "/newemail/" + email + "'>link</a></b> to activate your account.</div>" +
                            "<div>If you didn't send any requests, ignore this e-mail.</div>" +
                            "</body>" +
                            "</html>";
            helper.setText(emailForm, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendFeedback(String email, String theme, String message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("email.username")));
            helper.setTo(env.getProperty("email.username"));
            helper.setSubject("New feedback from the form");
            String emailForm =
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                            " <head>\n" +
                            "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                            "  <title>This is automatically generated Email</title>\n" +
                            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                            "</head>\n" +
                            "<body style=\"margin: 0; padding: 0; font-size: 13pt;\">\n" +
                            "<div>You received new feedback</div>" +
                            "<div>From: " + email + "</div>" +
                            "<div>Theme: " + theme + "</div>" +
                            "<div>Message: " + message + "</div>" +
                            "</body>" +
                            "</html>";
            helper.setText(emailForm, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }
}
