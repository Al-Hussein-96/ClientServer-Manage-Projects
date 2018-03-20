package client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FileClient {

    private Socket s;
    DataOutputStream dos;

    public FileClient(String file) {
        try {

            sendFolder(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String file, boolean bo) throws IOException {
        s = new Socket("localhost", 1988);
        dos = new DataOutputStream(s.getOutputStream());
        if (bo) {
            FileInputStream fis = new FileInputStream(file);
            File f = new File(file);
            int size = (int) f.length();
            String path = f.getPath();
            System.out.println("Size file = " + size);
            System.out.println("the path : " + path);
            dos.writeInt(size);
            dos.writeChars(path + "\n");

            byte[] buffer = new byte[4096];
            while (fis.read(buffer) > 0) {
                dos.write(buffer);
            }
            fis.close();
        } else {
            File f = new File(file);
            String path = f.getPath();
            dos.writeInt(-1);
            dos.writeChars(path + "\n");
        }
        dos.close();

    }

    public void sendFolder(String folder) throws IOException {
        File f = new File(folder);
        if (!f.isDirectory()) {
            sendFile(folder, true);
            return;
        } else {
            sendFile(folder, false);
        }
        String[] directories = f.list();
        for (String d : directories) {
            sendFolder(folder + '\\' + d);
        }
    }

    public static void main(String[] args) {
        FileClient fc = new FileClient("src\\Moaz");
    }

}
