package com.barolab.util.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshDefault {

	protected JSch jsch = new JSch();
	protected Channel channel = null;
	protected Session session = null;
}
