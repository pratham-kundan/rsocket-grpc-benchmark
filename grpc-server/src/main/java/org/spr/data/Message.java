package org.spr.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


/**
 * Mongo document representing a simple message
 */
@Document
public class Message {
    @Id
    private String id;
    private String body;

    public Message(String body) {
        this.id = UUID.randomUUID().toString();
        this.body = body;
    }

    public Message(String id, String body) {
        this.id = id;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
