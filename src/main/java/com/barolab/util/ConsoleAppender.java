package com.barolab.util;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class ConsoleAppender extends AppenderSkeleton {

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void append(LoggingEvent arg0) {
		
		System.out.println(arg0.getLevel()+":"+arg0.getMessage());
		
	}

}
