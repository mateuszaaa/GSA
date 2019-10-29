package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {
private OrderBook book = new OrderBook();
    private Order createSellOrder(int quantity, int price) {
        Order sell_order = new Order();
        sell_order.uid =buy_order_id++;
        sell_order.type = TransactionType.SELL;
        sell_order.quantity = quantity;
        sell_order.price = (short) price;
        sell_order.peak_size = quantity;
        return sell_order;
    }

    private Order createIcebergSellOrder(int quantity, int price, int peak_size) {
        Order sell_order = new Order();
        sell_order.uid = buy_order_id++;
        sell_order.type = TransactionType.SELL;
        sell_order.quantity = quantity;
        sell_order.price = (short) price;
        sell_order.peak_size = peak_size;
        return sell_order;
    }

    private Order createIcebergBuyOrder(int quantity, int price, int peak_size) {
        Order buy_order = new Order();
        buy_order.uid =buy_order_id++;
        buy_order.type = TransactionType.BUY;
        buy_order.quantity = quantity;
        buy_order.price = (short) price;
        buy_order.peak_size = peak_size;
        return buy_order;
    }

    private Order createBuyOrder(int quantity, int price) {
        Order buy_order = new Order();
        buy_order.uid =buy_order_id++;
        buy_order.type = TransactionType.BUY;
        buy_order.quantity = quantity;
        buy_order.price = (short) price;
        buy_order.peak_size = quantity;
        return buy_order;
    }

    private int buy_order_id = 0;
    private int sell_order_id = 0;

    @Test
    void initialOrdersStatus() {
        assertTrue(book.getBuyOrders().length == 0);
        assertTrue(book.getSellOrders().length == 0);
    }

    @Test
    void test_insert_single_buy_order() {
        Order order = new Order();
        order.type = TransactionType.BUY;
        book.executeOrder(order);
        assertEquals(book.getBuyOrders().length,1);
    }

    @Test
    void test_insert_single_sell_order() {
        Order order = new Order();
        order.type = TransactionType.SELL;
        book.executeOrder(order);
        assertEquals(book.getSellOrders().length,1);
    }

    @Test
    void consume_buy_order() {
        Order buy_order = createBuyOrder(1,1);
        Order sell_order = createSellOrder(1,1);

        book.executeOrder(buy_order);
        book.executeOrder(sell_order);
        assertTrue(book.getSellOrders().length == 0 );
        assertTrue(book.getBuyOrders().length == 0);
    }

    @Test
    void consume_buy_order_with_price_greater_than_sell() {
        Order buy_order = createBuyOrder(1,2);
        Order sell_order = createSellOrder(1,1);

        book.executeOrder(buy_order);
        book.executeOrder(sell_order);
        assertTrue(book.getSellOrders().length == 0);
        assertTrue(book.getBuyOrders().length == 0 );
    }

    @Test
    void do_not_consume_buy_order_with_price_lower_than_sell() {
        Order buy_order = createBuyOrder(1,1);
        Order sell_order = createSellOrder(1,2);

        book.executeOrder(buy_order);
        book.executeOrder(sell_order);
        assertFalse(book.getSellOrders().length == 0);
        assertFalse(book.getBuyOrders().length == 0);
    }

    @Test
    void consume_only_part_of_buy_order() {
        Order buy_order = createBuyOrder(45, 1);
        Order sell_order = createSellOrder(23, 1);

        book.executeOrder(buy_order);
        book.executeOrder(sell_order);
        assertTrue(book.getSellOrders().length == 0);
        assertFalse(book.getBuyOrders().length == 0);
        assertTrue(book.getBuyOrders()[0].quantity == buy_order.quantity - sell_order.quantity);
    }

    @Test
    void consume_lower_price_buy_order_first() {
        Order buy_order = createBuyOrder(1, 15);
        Order cheaper_buy_order = createBuyOrder(1, 10);

        Order sell_order = createSellOrder(1, 10);
        book.executeOrder(cheaper_buy_order);
        book.executeOrder(buy_order);
        book.executeOrder(sell_order);


        assertTrue(book.getBuyOrders().length == 1);
        assertEquals(book.getBuyOrders()[0], buy_order);
    }

    @Test
    void consume_lower_price_sell_order_first() {
        Order buy_order = createBuyOrder(1, 15);

        Order sell_order = createSellOrder(1, 10);
        Order expensive_sell_order = createSellOrder(1, 15);
        book.executeOrder(sell_order);
        book.executeOrder(expensive_sell_order);
        book.executeOrder(buy_order);


        assertTrue(book.getSellOrders().length == 1);
        assertEquals(book.getSellOrders()[0], expensive_sell_order);
    }

    @Test
    void consume_buy_order_with_icebergsell_order() {
        Order buy_order = createBuyOrder(1, 15);
        Order sell_order = createIcebergSellOrder(1, 10,1);

        book.executeOrder(buy_order);
        book.executeOrder(sell_order);


        assertTrue(book.getSellOrders().length == 0);
        assertTrue(book.getBuyOrders().length == 0);
    }


    @Test
    void consume_few_buy_orders_with_icebergsell_order() {
        Order first_buy_order = createBuyOrder(1, 15);
        Order second_buy_order = createBuyOrder(1, 15);

        book.executeOrder(first_buy_order);
        book.executeOrder(second_buy_order);


        assertEquals(2,book.getBuyOrders().length);
    }

    @Test
    void execute_sell_iceberg_order_bigger_than_avaible_buy_orders() {
        Order first_buy_order = createBuyOrder(10, 10);
        Order second_buy_order = createBuyOrder(10, 10);
        Order sell_order = createIcebergSellOrder(30, 10,10);

        book.executeOrder(first_buy_order);
        book.executeOrder(second_buy_order);
        book.executeOrder(sell_order);

        assertTrue(book.getBuyOrders().length == 0);
        assertEquals(1, book.getSellOrders().length);
        assertEquals(10, book.getSellOrders()[0].quantity);
    }

    @Test
    void consume_iceberg_orders_in_proper_order() {
        Order buy_order = createBuyOrder(10, 10);
        Order sell_order_first = createIcebergSellOrder(30, 10,5);
        Order sell_order_second = createIcebergSellOrder(30, 10,3);

        book.executeOrder(sell_order_first);
        book.executeOrder(sell_order_second);
        book.executeOrder(buy_order);

        assertEquals(23, sell_order_first.quantity);
        assertEquals(27, sell_order_second.quantity);
    }



}