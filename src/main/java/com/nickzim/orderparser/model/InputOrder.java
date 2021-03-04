package com.nickzim.orderparser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputOrder {

    private long orderId;
    private double amount;
    private String currency;
    private String comment;
    private String message;
}
