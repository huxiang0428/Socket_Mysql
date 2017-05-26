package serivce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.DBUtil;

import entity.MyFile;

public class FileService {

	DBUtil db = new DBUtil();
	Connection conn = db.getConnection();

	public ArrayList<MyFile> viewFile(MyFile file) {
		ArrayList<MyFile> fileList = new ArrayList<>();
		String sql = "select fid,filename from tb_file where username= ? ";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, file.getUsername());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				fileList.add(new MyFile(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileList;
	}

	public boolean upload(MyFile file) {
		String sql = "insert into tb_file (username,filename,fcontent) values (?,?,?) ";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, file.getUsername());
			ps.setString(2, file.getFilename());
			ps.setBytes(3, file.getFcontent());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public byte[] download(MyFile file) throws Exception {
		String sql = "select fcontent,filename from tb_file where fid = ? ";
		PreparedStatement ps;
		ps = conn.prepareStatement(sql);
		ps.setInt(1, file.getFid());
		ResultSet rs = ps.executeQuery();
		InputStream is = null;
		if (rs.next()) {
			is = rs.getBinaryStream(1);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[is.available()];
			int len;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return buffer;
		}
		return null;
	}
}
