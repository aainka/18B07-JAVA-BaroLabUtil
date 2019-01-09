package com.barolab;

import java.io.BufferedReader;
import java.net.*;

import java.io.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class RingClockServer {

	private String str;
	private BufferedReader file;

	public void testTCP() {
		try {
			ServerSocket serSocket = new ServerSocket(8282);

			System.out.println("socket reday");
			Socket client = serSocket.accept();
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(client.getInputStream()));

			String UserName = "";
			System.out.println("accept ok");
			while (true) {
				char[] buf = new char[1024];
				int size = r.read(buf, 0, 1024);
				String msg = new String(buf);
				System.out.println(msg);
				if (msg.equals("quit"))
					break;

				w.write(msg, 0, size);
				w.flush();

			}
			System.out.println("accept end");
			w.close();
			r.close();
			serSocket.close();
			client.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	int count = 0;
	

	public void testUdpTimeEcho() {
		int port = 8282;
		try {
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];
			String sendString = "RSP-TI:190101-073012";
			byte[] sendData = sendString.getBytes("UTF-8");

			System.out.printf("Listening on udp:%s:%d%n", InetAddress.getLocalHost().getHostAddress(), port);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			while (true) {
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				/**
				 * time
				 */
				
				Date dd = new Date(System.currentTimeMillis());
				int hour = dd.getHours();
				if ( hour > 12 ) hour -=12;
				
				sendString =String.format("RSP-TI:190101-%02d%02d12", hour,dd.getMinutes());
				
				/*
				 * send echo
				 */
				int rxport = receivePacket.getPort();
				InetAddress IPAddress = receivePacket.getAddress();
				System.out.println("RX: " + sentence + " " + rxport+" "+ IPAddress.getHostAddress()+" "+count);
				DatagramPacket sendPacket = new DatagramPacket(sendString.getBytes(), sendString.getBytes().length, IPAddress,
						receivePacket.getPort());
				System.out.println("TX: " + sendString);
				serverSocket.send(sendPacket);
				count++;
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		// should close serverSocket in finally block

	}

	public void testUDP() {
		String ip = "192.168.25.50";
		int port = 8282;
		String str = "TIME:1122:" + "a-";
		try {
			InetAddress ia = InetAddress.getByName(ip);
			DatagramSocket ds = new DatagramSocket(port);
//	            System.out.print("message : ");
//	            file = new BufferedReader(new InputStreamReader(System.in)); 
//	            str = file.readLine();   

			/**
			 * send
			 */
			for (int i = 0; i < 1; i++) {
				String txStr = str + i;
				System.out.println(txStr);
				byte buffer[] = txStr.getBytes();
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ia, port);
				ds.send(dp);
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			/**
			 * Receive
			 */
			{
				byte[] buffer = new byte[512];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				System.out.println("server ip : " + dp.getAddress() + " , server port : " + dp.getPort());
				System.out.println("수신된 데이터 : " + new String(dp.getData()).trim());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	};

	public static void main(String[] args) {
	 	new RingClockServer().testUdpTimeEcho();
		// new RingClockServer().testUDP();
	}

}
