package com.nickzim.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OutputOrder extends InputOrder {

    private String filename;
    private long line;
    private String result;

    public OutputOrder(long orderId, double amount, String currency, String comment, String filename, long line, String result) {
        super(orderId, amount, currency, comment);
        this.filename = filename;
        this.line = line;
        this.result = result;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
