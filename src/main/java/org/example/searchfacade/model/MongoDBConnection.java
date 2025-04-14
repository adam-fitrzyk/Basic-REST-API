package org.example.searchfacade.model;

import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/* tl_rest_api_task DATABASE PASSWORD:
 * --- LMBSBHwaaAV9bC3U ---
 */

public class MongoDBConnection {

    private static MongoDBConnection instance;
    private static final String CONNECTION_STRING = "mongodb+srv://fitrzyka:LMBSBHwaaAV9bC3U@mymongocluster.w8ticzb.mongodb.net/?retryWrites=true&w=majority&appName=MyMongoCluster";
    private static final String DATABASE_NAME = "tl_rest_api_task";
    
    private MongoClient client;
    private MongoDatabase database;

    private MongoDBConnection() {
        var serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();

        var settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(CONNECTION_STRING))
            .serverApi(serverApi)
            .build();

        this.client = MongoClients.create(settings);
        this.database = client.getDatabase(DATABASE_NAME);
        System.out.println("> MongoDatabase model formed successfully");
    }

    public static MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }

    public void testPing() {
        var collection = database.getCollection("pings");
        collection.insertOne(new Document("ping", 1));
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public void close() {
        this.client.close();
    }

}