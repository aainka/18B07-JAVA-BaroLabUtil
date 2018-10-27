package com.barolab.util.sftp.term;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.barolab.util.sftp.SshShell;

import lombok.extern.java.Log;

@Log
public class DebugTerminal {

	// private static Process p;

	private static JTerminal terminal;

	private SshShell termShell = new SshShell();
	private PrintWriter writer;
	private BufferedReader reader;

	public void test() {
	terminal.setEditable(false);
		termShell.connect("root", "root123", "211.239.124.246", 19801); // fun25
		// termShell.connect("root", "root123", "110.13.71.93", 22); // raspberry
		writer = termShell.getWriter();
		reader = termShell.getReader();

		/*
		 * Receive Task
		 */
		new Thread(new Runnable() {

			String rxMessage = new String();

			public void run() {
				terminal.append(" ready run\n");

				while (true) {
					try {
						while (reader.ready()) {
							char c = (char) reader.read();
							rxMessage += c;
						}
						if (rxMessage.length() > 0) {
							terminal.setEditable(true);
							terminal.append(rxMessage);
							terminal.setEditable(false);
							rxMessage = new String();
						}
						Thread.sleep(200);
					} catch (InterruptedException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}).start();

		/*
		 * Transmit Listener
		 */
		terminal.addInputListener(new InputListener() {
			@Override
			public void processCommand(JTerminal terminal, char c) {
				System.out.println("send-1 = " + c);
				append(c);

			}

			@Override
			public void onTerminate(JTerminal terminal) {
				System.out.println(" III3 = ");
//				try {
//					if (p != null) {
//						p.destroy();
//					}
//					startShell();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		});
		writer.append("uptime \n");
		writer.flush();
//		writer.append("ps -ef \n");
//		writer.flush();
	}

	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		terminal = new JTerminal();

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(terminal);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	frame.addKeyListener(terminal.getKeyListener());
		frame.add(scrollPane);
		frame.setSize(675, 700);
		frame.setVisible(true);

		terminal.append("JTerminal Test\n");
		terminal.append("Debug and Example\n\n");
		new DebugTerminal().test();

//		startShell();

	}

	public void startShell() {
		try {
			String shell = "bash";

//			ProcessBuilder builder = new ProcessBuilder(shell);
//			builder.redirectErrorStream(true);
//			p = builder.start();

			// writer = new PrintWriter(p.getOutputStream(), true);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void append(char c) {
		terminal.setEditable(true);
		terminal.append(""+c);
		terminal.setEditable(false);
		if (c == '\n') {
			writer.print("\n");
			writer.flush();
		} else {
			writer.print(c);
		}
	}

	public void append(String command) {
		try {
			writer.println(command);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
