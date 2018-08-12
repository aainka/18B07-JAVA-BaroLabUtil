package com.barolab.util;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// 방화벽으로 나가지 않음.

public class MailTest {

	public static void main(String[] args) {

		final String username = "aainka@gmail.com";
		final String password = "inka4723";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		System.out.println("Step-1");
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("aainka@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("aainka@gmail.com")); 
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");
			System.out.println("Step-2");
			Transport.send(message);
			System.out.println("Step-3");
			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
