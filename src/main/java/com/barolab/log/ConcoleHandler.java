package com.barolab.log;

import java.util.logging.Handler;
import java.util.logging.Level;
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
		String sLevel = arg0.getLevel() + "           ";
		sLevel = sLevel.substring(0, 7);
		String name = arg0.getSourceClassName();
		name = name.substring(name.lastIndexOf(".") + 1, name.length());
		 
		String sMeter;
		switch (arg0.getLevel().intValue()) {
		case 900: // warnning
			sMeter = "[****E]";
			break;
		case 800: // config
			sMeter = "[*****]";
			break;
		case 700: // info
			sMeter = "  ***";
			break;
		case 500: // fine
			sMeter = "   **";
			break;
		case 400: // finer
			sMeter = "    *";
			break;
		default:
			sMeter = "-----"+arg0.getLevel().intValue();
		}
		System.out.print(sMeter + ":" + name + "." + arg0.getSourceMethodName() + "():"    );
		if (arg0.getMessage().indexOf("\n") >=0) {
			String[] lines = arg0.getMessage().split("\n");
			for (String line : lines) {
				if (line.indexOf("\r\n") >= 0) {
					System.out.println("UUUUUUUUUUUUUUUUUU");
				}
				line = line.replaceAll("\r", "");
				System.out.print("\n      | " + line);
			}
			System.out.println("\n");
		} else {
		System.out.println(  ":: " + arg0.getMessage());
		}
	}

}
