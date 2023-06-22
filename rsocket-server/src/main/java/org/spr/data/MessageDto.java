package org.spr.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A simple Message Data transfer Object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String id;
    private String body;

    public String toString() {
        return String.format("<<Message -> id: %s, body: %s>>", this.id, this.body);
    }
}