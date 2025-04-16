package org.example.searchfacade.model;

import org.bson.types.ObjectId;

public record Event (
        ObjectId _id,
        String type,
        double time,
        String user,
        String ip
) {

    @Override
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
