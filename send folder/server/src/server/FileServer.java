package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {

    private ServerSocket ss;

    public FileServer(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket clientSock = ss.accept();
                saveFile(clientSock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(Socket clientSock) throws IOException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        int filesize = dis.readInt(); // Send file size in separate msg
        String temp = dis.readLine();
        String path = "";
        for (int i = 0; i < temp.length(); i++) {
            if (i % 2 == 0) {
                continue;
            }
            path += temp.charAt(i);
        }
        if (filesize == -1) {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdir();
            }
            return;
        }
        FileOutputStream fos = new FileOutputStream(path);
        byte[] buffer = new byte[4096];
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        fos.close();
        dis.close();
    }

    public static void main(String[] args) {
        FileServer fs = new FileServer(1988);
        fs.start();
    }

}
