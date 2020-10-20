import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.swing.*;

public class MainWindow {

    private final static String SERVER_HOST = "localhost";
    private final static int SERVER_PORT = 3443;
    private ChatGUI chatGui = null;
    private DownloadFileGUI downloadFileGUI = null;
    public DefaultListModel userList = null;
    private Socket clientSocket;
    private Scanner inMessage;//входящее сообщение
    private PrintWriter outMessage;//исходящее сообщение

    public MainWindow(){
        userList = new DefaultListModel();
        File filePath = new File("FilesFromServer");//создание папки для файлов пользователей
        if(!filePath.exists()){
            filePath.mkdir();
        }
        ProgramMain Main = new ProgramMain();
        JFrame frame = new JFrame("Программа INTERNET");

            try {
                clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
                inMessage = new Scanner(clientSocket.getInputStream());
                outMessage = new PrintWriter(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        SerchFileGUI serchFile = new SerchFileGUI();
        RegisterGUI regGui = new RegisterGUI();

        //для заполнения списка пользователей
        outMessage.println("OK");
        outMessage.flush();

        AthorizGUI authGui = new AthorizGUI();
        PublicationFileGUI pubGui = new PublicationFileGUI();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel listPanel = new JPanel();
        listPanel.setPreferredSize(new Dimension(400, 500));
        final JList list = new JList(userList);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        JScrollPane mainList = new JScrollPane(list);
        mainList.setPreferredSize(new Dimension(360,490));
        listPanel.add(mainList);

        JPanel btnPannel = new JPanel(new GridLayout(8, 1,7,7));

        JLabel label = new JLabel("Вы вошли как:");
        btnPannel.add(label,BorderLayout.NORTH);

        JButton regBtn = new JButton("Регистрация");
        btnPannel.add(regBtn,BorderLayout.WEST);

        PrintWriter finalOutMessage1 = outMessage;
        RegisterGUI finalRegGui = regGui;
        regBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                regBtn.setEnabled(false);
                JFrame regFr = finalRegGui.regFrame(regBtn, userList, finalOutMessage1, Main);
                regFr.setResizable(false);
                regFr.setVisible(true);
            }
        });

        JButton authBtn = new JButton("Авторизация");
        btnPannel.add(authBtn);

        JButton dwndBtn = new JButton("Скачать файл");
        dwndBtn.setEnabled(false);
        btnPannel.add(dwndBtn);
        dwndBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                outMessage.println("GetFilesList");
                outMessage.flush();
                dwndBtn.setEnabled(false);
            }
        });

        JButton chatBtn = new JButton("Чат");
        chatBtn.setPreferredSize(new Dimension(20,20));
        chatBtn.setEnabled(false);
        btnPannel.add(chatBtn);

        JButton loadBtn = new JButton("Опубликовать файл");
        loadBtn.setEnabled(false);
        btnPannel.add(loadBtn);
        loadBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFrame pubFile = pubGui.PubFile(loadBtn, outMessage,clientSocket);
                loadBtn.setEnabled(false);
                pubFile.setResizable(false);
                pubFile.setVisible(true);
            }
        });

        JButton serchBtn = new JButton("Поиск файла");
        serchBtn.setEnabled(false);
        btnPannel.add(serchBtn);
        serchBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                serchBtn.setEnabled(false);
                JFrame serchFr = serchFile.serchFr(serchBtn, outMessage);
                serchFr.setResizable(false);
                serchFr.setVisible(true);
            }
        });

        JButton exitBtn = new JButton("Сменить пользователя");
        exitBtn.setEnabled(false);
        btnPannel.add(exitBtn);

        authBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                authBtn.setEnabled(false);
                JFrame authFr = authGui.authFrame(regGui, authBtn, userList, label, dwndBtn,
                        chatBtn, loadBtn, serchBtn, exitBtn, regBtn, outMessage, Main);
                authFr.setResizable(false);
                authFr.setVisible(true);
            }
        });

        chatBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                chatGui = new ChatGUI(chatBtn, authGui.getUsername(), outMessage, Main);
                chatBtn.setEnabled(false);
            }
        });

        exitBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                label.setText("Вы вошли как:");
                System.out.println("EXituser");
                exitBtn.setEnabled(false);
                dwndBtn.setEnabled(false);
                chatBtn.setEnabled(false);
                loadBtn.setEnabled(false);
                serchBtn.setEnabled(false);
                authBtn.setEnabled(true);
                regBtn.setEnabled(true);

                int size = userList.getSize();

                for(int i = 0; i < size; i++) {
                    String userOnline = (String) userList.get(i);
                    String[] user = userOnline.split(" ");
                    if(user[0].equals(authGui.getUsername())) {
                        exitUser(user[0], outMessage);
                    }
                }
            }
        });

        JPanel east = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        east.add(btnPannel, gbc);

        frame.add(east, BorderLayout.EAST);
        frame.add(listPanel);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (inMessage.hasNext()) {
                            String inMes = inMessage.nextLine();

                            if(inMes.equals("usersOnline")){
                                System.out.println(inMes);
                                String servUserPassOnline = inMessage.nextLine();
                                String servUserPass = inMessage.nextLine();

                                HashMap<String, String> onlineuser = (HashMap<String, String>) Main.deserialize(servUserPassOnline);
                                HashMap<String, String> users = (HashMap<String, String>) Main.deserialize(servUserPass);
                                regGui.setUserPass(users);

                                for(Map.Entry<String, String> entry : users.entrySet()){
                                    for(Map.Entry<String, String> entryonline : onlineuser.entrySet()){
                                        if(!entry.getKey().equals(entryonline.getKey())){
                                            userList.addElement(entry.getKey() + " " + "offline");
                                        }else{
                                            userList.addElement(entry.getKey() + " " + "online");
                                        }
                                    }
                                }
                            }

                            if(inMes.equals("First")){
                                String servUserPass = inMessage.nextLine();
                                try {
                                    regGui.setUserPass((HashMap<String, String>) Main.deserialize(servUserPass));
                                    for (HashMap.Entry<String, String> entry : regGui.getUserPass().entrySet()) {
                                        userList.addElement(entry.getKey() + " " + "offline");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }

                            }
                            if(inMes.equals("newRegInf")){
                                System.out.println(inMes);
                                inMes = inMessage.nextLine();
                                userList.addElement(inMes + " offline");
                            }
                            if(inMes.equals("newAuthInf")){
                                System.out.println(inMes);
                                inMes = inMessage.nextLine();
                                int size = userList.getSize();

                                for(int i = 0; i < size; i++) {
                                    Object userCheck = userList.get(i);
                                    if ((inMes + " offline").equals(userCheck.toString())) {
                                        userList.remove(i);
                                        userList.addElement(inMes + " online");
                                    }
                                }
                            }
                            if(inMes.equals("exitInf")){
                                System.out.println(inMes);
                                inMes = inMessage.nextLine();
                                int size = userList.getSize();
                                for(int i = 0; i < size; i++) {
                                    Object userCheck = userList.get(i);
                                    if ((inMes + " online").equals(userCheck.toString())) {
                                        userList.remove(i);
                                        userList.addElement(inMes + " offline");
                                    }
                                }
                            }
                            if(inMes.equals("ResultPoisk")) {
                                System.out.println(inMes);
                                int sizeUser = inMessage.nextInt();
                                String[] users = null;
                                if(sizeUser != 0) {
                                    int i = 0;
                                    users = new String[sizeUser];
                                    inMes = inMessage.nextLine();
                                    while (i < sizeUser) {
                                        inMes = inMessage.nextLine();
                                        users[i] = inMes;
                                        i++;
                                    }
                                    serchFile.showResultFrames(users, sizeUser);
                                }else
                                    serchFile.showResultFrames(users, sizeUser);
                            }
                            if(inMes.equals("ListFiles")){
                                System.out.println(inMes);
                                int size = Integer.parseInt(inMessage.nextLine());
                                ArrayList<String> userAndFiles = new ArrayList<>();
                                int i = 0;
                                while (i < size) {
                                    inMes = inMessage.nextLine();
                                    userAndFiles.add(inMes);
                                    i++;
                                }
                                downloadFileGUI = new DownloadFileGUI();
                                JFrame fr = downloadFileGUI.DownloadFr(Main, dwndBtn, userAndFiles, outMessage);
                                fr.setVisible(true);

                            }
                            if(inMes.equals("FileEst")){
                                String userName = inMessage.nextLine();
                                String fileName = inMessage.nextLine();

                                if(userName.equals("FailaNet")){
                                    System.out.println(userName);
                                    downloadFileGUI.ResDownloadFr(userName, fileName);
                                }else {
                                    File fileUser = new File(filePath.getPath(), userName);
                                    if (!fileUser.exists()) {
                                        fileUser.mkdir();
                                    }
                                    File copyFileName = new File(fileUser.getPath(), fileName);
                                    copyFileName.createNewFile();

                                    byte[] mybytearray = new byte[1024];
                                    InputStream is = clientSocket.getInputStream();
                                    FileOutputStream fos = new FileOutputStream(copyFileName);
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                                    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                                    bos.write(mybytearray, 0, bytesRead);
                                    bos.close();
                                    downloadFileGUI.ResDownloadFr(userName, fileName);

                                    System.out.println("Download!");
                                }
                            }

                            if (inMes.equals("ChatOpen")) {
                            System.out.println(inMes);
                                while (chatGui.chatFr.isVisible()) {
                                    inMes = inMessage.nextLine();
                                    if (inMes.equals("Close")) {
                                        chatGui.chatFr.setVisible(false);
                                        chatBtn.setEnabled(true);
                                    }
                                    chatGui.setJtaTextAreaMessage(inMes, chatBtn);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }).start();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                exitUser(authGui.getUsername() ,outMessage);
            }
        });

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(540,500);
        frame.setResizable(false);
    }

    public void exitUser(String userName, PrintWriter outMessage){
        outMessage.println("Exit");
        outMessage.flush();
        outMessage.println(userName);
        outMessage.flush();
    }

    public static void main(String[] args) {
        MainWindow app = new MainWindow();
    }
}
