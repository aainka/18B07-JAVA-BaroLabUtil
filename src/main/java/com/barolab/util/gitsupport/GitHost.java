package com.barolab.util.gitsupport;

import java.io.File;
import java.io.IOException;

import com.barolab.util.sftp.SshFtpSync;
import com.barolab.util.sftp.SshTerm;

public class GitHost {

	SshTerm shell = new SshTerm();
	SshFtpSync ftpSync = new SshFtpSync();

	public void connect(String user, String passwd, String host, int port) {
		shell.connect(user, passwd, host, port);
		ftpSync.connect(user, passwd, host, port);
	}

	public void disconnect() {
		shell.disconnect();
		ftpSync.disconnect();
	}

	public void pull(String project, String remotePath, String comment) {
		try {
			shell.doCommand("cd " + remotePath + "/" + project);
			shell.doCommand("pwd");
			shell.doCommand("git pull");
			File localDir = new File("C:/@SWDevelopment/workspace-java/" + project);
			ftpSync.syncUpload(localDir, remotePath + "/" + project);
			shell.doCommand("git add -A");
		 	shell.doCommand("git commit -m " + "\"" + comment + "\"");
			// shell.doCommand("git push");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
