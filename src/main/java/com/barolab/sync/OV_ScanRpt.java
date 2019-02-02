package com.barolab.sync;

import java.util.List;

import lombok.Data;
import lombok.extern.java.Log;

@Data
public class OV_ScanRpt {
	private String op;
	private OV_FileInfo src;
	private OV_FileInfo dst;
	 
	public static void create(String string, List<OV_ScanRpt> scanList, OV_FileInfo src2, OV_FileInfo dst2) {
		OV_ScanRpt np = new OV_ScanRpt();
		np.op = string;
		np.src = src2;
		np.dst = dst2;
		scanList.add(np);
		
	}
}
