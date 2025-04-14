package org.example.searchfacade.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;

@Disabled("Disabled due to school firewall")
public class MongoDBConnectionTest {

    static MongoDBConnection connection;

    @BeforeAll
    static void openConnection() {
        System.out.println(">>> MONGO DATABASE ACCESS TEST");
        connection = MongoDBConnection.getInstance();
    }

    @AfterAll
    static void closeConection() {
        connection.close();
    }

    /* Test UserRepository class */
    @Test
    void testConnection() {
        var db = connection.getDatabase();
        var users = db.getCollection("users");
        var events = db.getCollection("events");

        // Get data from database to ensure connection is successful
        var first_user = users.find().first();
        System.out.println("First user found in <users> collection: " + first_user.toString());
        assert first_user != null;
        var first_event = events.find().first();
        System.out.println("First event found in <events> collection: " + first_event.toString());
        assert first_event != null;

        // Ping database to ensure connection is open for download and upload
        connection.testPing();
    }

}
