package com.barolab.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.barolab.util.TableMap;
import com.barolab.util.model.BeanClass;
import com.barolab.util.model.ListUtils;

public class HttpBuilder {

	StringBuilder stringBuilder = new StringBuilder();

	public HttpBuilder() throws FileNotFoundException {
		printMeta();
	}

	public String toString() {
		return stringBuilder.toString();
	}

	public void writeToFile(String filename) throws IOException {

		File file = new File(filename);
		FileWriter writer = null;
		writer = new FileWriter(file, false); // false, always new file
		writer.write(toString());
		writer.flush();
		writer.close();

	}

	public void println(Object anObject) {
		stringBuilder.append(anObject.toString() + "\n");
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

	public void printTable(TableMap table, ListUtils listUtils, String opname, int width) {
		println("TablePrint");
		HmTable htable = new HmTable();
		/*
		 * print header
		 */
		HmTR tr = htable.addTR();
		tr.addTD().add("");
		for (String colKey : table.getColumnKeys()) {
			if (width > 0) {
				tr.addTD().setAligh("CENTER").setWidth(width).add(colKey);
			} else {
				tr.addTD().add(colKey);
			}
		}
		for (String rowKey : table.getRowKeys()) {
			tr = htable.addTR();
			tr.addTD().add(rowKey);
			for (String colKey : table.getColumnKeys()) {
				String s = listUtils.pivot(opname, table.get(rowKey, colKey));
				tr.addTD().setAligh("CENTER").add(s);
			}
		}
		htable.toHTML(this);
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

	// **************************************************************************
	// ** Plain File Reader
	// **************************************************************************

	public String readFile(String filename) throws FileNotFoundException {
		BufferedReader br;
		String msg = null;
		br = new BufferedReader(new FileReader(filename));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			msg = sb.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(msg);
		return msg;
	}

	// **************************************************************************
	// ** Interpretor
	// **************************************************************************

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
		String[] term = parseTerm(line, " ");
		Object value = getValue(term[2]);
		// String dmsg = String.format("Compile %s=%s --> %s ", term[2], value,
		// term[0]);
		// System.out.println(dmsg);
		setValue(term[0], value);
	}

	public void setValue(String varName, Object value) {
		String[] term = parseTerm(varName, "[.]");
		Object target = symbol.get(term[0]);
		// String dmsg = String.format("Compile setValue %s %s ", varName, value);
		// System.out.println(dmsg);
		BeanClass.setValue(target, term[1], value);
	}

	public Object getValue(String varName) {
		String[] term = parseTerm(varName, "[.]");
		Object target0 = symbol.get(term[0]);
		if (target0 == null) {
			System.out.println("Can't find varName=" + varName);
		}
		Object value = BeanClass.getValue(target0, term[1]);
		return value;
	}

	private String[] parseTerm(String msg, String dli) {
		String[] term = msg.trim().split(dli);
		for (int i = 0; i < term.length; i++) {
			// System.out.println(" term[" + i + "]=" + term[i]);
			term[i] = term[i].trim();
		}
		return term;
	}

	public boolean bif99(String line) {
		String[] term = parseTerm(line, " ");
		return false;
	}

	public boolean isValue99(String string, String string2) {
		// TODO Auto-generated method stub
		return false;
	}

}
