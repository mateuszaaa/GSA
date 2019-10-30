package com.company;

import java.util.*;

public class OrderBook {
    private Map<Order, Integer> orders = new HashMap<>();
    private PriorityQueue<Order> buys = new PriorityQueue<>(new OrderComparator(orders));
    private PriorityQueue<Order> sels = new PriorityQueue<>(new OrderComparator(orders));
    private int id_counter;

    public void executeOrder(Order order){
        if(order.type == TransactionType.BUY){
            executeBuyOrder(order);
        }else{
            executeSellOrder(order);
        }


        while( ! buys.isEmpty() && ! sels.isEmpty() ){

            Order buy = buys.peek();
            Order sell = sels.peek();


           if(buy.price >= sell.price) {
                //TODO optimize removing only buy or sell
                int quantity = Math.min(Math.min(buy.peak_size, buy.quantity), Math.min(sell.peak_size, sell.quantity));
                buy.quantity -= quantity;
                sell.quantity -= quantity;

                sels.poll();
                buys.poll();

                if (sell.quantity != 0) {
                    executeSellOrder(sell);
                }

                if (buy.quantity != 0) {
                    executeBuyOrder(buy);
                }

            }else{
               break;
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
        orders.put(order,id_counter++);
        sels.add(order);
    }

    void executeBuyOrder(Order order) {
        orders.put(order,id_counter++);
        buys.add(order);
    }
}
