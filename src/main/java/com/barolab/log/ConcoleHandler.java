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
		String sLevel = arg0.getLevel()+"           ";
		sLevel = sLevel.substring(0, 7);
		String name = arg0.getSourceClassName();
		name = name.substring(name.lastIndexOf(".")+1, name.length());
		System.out.println(sLevel+":"  +name+"."+arg0.getSourceMethodName()+"():"+":: "+arg0.getMessage());

	}

}
