package com.fs11.tiner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Long id;
    private Long date_in_millis;
    private Long from_id;
    private Long to_id;
    private String message_body;
}
