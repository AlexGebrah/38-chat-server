package telran.chat.server.task;

import telran.chat.model.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServerReceiver implements Runnable {
    private final Socket socket;
    private final LinkedBlockingQueue<Message> messageBox;

    public ChatServerReceiver(Socket socket, LinkedBlockingQueue<Message> messageBox) {
        this.socket = socket;
        this.messageBox = messageBox;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Message message = (Message) ois.readObject();
                messageBox.put(message);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Client host: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " disconnected");
//            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
