import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterGUI implements Serializable {

   private HashMap<String, String> userPass = new HashMap<>();

    public void setUserPass(HashMap<String, String> userPass) {
        this.userPass = userPass;
    }

    public HashMap<String, String> getUserPass() {
        return userPass;
    }

    public RegisterGUI(){ }
   //для того чтобы кнопка регистрации стала активной после ввода информации
    @FunctionalInterface
    public interface SimpleDocumentListener extends DocumentListener {
        void update(DocumentEvent e);

        @Override
        default void insertUpdate(DocumentEvent e) {
            update(e);
        }
        @Override
        default void removeUpdate(DocumentEvent e) {
            update(e);
        }
        @Override
        default void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }

    public JFrame regFrame(JButton regBtn, DefaultListModel listModel, PrintWriter outMessage, ProgramMain Main){

        JFrame regFr = new JFrame("Регистрация");
        regFr.setBounds(400, 300,450,230);
        Container contentPane = regFr.getContentPane();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        Component label = new JLabel("ФИО:");
        JTextField field = new JTextField(23);
        contentPane.add(label);
        contentPane.add(field);
        Main.setLayout(layout, 10, 25, label, 25,114,field,contentPane);

        Component label1 = new JLabel("Дата рождения:*");
        JTextField field1 = new JTextField(23);
        contentPane.add(label1);
        contentPane.add(field1);
        Main.setLayout(layout, 10, 50, label1, 50,47,field1,contentPane);

        Component label2 = new JLabel("Пользовательское имя:");
        JTextField field2 = new JTextField(23);
        field2.setDocument(new PlainDocument() {//ограничение на ввод
            String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (chars.indexOf(str) != -1) {
                    if (getLength()< 10) {
                        super.insertString(offs, str, a);
                    }
                }
            }
        });
        contentPane.add(label2);
        contentPane.add(field2);
        Main.setLayout(layout, 10, 75, label2, 75,5,field2,contentPane);

        Component label3 = new JLabel("Пароль:");
        JPasswordField  field3 = new JPasswordField (23);
        contentPane.add(label3);
        contentPane.add(field3);
        Main.setLayout(layout, 10, 100, label3, 100,97,field3,contentPane);

        JButton regBtn1 = new JButton("Зарегистрироваться");
        contentPane.add(regBtn1);
        Main.setLayoutBtn(layout,regBtn1,200,135,contentPane);
        regBtn1.setEnabled(false);

        field.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                if (field.getText().trim().length() != 0 && field3.getText().trim().length() != 0 &&
                        field2.getText().trim().length() != 0)
                    regBtn1.setEnabled(true);
            }
        });

        field2.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                if (field.getText().trim().length() != 0 && field3.getText().trim().length() != 0 &&
                        field2.getText().trim().length() != 0)
                    regBtn1.setEnabled(true);
            }
        });

        field3.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                if (field.getText().trim().length() != 0 && field3.getText().trim().length() != 0 &&
                        field2.getText().trim().length() != 0)
                    regBtn1.setEnabled(true);
            }
        });

        regBtn1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String fio = field.getText();
                String dateBirth = field1.getText();
                String userName = field2.getText();
                String pass = field3.getText();

                    if (userName.trim().length() != 0 && fio.trim().length() != 0 &&
                            pass.trim().length() != 0 && checkUser(userName) == null) {
                        userPass.put(userName, pass);
                        Main.sendMsg("Reg", outMessage);
                        Main.sendMsg(userName, outMessage);
                        Main.sendMsg(pass, outMessage);

                        regFr.setVisible(false);
                        regBtn.setEnabled(true);
                    }
            }
        });

        JButton backBtn = new JButton("Назад");
        contentPane.add(backBtn);
        Main.setLayoutBtn(layout,backBtn,70,135,contentPane);

        backBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                regFr.setVisible(false);
                regBtn.setEnabled(true);
            }
        });

        regFr.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                regBtn.setEnabled(true);
            }
        });

        return regFr;
    }

    public String checkUser(String user) {

        for (Map.Entry<String, String> entry : userPass.entrySet()) {
            if(user.equals(entry.getKey())){
                return entry.getValue();
            }
        }
        return null;
    }
}