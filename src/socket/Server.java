package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		ServerSocket ss = null;
		Socket socket = null;
		try {
			ss = new ServerSocket(8888);
			while (true) {
				socket = ss.accept();
				ServerThread st = new ServerThread(socket);
				new Thread(st).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
