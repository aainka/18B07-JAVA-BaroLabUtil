package com.barolab.util.sftp;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class TermView {

	JFrame frame = new JFrame("test");
	JTextPane console = new JTextPane();
	SshShell termShell = new SshShell();

	public void test() {
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.getContentPane().add(new JScrollPane(console));

		termShell.connect("root", "root123", "211.239.124.246", 19801);
		try {
//			doCommand("cd AAA/18B07-BaroLabUtil\n", "#");
			String response = termShell.doCommand("uptime \n", "#");

			console.setText(response);
			response += termShell.doCommand("ps -ef \n", "#");
			console.setText(response);
			response += termShell.doCommand("date \n", "#");
			console.setText(response);
//			doCommand("ps -ef \n", "#");
// 	 		doCommand("ps -ef | grep java \n", "#");
//
// 			doCommand("sh ../upload.sh\n", "':");
// 			doCommand("inka4723\n", "#");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		termShell.disconnect();
	}

	static public void main(String[] args) {
		new TermView().test();
	}

}
