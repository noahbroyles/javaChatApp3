import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class test  {

    public test() throws BadLocationException {

        JFrame frame = new JFrame();
        DefaultStyledDocument document = new DefaultStyledDocument();
        JTextPane pane = new JTextPane(document);
        JPanel mainPanel = new JPanel();
        JButton button = new JButton("Button");
        button.setPreferredSize(new Dimension(100, 40));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane.setPreferredSize(new Dimension(200, 200));
        mainPanel.add(button);
        frame.getContentPane().add(pane, BorderLayout.CENTER);
        frame.getContentPane().add(mainPanel, BorderLayout.WEST);
        StyleContext context = new StyleContext();
        // build a style
        Style style = context.addStyle("test", null);
        // set some style properties
        StyleConstants.setForeground(style, Color.BLACK);
        document.insertString(0, "Four: success \n", style);
        StyleConstants.setForeground(style, Color.RED);
        document.insertString(0, "Three: error \n", style);
        document.insertString(0, "Two: error \n", style);

        StyleConstants.setForeground(style, Color.BLACK);
        // add some data to the document
        document.insertString(0, "One: success \n", style);


        //  StyleConstants.setForeground(style, Color.blue);

        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) throws BadLocationException {
        new test();
    }
}