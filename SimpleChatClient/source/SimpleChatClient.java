// Imports arranged beautifully in order from least to greatest 

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Toolkit;
import javax.sound.sampled.Clip; 
import java.awt.datatransfer.Clipboard;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.awt.datatransfer.StringSelection;



public class SimpleChatClient {
    Boolean soundAlerts = true;
    JFrame emojiFrame;
    JTextArea incoming;
    JTextField outgoing;
    JButton soundToggle;
    JButton emojiToggle;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    String userName;
    ArrayList<String> emojis = new ArrayList<String>(Arrays.asList("(~_^)", "﴾͡๏̯͡๏﴿", "◔ ⌣ ◔", "♥‿♥","(˃̣̣̥ w ˂̣̣̥)", "(ᵔᴥᵔ)", "¯\\_(ツ)_/¯","(✖╭╮✖)", "⌐╦╦═─", "༼ つ ◕_◕ ༽つ", "˙ ͜ʟ˙", "¬_¬", "[̲̅$̲̅(̲̅5̲̅)̲̅$̲̅]", "ू(･ิ ॄ･ิू๑)", "( ︶︿︶)_╭∩╮", "✄ it"));

    public static void main(String[] args) {

        SimpleChatClient client = new SimpleChatClient();

        try {
            String ip = args[0];
            client.userName = args[1];
            client.go(ip);
        } catch(Exception ex) {
            Scanner kbdReader = new Scanner(System.in);
            System.out.println();
            System.out.println("Enter the IP of the host Server: ");
            String ip = kbdReader.next();
            System.out.println();
            System.out.println("Enter your username, no spaces allowed: ");
            Boolean you = true;
            Boolean spaces = true;
            while (spaces || you) {
                client.userName = kbdReader.next();
                if (!client.userName.toLowerCase().equals("you")) {
                    you = false;
                } else {
                    System.out.println("Sorry Brotha, your username can't be \"You\". Try again: ");
                }
                if (!client.userName.contains(" ")) {
                    spaces = false;
                } else {
                    System.out.println("Sorry Brotha, your username can't have spaces in it. Try again: ");
                }
            }
            System.out.println();
            kbdReader.close();
            client.go(ip);
        }
    }

    public void playSoundAlert() {
        try {

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream("alert.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            // ex.printStackTrace();
        }
    }

    public void playSendAlert() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream("sent.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            // ex.printStackTrace();
        }
    }
    
    public void copyToClipboard (String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public void go(String ip) {
        JFrame frame = new JFrame("Freaky Chat App | " + userName + " chattin'");
        emojiFrame = new JFrame("Copy and Paste Emojis!");
        emojiFrame.setResizable(true);
        frame.setResizable(false);

        JPanel mainpanel = new JPanel();

        incoming = new JTextArea(30, 40);
        incoming.setBackground(Color.lightGray);
        incoming.setLineWrap(true);
        incoming.setEditable(false);
        Font inFont = new Font("sans-serif", 20, 15);
        incoming.setFont(inFont);
        
        JTextArea emojiTextArea = new JTextArea(20, 30);
        emojiTextArea.setEditable(false);
        emojiTextArea.setLineWrap(false);
        emojiTextArea.setFont(inFont);
        emojiTextArea.setBackground(Color.ORANGE);
        // Time to populate Emojis from the ArrayList to the TextArea
        int increment = 0;
        for (String emoji : emojis) {
            emojiTextArea.append(" " + emoji + "      "); 
            increment += 1;
            if (increment == 4) {
                increment = 0;
                emojiTextArea.append("\n\n ");
            }
        }

        JScrollPane qscroller = new JScrollPane(incoming);
        new SmartScroller(qscroller);
        qscroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qscroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing = new JTextField(45);
        outgoing.addKeyListener(new SimpleChatClient.EnterKeyListener());


        JButton sendButton = new JButton("Send");
        sendButton.setForeground(Color.BLUE);
        emojiToggle = new JButton("Show Emojis");
        soundToggle = new JButton("Mute Sound");
        soundToggle.setForeground(Color.RED);
        soundToggle.addActionListener(new SoundButtonListener());
        sendButton.addActionListener(new SendButtonListener());
        emojiToggle.addActionListener(new EmojiButtonClicker());

        mainpanel.setBackground(Color.darkGray);
        mainpanel.add(qscroller);
        mainpanel.add(outgoing);
        mainpanel.add(sendButton);
        mainpanel.add(emojiToggle);
        mainpanel.add(soundToggle);

        setUpNetworking(ip);

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainpanel);
        emojiFrame.getContentPane().add(emojiTextArea);
        frame.setSize(750,680);
        emojiFrame.setSize(380, 270);
        frame.setVisible(true);
        outgoing.requestFocus();

    } // close the go methodical method

    public void sendMessage() {
        String textMessage = outgoing.getText().trim();
        try {
            if (textMessage.length() != 0) {
                writer.println(" " + userName + ":     " + textMessage);
                writer.flush();
                incoming.append(" You: " + textMessage + "\n\n");
                playSendAlert();
            } 
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        outgoing.setText("");
        outgoing.requestFocus();
    }

    private void setUpNetworking(String ip) {
        
        try {
            sock = new Socket(ip, 4010);
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

    public class SoundButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent ev) {
            if (soundAlerts) {
                soundAlerts = false;
                soundToggle.setText("Un-mute Sound");
            } else {
                soundAlerts = true;
                soundToggle.setText("Mute Sound");
            }
            
        }
    }

    public class EmojiButtonClicker implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (emojiToggle.getText() == "Show Emojis") {
                emojiToggle.setText("Hide Emojis");
                emojiFrame.setVisible(true);
            } else {
                emojiToggle.setText("Show Emojis");
                emojiFrame.setVisible(false);
            }
        }
    }

    public class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    incoming.append(message + "\n\n");
                    if (soundAlerts) {
                        playSoundAlert();
                    }
                }      
            } catch(Exception ex) {ex.printStackTrace();}
        }
    }

    private class EnterKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) {}

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                sendMessage();
            }
        }

        public void keyReleased(KeyEvent e) {}
    }

} //close whole thing


