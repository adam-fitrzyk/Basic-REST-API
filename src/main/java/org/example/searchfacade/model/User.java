package org.example.searchfacade.model;

import java.util.List;

import org.bson.types.ObjectId;

public record User (
        ObjectId _id,
        String user,
        String workstation
) {

    public static String usersToJson(List<User> users) {
        if (users.isEmpty()) {
            return "";
        }

        var json = new StringBuilder("{\n");

        for (User user : users) {
            json.append(user.toString().indent(4).stripTrailing())
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
                    "user" = "%s",
                    "workstation" = "%s"
                }
                """,
                _id.toString(), user, workstation
        );
    }
    
    public String toStringPretty() {
        return String.format(
            "User[_id=%s, user=%s, workstation=%s]",
            _id.toString(), user, workstation
        );
    }
}
