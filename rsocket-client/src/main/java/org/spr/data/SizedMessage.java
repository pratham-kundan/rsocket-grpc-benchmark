package org.spr.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A message object that will contain an empty byte array of required size
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizedMessage {
    byte[][] data;
}
