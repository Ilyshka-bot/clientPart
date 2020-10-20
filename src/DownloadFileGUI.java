import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DownloadFileGUI {

    public DefaultListModel userList = new DefaultListModel();

    public DownloadFileGUI(){ }

    public JFrame DownloadFr(ProgramMain Main, JButton dnwldBtn, ArrayList<String> userAndFiles,
                             PrintWriter outMessage){

        JFrame frame = new JFrame("Скачивание файла");
        JPanel listPanel = new JPanel();

        listPanel.setPreferredSize(new Dimension(400, 500));
        final JList list = new JList(userList);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        JScrollPane mainList = new JScrollPane(list);
        mainList.setPreferredSize(new Dimension(360,490));
        listPanel.add(mainList);

        for(int j = 0; j < userAndFiles.size(); j++) {
            userList.addElement(userAndFiles.get(j));
        }

        JPanel btnPannel = new JPanel(new GridLayout(6, 1,7,7));

        JLabel userLabel = new JLabel("Имя пользователя:");
        btnPannel.add(userLabel);

        JTextField userName = new JTextField("");
        btnPannel.add(userName);

        JLabel fileLabel = new JLabel("Имя файла:");
        btnPannel.add(fileLabel);

        JTextField fileName = new JTextField("");
        btnPannel.add(fileName);

        JButton dwnBtn = new JButton("Скачать файл");
        btnPannel.add(dwnBtn);
        dwnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.sendMsg("DownloadFile", outMessage);
                Main.sendMsg(userName.getText(), outMessage);
                Main.sendMsg(fileName.getText(), outMessage);
                dwnBtn.setEnabled(false);
            }
        });

        JButton backBtn = new JButton("Назад");
        btnPannel.add(backBtn);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                dnwldBtn.setEnabled(true);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dnwldBtn.setEnabled(true);
            }
        });

        JPanel east = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        east.add(btnPannel, gbc);

        frame.add(east, BorderLayout.EAST);
        frame.add(listPanel);
        frame.pack();
        frame.setSize(540,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    public void ResDownloadFr(String userName, String fileName){
        ProgramMain Main = new ProgramMain();
        JFrame resultFr = new JFrame("Результат скачивания");

        Container contentPane = resultFr.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        JLabel netRes = null;

        if(userName.equals("FailaNet")) {
                netRes = new JLabel("Искомый файл " + fileName + " не был найден!");
                layout.putConstraint(SpringLayout.WEST , netRes, 53,
                        SpringLayout.WEST , contentPane);
                layout.putConstraint(SpringLayout.NORTH, netRes, 23,
                        SpringLayout.NORTH, contentPane);
            resultFr.pack();
            resultFr.setSize(300, 110);
        }else{
            netRes = new JLabel("Файл " + fileName + " был скачан у пользователя " + userName + ".");
            layout.putConstraint(SpringLayout.WEST , netRes, 63,
                    SpringLayout.WEST , contentPane);
            layout.putConstraint(SpringLayout.NORTH, netRes, 23,
                    SpringLayout.NORTH, contentPane);
            resultFr.pack();
            resultFr.setSize(500, 110);
        }
        contentPane.add(netRes);
        resultFr.setResizable(false);
        resultFr.setVisible(true);
        resultFr.setLocationRelativeTo(null);
    }
}
