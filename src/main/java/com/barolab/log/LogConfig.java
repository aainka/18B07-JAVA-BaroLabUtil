package com.barolab.log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.barolab.util.sftp.CmdXlsReader;

public class LogConfig {

	public static void setLevel(Object anObject, Level level) {
		System.out.println("<<< logConfig.setLevel >>>");
		Logger log0 = Logger.getLogger(anObject.getClass().getName());
		Logger p = log0.getParent();
		for (Handler h : p.getHandlers()) {
			p.removeHandler(h);
		}
		p.addHandler(new com.barolab.log.ConcoleHandler());
	//	log0.setUseParentHandlers(false);
		log0.setLevel(level);
		
	}
	
	public void setLogLevel(Level newLevel, String scope) {
		Logger logger = Logger.getLogger(scope);
		logger.setLevel(newLevel);

	}

}
