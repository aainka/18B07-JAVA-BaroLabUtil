
package com.kosh.ecli.ex_ecli;

//String remoteCommandOutput = exec("ssh://user:pass@host/work/dir/path", "ls -t | head -n1");
//String remoteShellOutput = shell("ssh://user:pass@host/work/dir/path", "ls");
//shell("ssh://user:pass@host/work/dir/path", "ls", System.out);
//shell("ssh://user:pass@host", System.in, System.out);
//sftp("file:/C:/home/file.txt", "ssh://user:pass@host/home");
//sftp("ssh://user:pass@host/home/file.txt", "file:/C:/home");


class Usage {
	
	public void test() {
	// 	SshUtils.shell("ssh://root:root123@1.241.184.143", "ls", System.out);
	 	SshUtils.shell("ssh://root:root123@1.241.184.143", System.in, System.out);
	}
	public static void main(String[] args) {
		new Usage().test();
	}
	
}

