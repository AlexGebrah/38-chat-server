package telran.chat.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private static final long serialVersionUID = 20250522L;

    private String nickName;
    private LocalTime time;
    private String message;

    public Message(String nickName, String message) {
        this.nickName = nickName;
        this.message = message;
        time = LocalTime.now();
    }

    @Override
    public String toString() {
        return nickName + " [" + time.format(DateTimeFormatter.ofPattern("HH:mm")) + "] " + message;
    }
}
