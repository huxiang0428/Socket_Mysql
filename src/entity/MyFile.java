package entity;

import java.io.Serializable;

public class MyFile implements Serializable {
	@Override
	public String toString() {
		return fid+"\t"+filename;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8486286754678320598L;
	private int fid;
	private String filename;
	private byte[] fcontent;
	private String username;
	public MyFile(){
		
	}
	public MyFile(int id, String fname) {
		fid = id;
		filename = fname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getFcontent() {
		return fcontent;
	}

	public void setFcontent(byte[] fcontent) {
		this.fcontent = fcontent;
	}

}
