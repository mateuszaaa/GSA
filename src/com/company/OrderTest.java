package com.company;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @org.junit.jupiter.api.Test
    void parseStandardBuyOrder() {
        Order order = Order.parseOrder("B,1,100,10");
        assertEquals(TransactionType.BUY, order.type);
        assertEquals(1, order.uid);
        assertEquals(100, order.price);
        assertEquals(10, order.quantity);
    }

    @org.junit.jupiter.api.Test
    void parseStandardSellOrder() {
        Order order = Order.parseOrder("S,2,200,20");
        assertEquals(TransactionType.SELL, order.type);
        assertEquals(2, order.uid);
        assertEquals(200, order.price);
        assertEquals(20, order.quantity);
    }
}