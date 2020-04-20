package net.xilla.backupcore.api.response;

import net.xilla.backupcore.api.manager.ManagerObject;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ResponseObject extends ManagerObject {

    private String origin;
    private String destination;
    private String type;
    private Object data;

    public ResponseObject(String origin, String destination, String type, Object data, String id) {
        super(id, null);
        this.origin = origin;
        this.destination = destination;
        this.type = type;
        this.data = data;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public String getID() {
        return getKey();
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("id", getKey())
                .append("origin", origin)
                .append("destination", destination)
                .append("type", type)
                .append("data", data);
    }

}
