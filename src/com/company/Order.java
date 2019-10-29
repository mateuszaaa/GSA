package com.company;

import java.util.Scanner;

public class Order {
    public long uid;
    public TransactionType type;
    public short price;
    public int quantity;
    public int peak_size;

    @Override
    public int hashCode() {
        return (int) uid;
    }

    public static Order parseOrder(String str){
        return Order.parseOrder(new Scanner(str));
    }

    public static Order parseOrder(Scanner scanner){
        Order order = new Order();
        order.type = scanner.nextByte() == 'B' ? TransactionType.BUY : TransactionType.SELL;
        scanner.nextByte(); // comma
        order.uid = scanner.nextInt();
        scanner.nextByte(); // comma
        order.price= scanner.nextShort();
        scanner.nextByte(); // comma
        order.quantity = scanner.nextInt();
        return order;
    }
};

