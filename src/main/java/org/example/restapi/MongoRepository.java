package org.example.restapi;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.empty;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;

public class MongoRepository {
    
    private MongoDBConnection connection;
    private MongoCollection<Document> collection;

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
        var bson_filter_list = new ArrayList<Bson>();
        String attribute;
        String operator;
        int from;
        int to;

        // Load each filter and combine into a query
        for (var filter : filters) {
            attribute = filter.getString("attribute");
            operator = filter.getString("operator");

            Bson bson_filter = empty();
            try {
                // If value field is used, form filter with that value
                var value = filter.get("value");
                switch (operator) {
                    case "eq":
                        bson_filter = eq(attribute, value);
                        break;
                    case "lte":
                        bson_filter = lte(attribute, value);
                        break;
                    case "gte":
                        bson_filter = gte(attribute, value);
                        break;
                }
            }
            catch (Exception e) {
                // Otherwise, form filter with range
                var rangeDoc = (Document) filter.get("range");
                from = rangeDoc.getInteger("from");
                to = rangeDoc.getInteger("to");

                bson_filter = and(gte(attribute, from), lte(attribute, to));
            }

            bson_filter_list.add(bson_filter);
        }

        return and(bson_filter_list);
    }

}
