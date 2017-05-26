package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.MyFile;
import entity.User;

import serivce.FileService;
import serivce.UserService;
import util.Command;

public class ServerThread implements Runnable {

	private Socket socket = null;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			Command command = (Command) ois.readObject();
			// 得到结果后发回给client
			command = doCommand(command);
			oos.writeObject(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Command doCommand(Command command) throws SQLException {
		UserService us = new UserService();
		FileService fs = new FileService();
		boolean flag = false;
		switch (command.getCmd()) {
		case "login":
			flag = us.login((User) command.getData());
			break;
		case "register":
			flag = us.register((User) command.getData());
			break;
		case "upload":
			flag = fs.upload((MyFile) command.getData());
			break;
		case "view":
			ArrayList<MyFile> filelist = fs.viewFile((MyFile) command.getData());
			if (filelist != null) {
				flag = true;
				command.setFilelists(filelist);
			}
			break;
		case "download":
			try {
				byte[] fcontent = fs.download((MyFile) command.getData());
				if (fcontent != null) {
					flag = true;
					command.setFcontent(fcontent);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		command.setFlag(flag);
		return command;
	}
}
