package telran.chat.server;

import telran.chat.model.Message;
import telran.chat.server.task.ChatServerReceiver;
import telran.chat.server.task.ChatServerSender;
import telran.mediation.BlkQueue;
import telran.mediation.BlkQueueImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerAppl {
    public static void main(String[] args) {
        int port;
        if (args.length == 0) {
            port = 9000;
        } else {
            port = Integer.parseInt(args[0]);
        }
        BlkQueue<Message> messageBox = new BlkQueueImpl<>(10);
        ChatServerSender sender = new ChatServerSender(messageBox);
        Thread senderThread = new Thread(sender);
        senderThread.setDaemon(true);
        senderThread.start();
        ExecutorService service = Executors.newFixedThreadPool(16);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("Server is waiting...");
                Socket socket = serverSocket.accept();
                System.out.println("Connection established");
                System.out.println("Client host: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                sender.addClient(socket);
                ChatServerReceiver receiver = new ChatServerReceiver(socket, messageBox);
                service.execute(receiver);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
