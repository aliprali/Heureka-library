package heureka.cz.internal.library.repository;

import java.util.Date;

/**
 * Created by tomas on 25.5.16.
 */
public class BookReservation {

    public String user;
    public Date from;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }
}
