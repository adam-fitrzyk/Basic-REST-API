package org.example.searchfacade.model;

import org.bson.types.ObjectId;

public class User {

    private ObjectId _id;
    private String user;
    private String workstation;

    public User(ObjectId _id, String user, String workstation) {
        this._id = _id;
        this.user = user;
        this.workstation = workstation;
    }

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
