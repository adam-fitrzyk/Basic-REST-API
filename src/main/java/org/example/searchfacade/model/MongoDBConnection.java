package org.example.searchfacade.model;

import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.example.searchfacade.config.ConfigurationManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* tl_rest_api_task DATABASE PASSWORD:
 * --- LMBSBHwaaAV9bC3U ---
 */

public class MongoDBConnection {

    private static MongoDBConnection instance;
    private static final String CONNECTION_STRING = ConfigurationManager.getInstance().getConfiguration().mongodb_connection_string();
    private static final String DATABASE_NAME = ConfigurationManager.getInstance().getConfiguration().mongodb_name();
    
    private MongoClient client;
    private MongoDatabase database;

    private final Logger logger;

    private MongoDBConnection() {
        this.logger = LoggerFactory.getLogger(MongoDBConnection.class);
        this.open();
        logger.info("> MongoDatabase model formed successfully");
    }

    public static MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public void testPing() {
        var collection = database.getCollection("pings");
        collection.insertOne(new Document("ping", 1));
    }

    public void open() {
        var serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        var settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        this.client = MongoClients.create(settings);
        this.database = client.getDatabase(DATABASE_NAME);
    }

    public void close() {
        this.client.close();
    }

}