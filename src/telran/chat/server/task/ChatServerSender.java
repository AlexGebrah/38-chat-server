package telran.chat.server.task;

import telran.chat.model.Message;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServerSender implements Runnable {
    private final LinkedBlockingQueue<Message> messageBox;
    private final Set<ObjectOutputStream> clients;

    public ChatServerSender(LinkedBlockingQueue<Message> messageBox) {
        this.messageBox = messageBox;
        clients = new HashSet<>();
    }

    public synchronized boolean addClient(Socket socket) throws IOException {
        return clients.add(new ObjectOutputStream(socket.getOutputStream()));
    }

    @Override
    public void run() {
        while (true) {
            Message message = null;
            try {
                message = messageBox.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                Iterator<ObjectOutputStream> iterator = clients.iterator();
                while (iterator.hasNext()) {
                    try {
                        iterator.next().writeObject(message);
                    } catch (IOException e) {
                        iterator.remove();
                    }
                }
            }
            System.out.println("size = " + clients.size());
        }
    }
}
