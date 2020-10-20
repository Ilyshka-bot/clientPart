import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class PublicationFileGUI extends JFrame {

    private  JButton  btnPyblFile   = null;
    private  JButton  btnSendFile    = null;
    private  JButton  backBtn = null;
    private Socket client = null;

    private  JFileChooser fileChooser = null;
    private File file = null;

    public PublicationFileGUI() { }

    public JFrame PubFile(JButton loadBtn, PrintWriter outMessage, Socket Client){
        this.client = Client;

        JFrame pyblFr = new JFrame("Публикация файла");

        btnSendFile = new JButton("Отправить файл");
        btnSendFile.setEnabled(false);

        btnPyblFile = new JButton("Выбрать файл");

        backBtn = new JButton("Назад");
        fileChooser = new JFileChooser();
        addFileChooserListeners(loadBtn, outMessage, pyblFr);

        JPanel contents = new JPanel();
        contents.add(btnSendFile);
        contents.add(btnPyblFile);
        contents.add(backBtn);
        pyblFr.setContentPane(contents);

        pyblFr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                loadBtn.setEnabled(true);
            }
        });
        pyblFr.setVisible(true);
        pyblFr.pack();
        pyblFr.setSize(450,200);
        pyblFr.setLocationRelativeTo(null);
        return pyblFr;
    }

    public void addFileChooserListeners(JButton loadBtn, PrintWriter outMes, JFrame pybFr){

        btnPyblFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Обзор");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showDialog(PublicationFileGUI.this, "Выбрать");
                if (result == JFileChooser.APPROVE_OPTION )
                    JOptionPane.showMessageDialog(PublicationFileGUI.this,
                            "Файл " + fileChooser.getSelectedFile() +
                                    " был выбран, НЕПЛОХО!");

                file = fileChooser.getSelectedFile();
                btnSendFile.setEnabled(true);
            }
        });

        btnSendFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                pubFile(file,client, outMes);
                loadBtn.setEnabled(true);
                btnSendFile.setEnabled(false);
            }
        });

        backBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                loadBtn.setEnabled(true);
                pybFr.setVisible(false);

            }
        });
    }

    public void pubFile(File file, Socket socket, PrintWriter outMessage){
        try {
            outMessage.println("Publication");
            outMessage.flush();
            outMessage.println(file.getName());
            outMessage.flush();
            outMessage.println(file.length());
            outMessage.flush();

            byte[] mybytearray = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = socket.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

