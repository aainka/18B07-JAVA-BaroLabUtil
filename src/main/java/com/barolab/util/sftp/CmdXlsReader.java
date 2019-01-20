package com.barolab.util.sftp;

import java.util.List;
import java.util.logging.Level;



import com.barolab.log.LogConfig;
import com.barolab.util.ExcelObjectReader;

import lombok.extern.java.Log;

@Log
public class CmdXlsReader {

	public void test2() {
		LogConfig.setLevel(this, Level.FINE);
		log.info("reader");
		List<OV_Cmd> nlist = (List<OV_Cmd>) new ExcelObjectReader().read(OV_Cmd.class, "test",
				"C:\\VDI_SHARE\\cmd.xlsx");
		for ( OV_Cmd cmd : nlist) {
			log.info(cmd.toString());
		}
//		List<OV_Cmd> nlist = (List<OV_Cmd>) new ExcelObjectWriter().read(OV_Cmd.class, null,
//				"c:/VDI_SHARE/cmd.xlxs");
	}

	public static void main(String[] args) {
		new CmdXlsReader().test2();

	}

}
