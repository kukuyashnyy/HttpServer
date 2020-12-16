package org.itstep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Server
{
    private List<ClientThread> clients;
    private final int SERVER_PORT = 8080;

    public static void main( String[] args ) throws IOException {
        Server server = new Server();
        server.startServer();

    }

    public List<ClientThread> getClients() {
        return clients;
    }

    private void startServer() throws IOException {
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        acceptClients(serverSocket);
    }

    public void acceptClients(ServerSocket serverSocket) throws IOException {
        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("accepts : " + socket.getRemoteSocketAddress());
            ClientThread client = new ClientThread(socket, this);
            Thread thread = new Thread(client);
            thread.start();
            clients.add(client);
        }
    }
}
