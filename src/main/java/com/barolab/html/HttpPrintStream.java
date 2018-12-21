package com.barolab.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class HttpPrintStream extends PrintStream {
	
	public HttpPrintStream(File arg0) throws FileNotFoundException {
		super(arg0);
		printMeta();
	}
	
	public void close() {
		println("</html>");
	
	}
	
	private void printMeta() {
		println("<html>");
		println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
		println("<style>");
		println(".mytable { border-collapse:collapse; }   ");
		println(".mytable th, .mytable td { font-family:맑은 고딕; border:1px solid black; } ");
		println("</style>");
	}
	
	public void sprintf(String fmt, Object ... args) {
		String msg = String.format(fmt, args);
		println(msg+"\n");
	}

	File fp = new File("C:/tmp/test.html");
	FileWriter fileWriter;

	public void test() {
		HmTable table = new HmTable();
	//	HtmlTR tr = new HtmlTr();
		try {
			FileWriter fileWriter = new FileWriter(fp);
			fileWriter.append("<html>\n");
			fileWriter.append(String.format("<title>%s</title>\n", "hong"));
			fileWriter.append("</html>\n");
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
