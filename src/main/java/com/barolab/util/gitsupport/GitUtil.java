package com.barolab.util.gitsupport;

public class GitUtil {

	public void test() {
//		new SshHost ("211.239.124.246",19801,"Fun25.co.kr","/proj7/GITHUB/");
//		new SshUser ("root","root123");
		GitHost git = new GitHost();
		git.connect("root", "root123", "211.239.124.246", 19801); // fun25
		git.pull("18B07-BaroLabUtil", "/proj7/GITHUB/", "fun25");
		git.pull("18004-DashConsole", "/proj7/GITHUB/", "fun25");
		// git.clone()
		git.disconnect();
//		pull("18B07-BaroLabUtil");
//		push("C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil");
	}

	static public void main(String[] args) {
		new GitUtil().test();
	}

}
