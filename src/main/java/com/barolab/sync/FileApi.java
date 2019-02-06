package com.barolab.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileApi {

	String filename;

	public void readBinary() throws IOException {
		byte[] b = new byte[1024];
		int len; // 실제로 읽어온 길이 (바이트 개수)
		int counter = 0;

		DataInputStream in = new DataInputStream(new FileInputStream(filename));

		while ((len = in.read(b)) > 0) {
			for (int i = 0; i < len; i++) { // byte[] 버퍼 내용 출력
				System.out.format("%02X ", b[i]);
				counter++;
			}
		}

		System.out.format("%n%n%n[%d 바이트를 읽어서 출력]", counter);
		in.close();

		System.out.println(); // 줄 바꾸고 종료
	}

	public void wrtieBinary() throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));

		// byte 배열이 아닌 정수 배열이어야, byte 데이터가 제대로 입력됨
		int[] b = { 0x01, 0x02, 0x03, 0x04, 0x0D, 0x0A, 0x00, 0xFF };

		// 위의 int배열의 요소를 하나씩 꺼내어,
		// 바이트 형식으로 파일에 저장
		for (int i = 0; i < b.length; i++)
			out.write(b[i]);

		out.close(); // 파일 닫기
	}

	public void compare(byte[] buf) {

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
