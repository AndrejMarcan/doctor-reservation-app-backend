package com.docapp.api;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.docapp.api"})
@EnableScheduling
public class App {

	public static void main(String[] args) throws MessagingException {
		SpringApplication.run(App.class, args);
		sendEmails();
	}

	private static void sendEmails() throws MessagingException {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", "*");
		
		Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("testovacia.adresa1@gmail.com", "Testovacia_1");
		    }
		});
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("testovacia.adresa1@gmail.com"));
		message.setRecipients(
		  Message.RecipientType.TO, InternetAddress.parse("yami.yatem@gmail.com"));
		message.setSubject("Stretnutie Ambulancia Bernie");

		String msg = "Test EMAIL AFTER STARTING OF THE APP";

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		Transport.send(message);
	}
}
