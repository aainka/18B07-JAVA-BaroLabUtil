package com.barolab.sync;

import java.util.Date;

import lombok.Data;

@Data
public class OV_FileInfo {
	private String name;
	private Date updated;
	private Date created;
	private boolean is_dir;
	private String text_content;
}
