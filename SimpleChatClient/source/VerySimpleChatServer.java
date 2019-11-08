import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {
      ArrayList clientOutputStreams;
      
      public class ClientHandler implements Runnable {
       BufferedReader reader;
       Socket sock;
      
      public ClientHandler(Socket clientSocket) {
      try {
             sock = clientSocket;
            InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
          reader = new BufferedReader(isReader);

} catch(Exception ex) {ex.printStackTrace();}
    }
    
        public void run() {
              String message;
               try {
             while((message = reader.readLine()) != null) {
           tellEveryone(message);
       }
    } catch(Exception ex) {ex.printStackTrace();}
  
} // close run
}  // close inner class

      public static void main (String[] args) {
        System.out.println("The Chat Server is now running! Connections will show up below.\n");
        new VerySimpleChatServer().go();  
    }
    public void go() {
        clientOutputStreams = new ArrayList();
        try {
                ServerSocket serverSock = new ServerSocket(443);
                while (true) {
                        Socket clientSocket = serverSock.accept();
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                        clientOutputStreams.add(writer);
                        Thread t = new Thread(new ClientHandler(clientSocket));
                        t.start();
                        // This is a new connection
                        System.out.println("Got a connection from "+ clientSocket);
                        tellEveryone(" SYSTEM CHAT SERVER: "+ clientSocket.getInetAddress() + " connected to the chat.");
                }
               
        } catch(Exception ex) {
               ex.printStackTrace();
      }
     }
    public void tellEveryone(String message) {
          Iterator it = clientOutputStreams.iterator();
           while(it.hasNext()) {
                 try {
               PrintWriter writer = (PrintWriter) it.next();
              writer.println(message);
              writer.flush();
     } catch(Exception ex) {ex.printStackTrace();}
}

  }
  
} // end whole thing