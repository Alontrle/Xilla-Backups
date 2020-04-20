package net.xilla.backupcore.api.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.xilla.backupcore.api.config.Config;
import net.xilla.backupcore.api.config.ConfigManager;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Mongo {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> responses;
    private MongoCollection<Document> requests;
    private MongoCollection<Document> nodes;
    private MongoCollection<Document> clientServers;
    private MongoCollection<Document> storageServers;
    private MongoCollection<Document> files;

    public Mongo() {
        //Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        //mongoLogger.setLevel(Level.WARNING);
        Config config = ConfigManager.getInstance().getConfig("settings.json");

        String mongoUser = config.getString("mongodb-user");
        String mongoPass = config.getString("mongodb-pass");
        String mongoHost = config.getString("mongodb-host");
        String mongoDatabase = config.getString("mongodb-db");
        String mongoAuthDatabase = config.getString("mongodb-authdb");

        ConnectionString connString = new ConnectionString(
                "mongodb://" + mongoUser + ":" + mongoPass + "@" + mongoHost + "/" + mongoAuthDatabase + "?w=majority"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        client = MongoClients.create(settings);

        database = client.getDatabase(mongoDatabase);


        responses = database.getCollection("responses");
        requests = database.getCollection("requests");
        nodes = database.getCollection("nodes");
        clientServers = database.getCollection("clientServers");
        storageServers = database.getCollection("storageServers");
        files = database.getCollection("files");
    }

    public void clear() {
        responses.drop();
        requests.drop();
        clientServers.drop();
        storageServers.drop();
        files.drop();
    }

    public MongoCollection<Document> getResponses() {
        return responses;
    }

    public MongoCollection<Document> getRequests() {
        return requests;
    }

    public MongoCollection<Document> getFiles() {
        return files;
    }

    public MongoCollection<Document> getClientServers() {
        return clientServers;
    }

    public MongoCollection<Document> getNodes() {
        return nodes;
    }

    public MongoCollection<Document> getStorageServers() {
        return storageServers;
    }

}
