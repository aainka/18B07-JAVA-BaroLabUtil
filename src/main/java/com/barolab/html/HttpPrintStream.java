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
	
	private void printMeta() {
		println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
		println("<style>");
		println(".mytable { border-collapse:collapse; }   ");
		println(".mytable th, .mytable td { font-family:맑은 고딕; border:1px solid black; } ");
		println("</style>");
	}

	File fp = new File("C:/tmp/test.html");
	FileWriter fileWriter;

	public void test() {
		HtmlTable table = new HtmlTable();
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

	public static void main(String arg[]) {

	}

}
