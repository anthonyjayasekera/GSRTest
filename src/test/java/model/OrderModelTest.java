package model;

import json.Bid;
import json.Order;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class OrderModelTest {

    @Test
    public void orderPreserved() {
        OrderModel model = new OrderModel(10, it -> it.keySet().iterator());

        model.updateOrders(new Bid(200d, 1.0001d));
        model.updateOrders(new Bid(195d, 2.0001d));
        model.updateOrders(new Bid(190d, 3.0001d));
        model.updateOrders(new Bid(190d, 6.0001d));
        model.updateOrders(new Bid(295d, 4.0001d));
        model.updateOrders(new Bid(200d, 1.5201d));
        model.updateOrders(new Bid(395d, 5.0001d));
        model.updateOrders(new Bid(95d, 6.0001d));
        model.updateOrders(new Bid(65d, 4.0001d));
        model.updateOrders(new Bid(395d, 3.0001d));
        model.updateOrders(new Bid(5d, 2.0001d));
        model.updateOrders(new Bid(25d, 1.0001d));
        model.updateOrders(new Bid(55d, 3.0001d));
        model.updateOrders(new Bid(200d, 1.5001d));

        assertOrder(model.getUIOrders()[0],  5d,  2.0001d);
        assertOrder(model.getUIOrders()[1],  25d,  1.0001d);
        assertOrder(model.getUIOrders()[2],  55d,  3.0001d);
        assertOrder(model.getUIOrders()[3],  65d,  4.0001d);
        assertOrder(model.getUIOrders()[4],  95d,  6.0001d);
        assertOrder(model.getUIOrders()[5],  190d,  6.0001d);
        assertOrder(model.getUIOrders()[6],  195d,  2.0001d);
        assertOrder(model.getUIOrders()[7],  200d,  1.5001d);
        assertOrder(model.getUIOrders()[8],  295d,  4.0001d);
        assertOrder(model.getUIOrders()[9],  395d,  3.0001d);
    }


    @Test
    public void belowCapacity() {

        OrderModel model = new OrderModel(10, it -> it.keySet().iterator());

        model.updateOrders(new Bid(200d, 1.0001d));
        model.updateOrders(new Bid(195d, 2.0001d));
        model.updateOrders(new Bid(190d, 3.0001d));

        assertEquals(10, model.getUIOrders().length);
        assertOrder(model.getUIOrders()[0],  190d,  3.0001d);
        assertOrder(model.getUIOrders()[1],  195d,  2.0001d);
        assertOrder(model.getUIOrders()[2],  200d,  1.0001d);
        assertNull(model.getUIOrders()[3]);
        assertNull(model.getUIOrders()[4]);
        assertNull(model.getUIOrders()[5]);
        assertNull(model.getUIOrders()[6]);
        assertNull(model.getUIOrders()[7]);
        assertNull(model.getUIOrders()[8]);
        assertNull(model.getUIOrders()[9]);
    }

    @Test
    public void overCapacity() {

        OrderModel model = new OrderModel(3, it -> it.keySet().iterator());

        model.updateOrders(new Bid(200d, 1.0001d));
        model.updateOrders(new Bid(195d, 2.0001d));
        model.updateOrders(new Bid(190d, 3.0001d));
        model.updateOrders(new Bid(190d, 6.0001d));
        model.updateOrders(new Bid(295d, 4.0001d));
        model.updateOrders(new Bid(200d, 1.5201d));
        model.updateOrders(new Bid(395d, 5.0001d));
        model.updateOrders(new Bid(95d, 6.0001d));
        model.updateOrders(new Bid(65d, 4.0001d));
        model.updateOrders(new Bid(395d, 3.0001d));
        model.updateOrders(new Bid(5d, 2.0001d));
        model.updateOrders(new Bid(25d, 1.0001d));
        model.updateOrders(new Bid(55d, 3.0001d));
        model.updateOrders(new Bid(200d, 1.5001d));

        assertEquals(3, model.getUIOrders().length);
        assertOrder(model.getUIOrders()[0],  5d,  2.0001d);
        assertOrder(model.getUIOrders()[1],  25d,  1.0001d);
        assertOrder(model.getUIOrders()[2],  55d,  3.0001d);
    }

    private void assertOrder(Order order, double price, double amount) {
        assertEquals(price, order.price());
        assertEquals(amount, order.size());
    }
}
