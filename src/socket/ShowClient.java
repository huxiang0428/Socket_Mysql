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
		System.out.println("1����¼");
		System.out.println("2��ע��");
		System.out.println("3���ϴ�");
		System.out.println("4���鿴");
		System.out.println("5������");
		System.out.println("******��ѡ��******");
		int i = 0;
		// ѭ��ѡ����ȷ��������
		while (true) {
			try {
				i = scanner.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("������������������");
				// Ӧ�ó������ֻ��ڻ���������ʱ��һ��String����newһ���µĶ���������޳���
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
				System.out.println("���ȵ�¼����ʹ���ϴ�����");
				showMain();
			}
			break;
		case 4:
			if (isLogin) {
				showFile(false);
			} else {
				System.out.println("���ȵ�¼����ʹ���ϴ�����");
				showMain();
			}
			break;
		case 5:
			if (isLogin) {
				downLoadFile();
			} else {
				System.out.println("���ȵ�¼����ʹ���ϴ�����");
				showMain();
			}
			break;
		default:
			System.out.println("���������˳�ϵͳ");
			System.exit(0);
			break;
		}
	}

	public void showLogin() {
		int count = 0;
		while (true) {
			System.out.print("�������û�����");
			String str = scanner.next();
			if (str.toLowerCase().equals("e")
					|| str.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setUsername(str);
			System.out.print("���������룺");
			str = scanner.next();
			if (str.toLowerCase().equals("e")
					|| str.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setPassword(str);
			count++;
			if (count == 3) {
				System.out.println("������󳬹�3�Σ��˳�ϵͳ");
				System.exit(0);
			}
			command.setCmd("login");
			command.setData(user);
			sendData(command);
			command = getDate();
			if (command.isFlag()) {
				System.out.println("��¼�ɹ�");
				isLogin = true;
				showMain();
				break;
			} else {
				System.out.println("�˺������������������ ������e����������");
				continue;
			}
		}
	}

	public void showRegister() {
		while (true) {
			System.out.print("�������û�����");
			String str = scanner.next();
			if (str.toLowerCase().equals("e")
					|| str.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setUsername(str);
			System.out.print("���������룺");
			String psw = scanner.next();
			if (psw.toLowerCase().equals("e")
					|| psw.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			user.setPassword(psw);
			System.out.print("�����������룺");
			String cpsw = scanner.next();
			if (cpsw.toLowerCase().equals("e")
					|| cpsw.toLowerCase().substring(0, 1).equals("e")) {
				showMain();
				break;
			}
			if (!user.getPassword().equals(cpsw)) {
				System.out.println("�����������벻һ��");
				showRegister();
				break;
			} else {
				command.setCmd("register");
				command.setData(user);
				sendData(command);
				command = getDate();
				if (command.isFlag()) {
					System.out.println("ע��ɹ������¼");
					showMain();
					break;
				} else {
					System.out.println("�˺��ظ�,����ע��");
					showRegister();
					break;
				}
			}
		}
	}

	public void showUpload() {
		System.out.println("******�ļ��ϴ�����*******");
		while (true) {
			System.out.print("������Ҫ�ϴ����ļ�����(D:/a/b.jpg)��");
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
				System.out.println("�ϴ��ɹ�");
				showMain();
				break;
			} else {
				System.out.println("�ϴ�ʧ�ܣ����������");
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
			System.out.println("���" + "\t" + "�ļ���");
			for (MyFile file : command.getFilelists()) {
				// sysoĬ�ϵ���tostring����
				System.out.println(file);
			}
			if (!isDownLoad) {
				showMain();
			}
		} else {
			System.out.println("���û�δ�ϴ��ļ��������ϴ�");
			showMain();
		}
	}

	public void downLoadFile() {
		int fid;
		showFile(true);
		System.out.println("ѡ��Ҫ���ص��ļ����:");
		while (true) {
			try {
				fid = scanner.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("������������������");
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
			System.out.println("������Ҫ�����λ����(D:/a/b.jpg):");
			String filepath = scanner.next();
			if (command.isFlag()) {
				byte[] bytes = command.getFcontent();
				if (filepath != null) {
					// ����·��Ŀ¼
					String path = filepath.substring(0,
							filepath.lastIndexOf("/") + 1);
					if (path.equals("")) {
						System.out.println("λ�û����ʹ�������ȷ����");
						continue;
					}
					File files = new File(path);
					if (!files.exists()) {
						files.mkdirs();
					}
					try {
						FileOutputStream out = new FileOutputStream(filepath);
						out.write(bytes);
						System.out.println("���سɹ�������ҳ��");
						showMain();
						break;
					} catch (FileNotFoundException e) {
						System.out.println("λ�û����ʹ�������ȷ����");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("λ������");
					continue;
				}
			} else {
				System.out.println("����ʧ�ܣ�û�д��ļ�");
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
