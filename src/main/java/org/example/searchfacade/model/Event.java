package org.example.searchfacade.model;

import java.util.List;

import org.bson.types.ObjectId;

public record Event (
        ObjectId _id,
        String type,
        double time,
        String user,
        String ip
) {

    public static String eventsToJson(List<Event> events) {
        if (events.isEmpty()) {
            return "";
        }

        var json = new StringBuilder("{\n");

        for (Event event : events) {
            json.append(event.toString().indent(4).stripTrailing())
                    .append(",\n");
        }
        json.deleteCharAt(json.length() - 2);

        json.append("}");

        return json.toString();
    }

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
