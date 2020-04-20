package net.xilla.backupcore.api.database;

import net.xilla.backupcore.BackupCore;
import net.xilla.backupcore.api.Log;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoDocument{

    private Document document;
    private boolean loaded;
    private String origin;
    private String destination;
    private ObjectId mongoID;

    public MongoDocument(Document doc, String origin, String destination) {
        this.loaded = false;
        this.mongoID = doc.getObjectId("_id");
        this.origin = origin;
        this.destination = destination;
        doc.remove("_id");
        this.document = doc;
    }

    public MongoDocument(Document doc) {
        this.loaded = doc.getBoolean("loaded");
        this.origin = doc.getString("origin");
        this.destination = doc.getString("destination");
        this.mongoID = doc.getObjectId("_id");
        doc.remove("_id");
        doc.remove("loaded");
        doc.remove("origin");
        doc.remove("destination");
        this.document = doc;
    }

    public MongoDocument(String destination) {
        this.loaded = false;
        this.document = new Document();
        this.origin = BackupCore.getInstance().getNodeID();
        this.destination = destination;
    }

    public Document getFinalDocument() {
        return document
                .append("loaded", loaded)
                .append("origin", origin)
                .append("destination", destination);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }

    public ObjectId getMongoID() {
        return mongoID;
    }

}
