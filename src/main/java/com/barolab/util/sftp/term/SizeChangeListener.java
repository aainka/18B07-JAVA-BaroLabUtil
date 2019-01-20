package com.barolab.util.sftp.term;

public abstract interface SizeChangeListener {
	
	public abstract void sizeChange(JTerminal terminal, boolean reset, int width, int height);

}
