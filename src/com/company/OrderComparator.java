package com.company;

import java.util.Comparator;
import java.util.Map;

public class OrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order lhs, Order rhs) {
        if (lhs.price < rhs.price){
            return -1;
        }
        if (lhs.price > rhs.price){
            return 1;
        }
        return (int) (lhs.uid - rhs.uid);
    }
}
