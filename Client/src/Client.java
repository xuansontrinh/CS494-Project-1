import java.io.*;

import Views.StartScreen;

public class Client {
    public static void main(String[] args) throws IOException {
//        String hostName = "192.168.43.119";
//        int portNumber = 3000;
//
//        try (
//            Socket echoSocket = new Socket(hostName, portNumber);
//            PrintWriter out =
//                new PrintWriter(echoSocket.getOutputStream(), true);
//            BufferedReader in =
//                new BufferedReader(
//                    new InputStreamReader(echoSocket.getInputStream()));
//            BufferedReader stdIn =
//                new BufferedReader(
//                    new InputStreamReader(System.in))
//        ) {
//            String userInput;
//            while ((userInput = stdIn.readLine()) != null) {
//                out.println(userInput);
//                System.out.println("echo: " + in.readLine());
//            }
//        } catch (UnknownHostException e) {
//            System.err.println("Don't know about host " + hostName);
//            System.exit(1);
//        } catch (IOException e) {
//            System.err.println("Couldn't get I/O for the connection to " +
//                hostName);
//            System.exit(1);
//        } 
    	StartScreen ss = new StartScreen();
    	ss.setVisible(true);
    }
}