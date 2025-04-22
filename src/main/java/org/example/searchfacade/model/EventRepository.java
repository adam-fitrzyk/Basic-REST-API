package org.example.searchfacade.model;

import java.util.List;
import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

public class EventRepository extends MongoRepository {

    private static EventRepository instance;
    public static final String COLLECTION_NAME = "events";

    private final MongoCollection<Document> collection;

    private EventRepository() {
        super(COLLECTION_NAME);
        this.collection = this.getCollection();
    }

    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }

    public List<Event> findAll() {
        ObjectId _id;
        String type;
        double time;
        String user;
        String ip;

        var result = new ArrayList<Event>();
        var docs = this.collection.find();

        if (docs.first() != null) {
            for (Document doc : docs) {
                _id = doc.getObjectId("_id");
                type = doc.getString("type");
                time = doc.getDouble("time");
                user = doc.getString("user");
                ip = doc.getString("ip");

                result.add(new Event(_id, type, time, user, ip));
            }
        }

        return result;
    }

    public Event findById(String id) {
        ObjectId _id = new ObjectId(id);
        String type;
        double time;
        String user;
        String ip;

        var doc = this.collection.find(eq("_id", _id)).first();

        if (doc != null) {
            type = doc.getString("type");
            time = doc.getDouble("time");
            user = doc.getString("user");
            ip = doc.getString("ip");

            return new Event(_id, type, time, user, ip);
        }
        
        return null;
    }

    public List<Event> findByFilters(List<Document> filters) {
        // Load filters into a single query
        var query = this.loadQuery(filters);

        // Get matching entries 
        ObjectId _id;
        String type;
        double time;
        String user;
        String ip;

        var result = new ArrayList<Event>();
        var docs = this.collection.find(query);

        if (docs.first() != null) {
            for (Document doc : docs) {
                _id = doc.getObjectId("_id");
                type = doc.getString("type");
                time = doc.getDouble("time");
                user = doc.getString("user");
                ip = doc.getString("ip");

                result.add(new Event(_id, type, time, user, ip));
            }
        }

        return result;
    }

}
