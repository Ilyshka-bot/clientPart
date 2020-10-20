import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

public class AthorizGUI {

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public AthorizGUI(){ }

    public JFrame authFrame(RegisterGUI regGui, JButton authBtn, DefaultListModel listModel, JLabel label,
                            JButton dwndBtn, JButton chatBtn, JButton loadBtn, JButton serchBtn, JButton exitBtn, JButton regBtn,
                            PrintWriter outMessage, ProgramMain Main){

        JFrame authFr = new JFrame("Авторизация");
        Container contentPane = authFr.getContentPane();
        ProgramMain pr = new ProgramMain();//для полей

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        Component label2 = new JLabel("Имя пользователя:");
        JTextField field2 = new JTextField(23);
        contentPane.add(label2);
        contentPane.add(field2);
        pr.setLayout(layout, 10, 35, label2, 35,10,field2,contentPane);

        Component label3 = new JLabel("Пароль:");
        JPasswordField  field3 = new JPasswordField (23);
        contentPane.add(label3);
        contentPane.add(field3);
        pr.setLayout(layout, 10, 65, label3, 65,74,field3,contentPane);

        JButton authBtn1 = new JButton("Авторизироваться");
        contentPane.add(authBtn1);
        pr.setLayoutBtn(layout,authBtn1,200,115,contentPane);

        authBtn1.setEnabled(false);

        field2.getDocument().addDocumentListener((RegisterGUI.SimpleDocumentListener) e -> {
            if (field3.getText().trim().length() != 0 &&
                    field2.getText().trim().length() != 0)
                authBtn1.setEnabled(true);
        });

        field3.getDocument().addDocumentListener((RegisterGUI.SimpleDocumentListener) e -> {
            if (field3.getText().trim().length() != 0 &&
                    field2.getText().trim().length() != 0)
                authBtn1.setEnabled(true);
        });

        authBtn1.addActionListener(e -> {
            String user = field2.getText();
            String pass = field3.getText();
            int size = listModel.getSize();

            for(int i = 0; i < size; i++){
                 Object userCheck = listModel.get(i);
                    if ((user + " " + "offline").equals(userCheck.toString())
                            && pass.equals(regGui.checkUser(user))) {
                        Main.sendMsg("Auth", outMessage);
                        Main.sendMsg(user, outMessage);

                        label.setText("Вы вошли как: " + user);
                        setUsername(user);
                        authBtn.setEnabled(false);
                        authFr.setVisible(false);
                        dwndBtn.setEnabled(true);
                        chatBtn.setEnabled(true);
                        loadBtn.setEnabled(true);
                        serchBtn.setEnabled(true);
                        exitBtn.setEnabled(true);
                        regBtn.setEnabled(false);
                        break;
                    }
            }
        });

        JButton backBtn = new JButton("Назад");
        contentPane.add(backBtn);
        pr.setLayoutBtn(layout,backBtn,70,115,contentPane);
        backBtn.addActionListener(e -> {
            authFr.setVisible(false);
            authBtn.setEnabled(true);
        });

        authFr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                authBtn.setEnabled(true);
                super.windowClosing(e);
            }
        });
        authFr.pack();
        authFr.setSize(450, 200);
        authFr.setLocationRelativeTo(null);
        return authFr;
    }
}
