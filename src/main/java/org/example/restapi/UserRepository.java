package org.example.restapi;

import java.util.List;
import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

public class UserRepository extends MongoRepository {

    private static final UserRepository instance = new UserRepository();

    private MongoDBConnection connection;
    private MongoCollection<Document> collection;

    public UserRepository() {
        super("users");
        this.connection = this.getConnection();
        this.collection = this.getCollection();
    }

    public static UserRepository getInstance() {
        return instance;
    }

    public List<User> findAll() {
        ObjectId _id;
        String user;
        String workstation;

        var result = new ArrayList<User>();
        var docs = this.collection.find();

        if (docs.first() != null) {
            var cursor = docs.iterator();

            while (cursor.hasNext()) {
                var doc = cursor.next();

                _id = doc.getObjectId("_id");
                user = doc.getString("user");
                workstation = doc.getString("workstation");

                result.add(new User(_id, user, workstation));
            }
        }
        
        return result;
    }

    public User findById(String id) {
        ObjectId _id = new ObjectId(id);
        String user;
        String workstation;

        var doc = this.collection.find(eq("_id", _id)).first();

        if (doc != null) {
            user = doc.getString("user");
            workstation = doc.getString("workstation");

            return new User(_id, user, workstation);
        }

        return null;
    }

    public List<User> findByFilters(List<Document> filters) {
        // Load filters into a single query
        var query = this.loadQuery(filters);

        // Get matching entries 
        ObjectId _id;
        String user;
        String workstation;

        var result = new ArrayList<User>();
        var docs = this.collection.find(query);

        if (docs.first() != null) {
            var cursor = docs.iterator();

            while (cursor.hasNext()) {
                var doc = cursor.next();

                _id = doc.getObjectId("_id");
                user = doc.getString("user");
                workstation = doc.getString("workstation");

                result.add(new User(_id, user, workstation));
            }
        }     

        return result;
    }

    public void closeConnection() {
        this.connection.close();
    }

}
