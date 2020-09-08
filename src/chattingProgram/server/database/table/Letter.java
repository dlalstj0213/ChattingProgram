package chattingProgram.server.database.table;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Letter {
    public String own;
    public String from;
    public String to;
    public ZonedDateTime when;
    public String content;

    @Override
    public String toString() {
        return "Letter{" +
                "own='" + own + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", when=" + when +
                ", content='" + content + '\'' +
                '}';
    }
}
