package com.barolab.log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ConcoleHandler extends Handler {

	@Override
	public void close() throws SecurityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public void publish(LogRecord arg0) {
		String name = arg0.getSourceClassName();
		name = name.substring(name.lastIndexOf(".")+1, name.length());
		System.out.println("["+name+"] "+arg0.getLevel()+":: "+arg0.getMessage());

	}

}
