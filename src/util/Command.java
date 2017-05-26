package util;

import java.io.Serializable;
import java.util.ArrayList;

import entity.MyFile;

public class Command implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1311433129494690604L;
	private String cmd;
	private Object data;
	private boolean flag;
	private ArrayList<MyFile> filelists = new ArrayList<>();
	private byte[] fcontent;

	public byte[] getFcontent() {
		return fcontent;
	}

	public void setFcontent(byte[] fcontent) {
		this.fcontent = fcontent;
	}

	public ArrayList<MyFile> getFilelists() {
		return filelists;
	}

	public void setFilelists(ArrayList<MyFile> filelists) {
		this.filelists = filelists;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
