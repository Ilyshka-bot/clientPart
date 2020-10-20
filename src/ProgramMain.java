import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Base64;

public class ProgramMain {

    public ProgramMain(){};

    public Object deserialize(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public void setLayout(SpringLayout layout, int x, int y, Component label, int y1, int x1, JTextField text, Container contentPane){
        layout.putConstraint(SpringLayout.WEST , label, x,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.NORTH, label, y,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, text, y1,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST , text, x1,
                SpringLayout.EAST , label);
    }

    public void setLayoutBtn(SpringLayout layout, JButton btn, int pad1, int pad2, Container contentPane){
        layout.putConstraint(SpringLayout.WEST , btn, pad1,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.NORTH, btn, pad2,
                SpringLayout.NORTH, contentPane);
    }

    public void sendMsg(String msg, PrintWriter outMessage){
        outMessage.println(msg);
        outMessage.flush();
    }
}
