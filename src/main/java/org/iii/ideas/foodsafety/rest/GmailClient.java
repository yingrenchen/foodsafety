package org.iii.ideas.foodsafety.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author BUDDHIMA
 */

public class GmailClient {

	private String userName;
	private String password;
	private String receivingHost;

	public void setAccountDetails(String userName, String password) {

		this.userName = userName;// sender's email can also use as User Name
		this.password = password;

	}

	// public void sendGmail(String from, String to, String subject, String
	// text) {
	//
	// // This will send mail from -->sender@gmail.com to -->receiver@gmail.com
	//
	// this.from = from;
	// this.to = to;
	// this.subject = subject;
	// this.text = text;
	//
	// // For a Gmail account--sending mails-- host and port shold be as
	// // follows
	//
	// this.sendingHost = "smtp.gmail.com";
	// this.sendingPort = 465;
	//
	// Properties props = new Properties();
	//
	// props.put("mail.smtp.host", this.sendingHost);
	// props.put("mail.smtp.port", String.valueOf(this.sendingPort));
	// props.put("mail.smtp.user", this.userName);
	// props.put("mail.smtp.password", this.password);
	//
	// props.put("mail.smtp.auth", "false");
	//
	// Session session1 = Session.getDefaultInstance(props);
	//
	// Message simpleMessage = new MimeMessage(session1);
	//
	// // MIME stands for Multipurpose Internet Mail Extensions
	//
	// InternetAddress fromAddress = null;
	// InternetAddress toAddress = null;
	//
	// try {
	//
	// fromAddress = new InternetAddress(this.from);
	// toAddress = new InternetAddress(this.to);
	//
	// } catch (AddressException e) {
	//
	// e.printStackTrace();
	//
	// JOptionPane.showMessageDialog(null, "Sending email to: " + to + " failed
	// !!!", "Falied to Send!!!",
	// JOptionPane.ERROR_MESSAGE);
	//
	// }
	//
	// try {
	//
	// simpleMessage.setFrom(fromAddress);
	// simpleMessage.setRecipient(RecipientType.TO, toAddress);
	// simpleMessage.setSubject(this.subject);
	// simpleMessage.setText(this.text);
	// Transport transport = session1.getTransport("smtps");
	// transport.connect(this.sendingHost, sendingPort, this.userName,
	// this.password);
	// transport.sendMessage(simpleMessage, simpleMessage.getAllRecipients());
	// transport.close();
	// JOptionPane.showMessageDialog(null, "Mail sent successfully ...", "Mail
	// sent", JOptionPane.PLAIN_MESSAGE);
	//
	// } catch (MessagingException e) {
	//
	// e.printStackTrace();
	//
	// JOptionPane.showMessageDialog(null, "Sending email to: " + to + " failed
	// !!!", "Falied to Send!!!",
	// JOptionPane.ERROR_MESSAGE);
	//
	// }
	//
	// }

	public List<File> readGmail() {

		/*
		 * this will print subject of all messages in the inbox of
		 * sender@gmail.com
		 */

		this.receivingHost = "imap.gmail.com";// for imap protocol

		Properties props2 = System.getProperties();

		props2.setProperty("mail.store.protocol", "imaps");

		// I used imaps protocol here

		Session session2 = Session.getDefaultInstance(props2, null);
		List<File> attachments = new ArrayList<File>();
		try {

			Store store = session2.getStore("imaps");

			store.connect(this.receivingHost, this.userName, this.password);

			Folder folder = store.getFolder("yyy");// get inbox

			folder.open(Folder.READ_ONLY);// open folder only to read

			Message message[] = folder.getMessages();

			System.out.println("==========================" + message.length);
			for (int i = 0; i < message.length; i++) {
				System.out.println("@1:" + message[i].getSentDate().getTime());
				System.out.println("@2:" + new Date().getTime());
				if (new Date().getTime() - message[i].getSentDate().getTime() <=  10*60 * 1000) {
					// print subjects of all mails in the inbox

					System.out.println(message[i].getSubject());
					// anything else you want
					System.out.println("-------------" + (i + 1) + "-----------");
					System.out.println(message[i].getSentDate());

					Multipart multipart = (Multipart) message[i].getContent();

					for (int i1 = 1; i1 < multipart.getCount(); i1++) {// 0是信件內容，附件從1開始
						String orgFileName = "";
						BodyPart bodyPart = multipart.getBodyPart(i1);
						InputStream is = bodyPart.getInputStream();
						String s = bodyPart.getFileName();
						System.out.println(s);
						String[] ss = s.split(" ");
						String[] tmp = null;
						// System.out.println("1111111111111111111");
						for (int iii = 0; iii < ss.length; iii++) {
							tmp = ss[iii].split("\\?");
							System.out.println(ss[iii]);
							byte[] asBytes = Base64.getDecoder().decode(tmp[tmp.length - 2]);
							orgFileName += new String(asBytes, "utf-8");
							// System.out.println("111111151515111111111111");
						}
						if (tmp[2].equals("B")) {
							// System.out.println("@@@@@"+orgFileName);
							// byte[] asBytes =
							// Base64.getDecoder().decode(orgFileName);
							// System.out.println("111232fgegrw");
							// orgFileName = new String(asBytes, "utf-8");
							// System.out.println(orgFileName);

						} else if (tmp[2].equals("Q")) {
							// System.out.println("111FE2FF1232fgegrw");
							InputStream stream = new ByteArrayInputStream(orgFileName.getBytes());
							// System.out.println("111232@@@fgegrw");
							InputStream ssss = MimeUtility.decode(stream, "quoted-printable");
							// System.out.println("@@@111232fgegrw");
							InputStreamReader isr = null;
							StringBuilder sb = new StringBuilder();
							isr = new InputStreamReader(ssss);
							BufferedReader br = new BufferedReader(isr);
							String content;
							while ((content = br.readLine()) != null) {
								sb.append(content);
							}
							br.close();
							orgFileName = sb.toString();
							System.out.println(orgFileName);
						} else {
							System.out.println("123");
						}
						File f = new File("/home/yrchen/edata/" + orgFileName);
						FileOutputStream fos = new FileOutputStream(f);
						byte[] buf = new byte[4096];
						int bytesRead;
						while ((bytesRead = is.read(buf)) != -1) {
							fos.write(buf, 0, bytesRead);
						}
						fos.close();
						if (f.getName().contains(".xlsx")) {
							attachments.add(f);
						}
					}
					System.out.println("==");
				} else {
					System.out.println("fuck you");
				}
			}

			folder.close(true);

			store.close();

		} catch (Exception e) {

			System.out.println(e.toString());

		}
		return attachments;

	}

	// public static void main(String[] args) {
	//
	// String senderPassword = new String("iii66072557");
	// String senderUserName = new String("moeiii.2557");
	// GmailClient newGmailClient = new GmailClient();
	// newGmailClient.setAccountDetails(senderUserName, senderPassword);
	// System.out.print(newGmailClient.readGmail());
	//
	// }

	class GMailAuthenticator extends Authenticator {
		String user;
		String pw;

		public GMailAuthenticator(String username, String password) {
			super();
			this.user = username;
			this.pw = password;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, pw);
		}
	}
}
