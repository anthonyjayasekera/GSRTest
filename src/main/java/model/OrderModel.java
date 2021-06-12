package model;

import json.Order;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

public class OrderModel {

    private TreeMap<Double, Order> orders = new TreeMap<>();
    private Order[] uiOrders;
    private Function<TreeMap<Double, Order>, Iterator<Double>> keyIteratorSupplier;

    public OrderModel(int size, Function<TreeMap<Double, Order>, Iterator<Double>> keyIteratorSupplier) {
        uiOrders = new Order[size];
        this.keyIteratorSupplier = keyIteratorSupplier;
    }

    public void updateOrders(Order newOrder) {
        if(newOrder.size() == 0d) {
            orders.remove(newOrder.price());
        }
        else {
            orders.put(newOrder.price(), newOrder);
        }
        Iterator<Double> descendingKeys = keyIteratorSupplier.apply(orders);
        for(int i=0;i<uiOrders.length && descendingKeys.hasNext();i++) {
            uiOrders[i] = orders.get(descendingKeys.next());
        }
    }

    public Order[] getUIOrders() {
        return uiOrders;
    }

    public TreeMap<Double, Order> getOrders() {
        return orders;
    }
}
