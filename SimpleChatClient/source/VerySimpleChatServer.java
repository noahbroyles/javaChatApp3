import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {

	static int port;

	public static void main (String[] args) {
		try {
			port = Integer.parseInt(args[0]);

		// There were no args, so ask in the console	
		} catch (Exception ex) {
			Scanner kbdReader = new Scanner(System.in);
			boolean good = false;
			while (!good) {
				System.out.println("Enter the Chat-code/port number of the server. Must be greater than 4000: ");
				port = kbdReader.nextInt();
				if (port > 4000) {
					good = true;
				}
			}
		}
        System.out.println("The Chat Server is now running! Connections will show up below.\n");
        new VerySimpleChatServer().go();  
	}
	// Main ends right here! Don't do anything bro! Hands up!

	ArrayList<PrintWriter> clientOutputStreams;
	ArrayList<Socket> clientSocketList;
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		PrintWriter writer;
		public ClientHandler(Socket clientSocket, PrintWriter clientWriter) {
			try {
				writer = clientWriter;
				sock = clientSocket;
				clientSocketList = new ArrayList<Socket>();
				clientSocketList.add(clientSocket);
			} catch(Exception ex) {System.out.println(ex.getLocalizedMessage());}
		}
	
		public void run() {
			String message;
			try {
				for (Socket sock : clientSocketList) {
					InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(isReader);
					while((message = reader.readLine()) != null) {
						tellEveryoneElse(message, writer);
					}
				}
	
			} catch(Exception ex) {ex.printStackTrace();} // I hate try / catch!

		} // close run
	}  // close inner class

    public void go() {
        clientOutputStreams = new ArrayList<PrintWriter>();
        try {
			ServerSocket serverSock = new ServerSocket(port);
			Runtime.getRuntime().addShutdownHook(new Thread() {@Override public void run() {try {serverSock.close();} catch (Exception x) {System.out.println("It didn't happen");}}});
			while (true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				Thread t = new Thread(new ClientHandler(clientSocket, writer));
				t.start();
				// This is a new connection
				System.out.println("Got a connection from "+ clientSocket);
				tellEveryone(" SYSTEM CHAT SERVER: " + clientSocket.getInetAddress() + " connected to the chat");
			}
               
        } catch(Exception ex) {
            ex.printStackTrace();
      	}	
	}

	public void tellEveryoneElse(String message, PrintWriter whoSentIt) {
		for (PrintWriter writer : clientOutputStreams) {
			if (!whoSentIt.equals(writer)) {
				try {
					writer.println(message);
					writer.flush();
				} catch(Exception ex) {ex.printStackTrace();}
			}
		}
	}
	
    public void tellEveryone(String message) {
		for (PrintWriter writer : clientOutputStreams) {
			try {
				writer.println(message);
				writer.flush();
			} catch(Exception ex) {ex.printStackTrace();}
		}
  	}
  
} // end whole thing
