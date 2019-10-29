package com.company;

import java.util.*;

public class OrderBook {
    private Set<Order> buys = new TreeSet<>(new OrderComparator());
    private Set<Order> sels = new TreeSet<>(new OrderComparator());

    public void executeOrder(Order order){
        if(order.type == TransactionType.BUY){
            executeBuyOrder(order);
        }else{
            executeSellOrder(order);
        }

        Iterator buy_it = buys.iterator();
        Iterator sel_it = sels.iterator();

        Order buy = null;
        Order sell = null;
        while((buy_it.hasNext() || buy != null ) && (sell != null || sel_it.hasNext()) ){
            if(buy == null){
                buy = (Order) buy_it.next();
            }
            if(sell == null){
                sell = (Order) sel_it.next();
            }
            if(buy.price >= sell.price) {
                int quantity = Math.min(buy.quantity, sell.quantity);
                buy.quantity -= quantity;
                sell.quantity -= quantity;

                if (sell.quantity == 0) {
                    sel_it.remove();
                    sell = null;
                }

                if (buy.quantity == 0) {
                    buy_it.remove();
                    buy = null;
                }
            }
        }


    }

    public Order[] getSellOrders(){
        return sels.toArray(Order[]::new);
    }


    public Order[] getBuyOrders(){
        return buys.toArray(Order[]::new);
    }

    void executeSellOrder(Order order) {
        sels.add(order);
    }

    void executeBuyOrder(Order order) {
        buys.add(order);
    }
}
