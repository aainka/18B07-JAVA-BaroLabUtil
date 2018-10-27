package com.barolab.util.sftp;

import lombok.Data;

@Data
public class OV_Cmd {
	private String Name;
	private String PreFDN;
	private int timeExecution;
	private String result;
}
