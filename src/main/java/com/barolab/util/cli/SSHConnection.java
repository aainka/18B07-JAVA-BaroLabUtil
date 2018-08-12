package com.kosh.ecli.ex_ecli;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;

import com.barolab.util.LogUtil;
import com.jcraft.jsch.ChannelShell;
import com.kosh.ecli.ex_ecli.SshUtils.SessionHolder;

public class SSHConnection {

	private enum TYPE {
		INTERNAL, EXTERNAL, BOTH
	};

	private int sendCount;
	private Date timeConnected;
	private Date lastSend;
	private Date timeClosed;
	private TYPE type;
	private String url;
	private OutputStream output = null;
	private InputStream input = null;

	private void test() {
		// TODO Auto-generated method stub
		open();
	}

	public void open() {
		url = "ssh://root:root123@1.241.184.143";
		timeConnected = LogUtil.getTime();
		timeClosed = timeConnected;
		type = TYPE.BOTH;
		SessionHolder<ChannelShell> session = new SessionHolder<>("shell", URI.create(url));
		System.out.println(LogUtil.dump(this));
	}

	public void close() {

	}

	public boolean isConnected() {
		return false;
	}

	public void fireDisconnected() {

	}

	public void showStatistics() {

	}

	static public void main(String arg[]) {
		new SSHConnection().test();
	}

}
