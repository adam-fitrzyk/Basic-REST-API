package org.example.searchfacade.model;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.empty;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;

public class MongoRepository {
    
    private final MongoDBConnection connection;
    private final MongoCollection<Document> collection;

    public MongoRepository(String collection_name) {
        this.connection = MongoDBConnection.getInstance();
        this.collection = connection.getDatabase().getCollection(collection_name);
    }

    public MongoDBConnection getConnection() {
        return this.connection;
    }

    public MongoCollection<Document> getCollection() {
        return this.collection;
    }

    public Bson loadQuery(List<Document> filters) {
        if (filters.isEmpty()) {
            return empty();
        }
        
        var bson_filter_list = new ArrayList<Bson>();
        String attribute;
        String operator;

        // Load each filter and combine into a query
        for (var filter : filters) {
            attribute = filter.getString("attribute");
            operator = filter.getString("operator");

            Bson bson_filter = empty();

            var value = filter.get("value");
            if (value != null) {
                // If value field is used, form filter with that value

                if (attribute.equals("_id")) {
                    try {
                        value = new ObjectId(value.toString());
                    } catch (Exception e) {
                        assert true;
                    }
                }

                bson_filter = switch (operator) {
                    case "eq" -> eq(attribute, value);
                    case "lte" -> lte(attribute, value);
                    case "gte" -> gte(attribute, value);
                    default -> bson_filter;
                };
            }
            else {
                // Otherwise, form filter with range
                var rangeDoc = (Document) filter.get("range");
                var from = rangeDoc.get("from");
                var to = rangeDoc.get("to");

                bson_filter = and(gte(attribute, from), lte(attribute, to));
            }

            bson_filter_list.add(bson_filter);
        }

        return and(bson_filter_list);
    }

    public void closeConnection() {
        this.connection.close();
    }

}
