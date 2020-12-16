package org.itstep;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {

    private Socket socket;
    private Server server;
    private PrintWriter clientOut;

    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public PrintWriter getWriter() {
        return clientOut;
    }

    private String getFile(String filePath) throws IOException {
        FileReader fr = new FileReader(filePath);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder(1024);
        String s = "";
        while ((s = bf.readLine()) != null) {
            sb.append(s + "\r\n");
        }
        return sb.toString();
    }

    @Override
    public void run() {
        try {
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());
            StringBuilder fullRequest = new StringBuilder();
            while (!socket.isClosed()) {
                if (in.hasNextLine()) {
                    String input = in.nextLine();
                    if (input != null) {
//                        System.out.println(input);
                        fullRequest.append(input).append("\r\n");
                        if (fullRequest.toString().contains("\r\n\r\n")) {
                            if (fullRequest.toString().contains("GET / HTTP/1.1") ||
                                    fullRequest.toString().contains("GET /index.html HTTP/1.1")) {
                                clientOut.write("HTTP/1.1 200 OK\r\n");
                                clientOut.write("Content-Type: text/html; charset=utf-8\r\n");
                                clientOut.write("\r\n");
                                clientOut.write(getFile("src/main/html/index.html"));
                                clientOut.write("\r\n");
                                clientOut.flush();
                            }
                            if (fullRequest.toString().contains("GET /page.html HTTP/1.1")) {
                                clientOut.write("HTTP/1.1 200 OK\r\n");
                                clientOut.write("Content-Type: text/html; charset=utf-8\r\n");
                                clientOut.write("\r\n");
                                clientOut.write(getFile("src/main/html/page.html"));
                                clientOut.write("\r\n");
                                clientOut.flush();
                            }
                            if (fullRequest.toString().contains("GET /test.html HTTP/1.1")) {
                                clientOut.write("HTTP/1.1 404 Page not found\r\n");
                                clientOut.write("Content-Type: text/html; charset=utf-8\r\n");
                                clientOut.write("\r\n");
                                clientOut.write(getFile("src/main/html/404.html"));
                                clientOut.write("\r\n");
                                clientOut.flush();
                            }
                            fullRequest = new StringBuilder();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
