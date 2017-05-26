package socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import serivce.FileService;
import util.Command;

import entity.MyFile;
import entity.User;

public class ShowClient {
	Scanner scanner = new Scanner(System.in);
	Command command = new Command();
	private boolean isLogin;
	User user = new User();
	MyFile file = new MyFile();

	public void showMain() {
		System.out.println("1，登录");
		System.out.println("2，注册");
		System.out.println("3，上传");
		System.out.println("4，查看");
		System.out.println("5，下载");
		System.out.println("******请选择******");
		int i = 0;
		// 循环选择，正确再往下走
		while (true) {
			try {
				i = scanner.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("请重新输入整数类型");
				// 应该出错后错字还在缓冲区，此时读一个String或者new一个新的对象避免无限出错
				// scanner.next();
				scanner = new Scanner(System.in);
				continue;
			}
		}
		switch (i) {
		case 1:
			showLogin();
			break;
		case 2:
			showRegister();
			break;
		case 3:
			if (isLogin) {
				showUpload();
			} else {
				System.out.println("请先登录，再使用上传功能");
				showMain();
			}
			break;
		case 4:
			if (isLogin) {
				showFile(false);
			} else {
				System.out.println("请先登录，再使用上传功能");
				showMain();
			}
			break;
		case 5:
			if (isLogin) {
				downLoadFile();
			} else {
				System.out.println("请先登录，再使用上传功能");
				showMain();
			}
			break;
		default:
			System.out.println("输入有误，退出系统");
			System.exit(0);
			break;
		}
	}

	public void showLogin() {
		int count = 0;
		while (true) {
			System.out.print("请输入用户名：");
			String str = scanner.next();
			if (str.toLowerCase().equals("e")
					|| str.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setUsername(str);
			System.out.print("请输入密码：");
			str = scanner.next();
			if (str.toLowerCase().equals("e")
					|| str.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setPassword(str);
			count++;
			if (count == 3) {
				System.out.println("密码错误超过3次，退出系统");
				System.exit(0);
			}
			command.setCmd("login");
			command.setData(user);
			sendData(command);
			command = getDate();
			if (command.isFlag()) {
				System.out.println("登录成功");
				isLogin = true;
				showMain();
				break;
			} else {
				System.out.println("账号密码出错，请重新输入 ，输入e返回主界面");
				continue;
			}
		}
	}

	public void showRegister() {
		while (true) {
			System.out.print("请输入用户名：");
			String str = scanner.next();
			if (str.toLowerCase().equals("e")
					|| str.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setUsername(str);
			System.out.print("请输入密码：");
			String psw = scanner.next();
			if (psw.toLowerCase().equals("e")
					|| psw.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setPassword(psw);
			System.out.print("请再输入密码：");
			String cpsw = scanner.next();
			if (cpsw.toLowerCase().equals("e")
					|| cpsw.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			if (!user.getPassword().equals(cpsw)) {
				System.out.println("两次输入密码不一致");
				showRegister();
				break;
			} else {
				command.setCmd("register");
				command.setData(user);
				sendData(command);
				command = getDate();
				if (command.isFlag()) {
					System.out.println("注册成功，请登录");
					showMain();
					break;
				} else {
					System.out.println("账号重复,重新注册");
					showRegister();
					break;
				}
			}
		}
	}

	public void showUpload() {
		System.out.println("******文件上传界面*******");
		while (true) {
			System.out.print("请输入要上传的文件名如(D:/a/b.jpg)：");
			String path = scanner.next();
			if (path.toLowerCase().equals("e")
					|| path.toLowerCase().substring(0, 2).equals("ex")) {
				showMain();
				break;
			}
			file.setUsername(user.getUsername());
			file.setFilename(path.substring(path.lastIndexOf('/') + 1));
			try {
				FileInputStream fis = new FileInputStream(path);
				byte[] fileBytes = new byte[fis.available()];
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(fileBytes);
				file.setFcontent(fileBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			command.setCmd("upload");
			command.setData(file);
			sendData(command);
			command = getDate();
			if (command.isFlag()) {
				System.out.println("上传成功");
				showMain();
				break;
			} else {
				System.out.println("上传失败，请继续操作");
			}
		}
	}

	public void showFile(boolean isDownLoad) {
		file.setUsername(user.getUsername());
		command.setCmd("view");
		command.setData(file);
		sendData(command);
		command = getDate();
		if (command.isFlag()) {
			System.out.println("编号" + "\t" + "文件名");
			for (MyFile file : command.getFilelists()) {
				// syso默认调用tostring方法
				System.out.println(file);
			}
			if (!isDownLoad) {
				showMain();
			}
		} else {
			System.out.println("该用户未上传文件，请先上传");
			showMain();
		}
	}

	public void downLoadFile() {
		int fid;
		showFile(true);
		System.out.println("选择要下载的文件编号:");
		while (true) {
			try {
				fid = scanner.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("请重新输入整数类型");
				scanner = new Scanner(System.in);
				continue;
			}
		}
		file.setFid(fid);
		while (true) {
			command.setCmd("download");
			command.setData(file);
			sendData(command);
			command = getDate();
			System.out.println("输入需要保存的位置如(D:/a/b.jpg):");
			String filepath = scanner.next();
			if (command.isFlag()) {
				byte[] bytes = command.getFcontent();
				if (filepath != null) {
					// 创建路径目录
					String path = filepath.substring(0,
							filepath.lastIndexOf("/") + 1);
					if (path.equals("")) {
						System.out.println("位置或类型错误，请正确输入");
						continue;
					}
					File files = new File(path);
					if (!files.exists()) {
						files.mkdirs();
					}
					try {
						FileOutputStream out = new FileOutputStream(filepath);
						out.write(bytes);
						System.out.println("下载成功返回主页面");
						showMain();
						break;
					} catch (FileNotFoundException e) {
						System.out.println("位置或类型错误，请正确输入");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("位置有误");
					continue;
				}
			} else {
				System.out.println("下载失败，没有此文件");
			}
		}
	}

	Socket socket = null;

	public void sendData(Command cmd) {
		ObjectOutputStream oos = null;
		try {
			socket = new Socket("127.0.0.1", 8888);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(cmd);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Command getDate() {
		ObjectInputStream ois = null;
		Command cmd = null;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			cmd = (Command) ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return cmd;
	}
}
