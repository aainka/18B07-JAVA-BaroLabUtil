package com.barolab.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.barolab.util.model.BeanClass;

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

	public void sprintf(String fmt, Object... args) {
		String msg = String.format(fmt, args);
		println(msg + "\n");
	}

	File fp = new File("C:/tmp/test.html");
	FileWriter fileWriter;

	public void test() {
		HmTable table = new HmTable();
		// HtmlTR tr = new HtmlTr();
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

	Map<String, Object> symbol = new HashMap<String, Object>();

	public void setObject(String name, Object anObject) {
		symbol.put(name, anObject);
	}

	public void exec(String line) {
		if (line.indexOf("=") > 0) {
			execCopy(line);
		}
	}

	public void execCopy(String line) {
		String[] term = parseTerm(line," ");
		Object value = getValue(term[2]);
		setValue(term[0], value);
	}
	
	public Object setValue(String varName, Object value) {
		String[] term = parseTerm(varName,"[.]");
		Object target0 = symbol.get(term[0]);
		BeanClass.setValue(target0,term[1],value);
		System.out.println("Templete:: sname = " + varName+ ", value=" + value);
		return value;
	}

	public Object getValue(String varName) {
		String[] term = parseTerm(varName,"[.]");
		Object target0 = symbol.get(term[0]);
		Object value = BeanClass.getValue(target0, term[1]);
		System.out.println("Templete:: gname = " + varName+ ", value=" + value);
		return value;
	}

	private String[] parseTerm(String msg, String dli) {
		String[] term = msg.trim().split(dli);
		System.out.println("parseTerm" + msg + " count=" + term.length);
		for (int i = 0; i < term.length; i++) {
			System.out.println("  term[" + i + "]=" + term[i]);
			term[i] = term[i].trim();
		}
		return term;
	}

	public boolean bif(String line) {
		String[] term = parseTerm(line," ");
		return false;
	}

}
