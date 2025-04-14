package org.example.searchfacade.model;

import org.bson.types.ObjectId;

public class Event {
    
    private ObjectId _id;
    private String type;
    private double time;
    private String user;
    private String ip;

    public Event(ObjectId _id, String type, double time, String user, String ip) {
        this._id = _id;
        this.type = type;
        this.time = time;
        this.user = user;
        this.ip = ip;
    }

    public String toString() {
        return String.format(
                """
                {
                    "_id" = ObjectId("%s"),
                    "type" = "%s",
                    "time" = %.0f,
                    "user" = "%s",
                    "ip" = "%s"
                }
                """,
                _id.toString(), type, time, user, ip
        );
    }

    public String toStringPretty() {
        return String.format(
            "Event[_id=%s, type=%s, time=%.0f, user=%s, ip=%s]",
            _id.toString(), type, time, user, ip
        );
    }

}
