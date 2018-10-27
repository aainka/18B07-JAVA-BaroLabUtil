package com.barolab.util.sftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.util.logging.Handler;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class SshTerm {

	private Session session = null;
	private Channel channel = null;
//	private OutputStream output = null;
//	private InputStream input = null;
	private JSch jsch = new JSch();
	private byte buffer[] = new byte[8096];
	private BufferedReader reader;
	private PrintWriter writer;
	private String prompt = new String("# ");

	public void test() {
		// SshHost("211.239.124.246", 19801)
		configLog();
		// connect("root", "root123", "1.241.184.143", 22);
		connect("root", "root123", "110.13.71.93", 22); // Raspiberry
		connect("root", "root123", "211.239.124.246", 19801);
		try {
//			sendShell("cd AAA/18B07-BaroLabUtil\n", "#");
			sendShell("uptime \n", "#");
			sendShell("ps -ef \n", "#");
			sendShell("ps -ef | grep java \n", "#");
//
// 			sendShell("sh ../upload.sh\n", "':");
// 			sendShell("inka4723\n", "#");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	public void configLog() {
		System.out.println("logConfig");
		Logger log0 = Logger.getLogger("Platfrom.DashConsole.*");
		Logger p = log0.getParent();
		for (Handler h : p.getHandlers()) {
			p.removeHandler(h);
		}
		p.addHandler(new com.barolab.log.ConcoleHandler());
		log0.setUseParentHandlers(false);
	}
	
	public void doCommand(String command) throws IOException, InterruptedException {
		log.info(command);
		writer.write(command+"\n");
		writer.flush();
		String response = waitPrompt(prompt);
		log.info(command + "OK\n"+response+"\n");
	}
	public void doCommand(String command, String tprompt) throws IOException, InterruptedException {
		log.info(command);
		writer.write(command+"\n");
		writer.flush();
		String response = waitPrompt(tprompt);
		log.info(command + "OK\n"+response+"\n");
	}

	public String sendShell(String cmd, String prompt) throws IOException, InterruptedException {
		log.info("### SEND : " + cmd);
		long time = System.currentTimeMillis();
		writer.write(cmd);
		writer.flush();
		String response = waitPrompt(prompt);
		log.info("#### SEND : " + cmd + "time = " + (System.currentTimeMillis() - time));
		return response;
	}


	public String waitPrompt(String marker) throws IOException, InterruptedException {
		log.config("wait prompt");
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
		log.info("m=" + msg);
		log.config("wait prompt ok ==============================");
		return msg;
	}

	public void connect(String user, String passwd, String host, int port) {
		configLog();
		log.info("connect "+host);
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passwd);
			session.connect();
			channel = session.openChannel("shell");
			((ChannelShell) channel).setPtyType("vt102");
			channel.connect(5000);
			// output = channel.getOutputStream();
			// input = channel.getInputStream();
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

	static public void main0(String[] args) {
		new SshTerm().test();
	}


}
