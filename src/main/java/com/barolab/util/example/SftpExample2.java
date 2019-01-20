package com.barolab.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
https://blog.naver.com/drm1801/220711599616
public class SftpExample2 {
	private String host;
	private Integer port;
	private String user;
	private String password;
	
	private JSch jsch;
	private Session session;
	private Channel channel;
	private ChannelSftp sftpChannel;
	
	public  SftpExample2( String host, Integer port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	public void connect() {
		System.out.println("connecting..."+ host);
		 
			try {
				jsch = new JSch();
				session = jsch.getSession(user, host, port);
				session.setConfig("StricHostKeyChecking","no");
				session.setPassword(password);
				session.connect();
				
				channel = session.openChannel("sftp");
				channel.connect();
				
				sftpChannel = (ChannelSftp) channel;
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void disconnect() {
		System.out.println("disconnecting..."+ host);
		sftpChannel.disconnect();
		channel.disconnect();
		session.disconnect();
	}
	
	public void upload (String fileName, String remoteDir) {
		FileInputStream fis = null;
		connect();
		File file;
		try {
			sftpChannel.cd(remoteDir);
			file = new File(fileName);
			fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());
			fis.close();
			System.out.println("File uploaded successfully - "+file.getAbsolutePath());
		} catch (SftpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		disconnect();
	}
	
	public void download ( String fileName, String localDir) {
		byte[] buffer = new byte[1024];
		Buffered
	}
	
	
	
	
	
	

1.	특정 directory의 모든 sub directory및 file 삭제
-	Path만 설정하면 모든 내용 삭제
-	Path 설정 방법 (Path path = Paths.get(“/home/ngepc/Event/cfr”))
       private void deleteDirectoryStream(Path path) throws IOException {
               Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                      @Override
                      public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                             Files.delete(file); // this will work because it's always a File
                             return FileVisitResult.CONTINUE;
                      }

                      @Override
                      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                             Files.delete(dir); //this will work because Files in the directory are already deleted
                             return FileVisitResult.CONTINUE;
                      }
               });
}

2.	Pattern Matcher
-	Regular expression에 대해서 잘알면 Pattern & Matcher가 유용하네요

               String testString = "cpu-utilization: Peak 4%, Average 2%";
               Pattern p = Pattern.compile("\\d+%");
               Matcher matcher = p.matcher(testString);
               
               while(matcher.find()){
                      System.out.println(matcher.group().split("%")[0]);
               }
               
               testString = "/home/ngepc/Event/cfr/20180807/CallFail.0935_10.180.86.20_vEPG02_vSGW_.csv";
               p = Pattern.compile("\\d+/");
               matcher = p.matcher(testString);
               
               while(matcher.find()){
                      System.out.println(matcher.group().split("/")[0]);
        }
3.	날짜 관련 함수
-	특정 날짜 관련된 string을 LocalDate or LocalDateTime으로 변경하게 되면
년/월/일/시/분/초를 쉽게 구할수 있습니다.
// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = "20160816";
        LocalDate currentDate = LocalDate.parse(date, formatter);
System.out.println(String.format("%04d%02d%02d",
currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth()

	
	
Lombok Library라고 Getter, Setter, toString, hashCode, equals등을 자동생성해주는 Library 입니다.
Json Interface나 코드량, 멤버 변수 추가/삭제될 경우 다시 생성하는 불편함을 해결해줄것 같아 공지 합니다.

참조하실 Site는 http://offbyone.tistory.com/113 나 Google에서 lombok으로 보시면 됩니다.
첨부 File은 기존 source와 lombak을 사용했을때의 소스 입니다.

그럼, 수고하세요

	
	
	
	
	
	
	

}
