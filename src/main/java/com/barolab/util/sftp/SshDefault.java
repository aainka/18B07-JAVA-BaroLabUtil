package com.barolab.util.sftp;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshDefault {

	protected JSch jsch = new JSch();
	protected Channel channel = null;
	protected Session session = null;
	
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
	
	public void configLog2() {
		System.out.println("logConfig");
		Logger log0 = Logger.getLogger("Platfrom.DashConsole.*");
		Logger p = log0.getParent();
		for (Handler h : p.getHandlers()) {
			p.removeHandler(h);
		}
		p.addHandler(new com.barolab.log.ConcoleHandler());
		log0.setUseParentHandlers(false);
		log0 = Logger.getLogger("com.barolab.*");
		log0.setLevel(Level.INFO);
	}
}
