import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

public class SerchFileGUI {

    private  JButton  btnSerchFile    = null;
    private  JButton  backBtn = null;
    private JTextField jtFile;

    public String getJtFile() {
        return this.jtFile.getText();
    }

    public SerchFileGUI( ){ }

    public JFrame serchFr(JButton serchBtn, PrintWriter outMessage){

        JFrame SerchFr = new JFrame("Поиск файла");
        SerchFr.setBounds(400, 300,450,200);

        btnSerchFile = new JButton("Поиск");
        backBtn = new JButton("Назад");
        jtFile = new JTextField("Enter file to search: ", 23);

        addListeners(SerchFr, serchBtn, outMessage);

        JPanel contents = new JPanel();
        contents.add(backBtn);
        contents.add(jtFile);
        contents.add(btnSerchFile);

        SerchFr.setContentPane(contents);

        SerchFr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                serchBtn.setEnabled(true);
            }
        });

        return SerchFr;
    }

    public void addListeners(JFrame sercFr, JButton btnSerch, PrintWriter outMessage){
        jtFile.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtFile.setText("");
            }
        });
        btnSerchFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outMessage.println("Poisk");
                outMessage.flush();
                outMessage.println(getJtFile());
                outMessage.flush();
                jtFile.setText(getJtFile());
            }
        });
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sercFr.setVisible(false);
                btnSerch.setEnabled(true);
            }
        });
    }

    public void showResultFrames(String[] users, int SizeUser){
        JFrame net = new JFrame("Результат поиска");
        Container contentPane = net.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        JLabel netRes = null;
        if(SizeUser == 0)
        {
            netRes = new JLabel("Поиск не дал результатов!");
            layout.putConstraint(SpringLayout.WEST , netRes, 53,
                    SpringLayout.WEST , contentPane);
            layout.putConstraint(SpringLayout.NORTH, netRes, 23,
                    SpringLayout.NORTH, contentPane);
            net.pack();
            net.setSize(300, 110);
        }else{
            String strUsers = "";
            for(int i = 0 ; i < users.length; i++) {
                System.out.println(users[i]);
                strUsers = strUsers + users[i] + " ";
                System.out.println(strUsers);

            }
            netRes = new JLabel("Файл " + getJtFile() + " был найден у пользователя(ей): " + strUsers + ".");
            layout.putConstraint(SpringLayout.WEST , netRes, 63,
                    SpringLayout.WEST , contentPane);
            layout.putConstraint(SpringLayout.NORTH, netRes, 23,
                    SpringLayout.NORTH, contentPane);
            net.pack();
            net.setSize(500, 110);
        }
        contentPane.add(netRes);
        net.setResizable(false);
        net.setVisible(true);
        net.setLocationRelativeTo(null);
    }
}

