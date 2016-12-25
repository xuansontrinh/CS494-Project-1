import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import Views.StartScreen;

public class Client {
	public static void main(String[] args) throws IOException {
		String hostName = "127.0.0.1";
		int portNumber = 9000;

		try (Socket server = new Socket(hostName, portNumber);
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				PrintWriter commandOut = new PrintWriter(server.getOutputStream(), true);) {

			StartScreen ss = new StartScreen(in, out, commandOut);
			ss.setVisible(true);

			while (true) {}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
}