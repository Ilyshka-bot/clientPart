import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

public class ChatGUI {

    private JTextField jtfMessage;
    public JTextArea jtaTextAreaMessage;
    private JTextField jUserName;
    private String clientName = "";
    public JFrame chatFr;

    public String getjUserName() {
        return jUserName.getText();
    }

    public void setJtaTextAreaMessage(String txt, JButton chatBtn){
        String strs[] = txt.split("\n");
        for(int i=0; i < strs.length; i++){
            if(strs[i].equals("Net")){
                chatFr.setVisible(false);
                chatBtn.setEnabled(true);
            }
        }
        this.jtaTextAreaMessage.append(txt + "\n");
    }

    public ChatGUI(JButton chatBtn, String username, PrintWriter outMessage, ProgramMain Main) {

        chatFr = new JFrame("Chat");

        Container contentPane = chatFr.getContentPane();

        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);

        contentPane.add(jtaTextAreaMessage);

        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        contentPane.add(jsp, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JLabel jtfName = new JLabel("User: " + username);
        bottomPanel.add(jtfName);
        clientName = username;

        jtfMessage = new JTextField("Enter your message: ");
        jtfMessage.setEnabled(false);
        bottomPanel.add(jtfMessage);

        JButton jbSendMessage = new JButton("Send");
        jbSendMessage.setEnabled(false);
        bottomPanel.add(jbSendMessage);

        JButton jConnectBtn = new JButton("Connect");
        bottomPanel.add(jConnectBtn);

        jUserName = new JTextField("");
        bottomPanel.add(jUserName);

        JButton jbBackBtn = new JButton("Назад");
        bottomPanel.add(jbBackBtn);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        jbSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jtfMessage.getText().trim().isEmpty()) {
                    sendMsg(clientName, outMessage);
                    jtfMessage.grabFocus();
                }
            }
        });

        jbBackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.sendMsg("##session##end##", outMessage);
                chatFr.setVisible(false);
                chatBtn.setEnabled(true);
            }
        });

        jConnectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.sendMsg("Chat", outMessage);
                Main.sendMsg(getjUserName(), outMessage);
                jConnectBtn.setEnabled(false);
                jUserName.setEnabled(false);
                jbSendMessage.setEnabled(true);
                jtfMessage.setEnabled(true);
            }
        });

        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });

        chatFr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Main.sendMsg("##session##end##",outMessage);
                chatBtn.setEnabled(true);
            }
        });
        chatFr.setVisible(true);
        chatFr.setResizable(false);
        chatFr.pack();
        chatFr.setSize( 600, 500);
        chatFr.setLocationRelativeTo(null);
    }

    public void sendMsg(String jtfName, PrintWriter outMessage) {
        String messageStr = jtfName + ": " + jtfMessage.getText();
        outMessage.println(messageStr);
        outMessage.flush();
        jtfMessage.setText("");
    }
}