package com.company;

import java.util.Comparator;
import java.util.Map;

public class OrderComparator implements Comparator<Order> {
    private final Map<Order, Integer> id_map;
    OrderComparator(Map<Order, Integer> id){
        this.id_map = id;
    }

    @Override
    public int compare(Order lhs, Order rhs) {
        if (lhs.price < rhs.price){
            return -1;
        }
        if (lhs.price > rhs.price){
            return 1;
        }
        return (int) id_map.get(lhs) - id_map.get(rhs);
    }
}
