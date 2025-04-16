package org.example.searchfacade.model;

import org.bson.types.ObjectId;

public record User (
        ObjectId _id,
        String user,
        String workstation
) {

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
