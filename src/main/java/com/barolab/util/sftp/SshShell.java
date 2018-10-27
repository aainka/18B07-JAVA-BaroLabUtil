package com.barolab.util.sftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;

import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class SshShell extends SshDefault {

	private BufferedReader reader;
	private PrintWriter writer;
	private String prompt = new String("# ");

	public void connect(String user, String passwd, String host, int port) {
		configLog();
		log.info("connect " + host);
		try {
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passwd);
			session.connect();

			channel = session.openChannel("shell");
			((ChannelShell) channel).setPtyType("vt102");
			channel.connect(5000);
			reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			writer = new PrintWriter(channel.getOutputStream(), true);
		} catch (JSchException | IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		log.info("disconnecting...");
		channel.disconnect();
		session.disconnect();
	}

	public void doCommand(String command) throws IOException, InterruptedException {
		log.info(command);
		writer.write(command + "\n");
		writer.flush();
		String response = waitPrompt(prompt);
		log.info(   response + "\n");
	}

	public String doCommand(String command, String tprompt) throws IOException, InterruptedException {
		log.info(command);
		writer.write(command + "\n");
		writer.flush();
		String response = waitPrompt(tprompt);
		log.info(response + "\n");
		return response;
	}

	public String waitPrompt(String marker) throws IOException, InterruptedException {
		String msg = new String();
		while (true) {
			while (reader.ready()) {
				char c = (char) reader.read();
				msg += c;
			}
			if (msg.indexOf(marker) > 0) {
				break;
			}
			Thread.sleep(200);
		}
	 	log.config("m=" + msg);
		log.config("wait prompt ok ==============================");
		return msg;
	}

	public void test() {
		configLog();
		connect("root", "root123", "110.13.71.93", 22); // Raspiberry
		connect("root", "root123", "211.239.124.246", 19801);
		try {
			doCommand("uptime \n", "#");
			doCommand("ps -ef \n", "#");
			doCommand("ps -ef | grep java \n", "#");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		disconnect();
	}
}
