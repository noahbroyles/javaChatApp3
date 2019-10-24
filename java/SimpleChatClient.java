import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.midi.*;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 


public class SimpleChatClient {

    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    String userName;

    public static void main(String[] args) {
        SimpleChatClient client = new SimpleChatClient();
        String ip = args[0];
        client.userName = args[1];
        client.go(ip);
        
    }

    
    public void playSoundAlert() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("alert.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            // ex.printStackTrace();
        }
    }
    



    public void go(String ip) {
        JFrame frame = new JFrame("Ludicrously Simple Chat App");
        JPanel mainpanel = new JPanel();
        incoming = new JTextArea(30, 40);
        incoming.setBackground(Color.lightGray);
        incoming.setLineWrap(true);
        incoming.setEditable(false);
        Font inFont = new Font("sans-serif", 20, 15);
        incoming.setFont(inFont);
        JScrollPane qscroller = new JScrollPane(incoming);
        new SmartScroller(qscroller);
        qscroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qscroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(45);
        outgoing.addKeyListener(new SimpleChatClient.EnterKeyListener());
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainpanel.setBackground(Color.darkGray);
        mainpanel.add(qscroller);
        mainpanel.add(outgoing);
        mainpanel.add(sendButton);
        setUpNetworking(ip);

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainpanel);
        frame.setSize(750,660);
        frame.setVisible(true);
    } // close the go methodical method

    public void sendMessage() {
        try {
            if (outgoing.getText().length() != 0) {
                writer.println(" " + userName + ":     " + outgoing.getText());
                writer.flush();
            } 
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        outgoing.setText("");
        outgoing.requestFocus();
    }

    private void setUpNetworking(String ip) {
        
        try {
            sock = new Socket(ip, 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("Networking established.");
            System.out.println("Have fun Chatting!");
        
        } catch (IOException ex) {
            System.out.println("The chat application could not connect to the chat server. Please try again with a different Internet Address. ");
            System.exit(1);
        }
    } // end setUpNetworking

    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            sendMessage();
        }
    } // close SendButtonListener

    public class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                
                while ((message = reader.readLine()) != null) {
                    incoming.append(message + "\n\n");
                    playSoundAlert();
                }
                    
            } catch(Exception ex) {ex.printStackTrace();}
        }
    }

    private class EnterKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                sendMessage();
            }
        }

        public void keyReleased(KeyEvent e) {}
    }

} //close whole thing


