package com.barolab.util.sftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Handler;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.extern.java.Log;

@Log
public class SshTerm {

	private Session session = null;
	private Channel channel = null;
	private OutputStream output = null;
	private InputStream input = null;
	private JSch jsch = new JSch();
	private byte buffer[] = new byte[8096];

	public void test() {
		configLog();
		connect("root", "root123", "1.241.184.143", 22);
		try {
			sendShell("cd AAA/18B07-BaroLabUtil\n", "#");
			sendShell("ps -ef | grep java \n", "#");

//			sendShell("sh ../upload.sh\n", "':");
//			sendShell("inka4723\n", "#");
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

	public void sendShell(String cmd, String prompt) throws IOException, InterruptedException {
		log.info("### SEND : " + cmd);
		long time = System.currentTimeMillis();
		output.write(cmd.getBytes());
		output.flush();
		waitPrompt(prompt);
		log.info("#### SEND : " + cmd + "time = " + (System.currentTimeMillis() - time));
	}

	public void waitPrompt(String marker) throws IOException, InterruptedException {
		log.config("wait prompt");
		String msg = new String();
		while (true) {
			while (input.available() > 0) {
				int size = input.read(buffer);
				// System.out.println("rx.size=" + size);
				String response = new String(buffer, 0, size);
				System.out.print(response);
				msg += response;
			}
			if (msg.indexOf(marker) > 0) {
				break;
			}
			Thread.sleep(200);
		}
		log.config("m=" + msg);
		log.config("wait prompt ok ==============================");
	}

	public void connect(String user, String passwd, String host, int port) {
		System.out.println("connecting..." + host);
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passwd);
			session.connect();
			channel = session.openChannel("shell");
			((ChannelShell) channel).setPtyType("vt102");
			channel.connect(5000);
			output = channel.getOutputStream();
			input = channel.getInputStream();
		} catch (JSchException | IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		log.info("disconnecting...");
		channel.disconnect();
		session.disconnect();
	}

	static public void main(String[] args) {
		new SshTerm().test();
	}
}
