import json.*;
import model.OrderModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.OrderBookFrame;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.times;

public class ControllerTest {

    private OrderModel buyOrderModel;
    private OrderModel sellOrderModel;
    private  OrderBookFrame ui;
    private Controller controller;

    @Before
    public void setup() {
        buyOrderModel = Mockito.mock(OrderModel.class);
        sellOrderModel = Mockito.mock(OrderModel.class);
        ui = Mockito.mock(OrderBookFrame.class);

        controller = new Controller(0, buyOrderModel, sellOrderModel, ui);

    }

    @Test
    public void snapshot() {
        Ask ask1 = new Ask(0.0d, 0.d);
        Ask ask2 = new Ask(0.0d, 0.d);
        Ask ask3 = new Ask(0.0d, 0.d);
        Ask ask4 = new Ask(0.0d, 0.d);
        Bid bid1 = new Bid(0.0d, 0.d);
        Bid bid2 = new Bid(0.0d, 0.d);
        Bid bid3 = new Bid(0.0d, 0.d);
        Bid bid4 = new Bid(0.0d, 0.d);

        Snapshot snapshot = new Snapshot("", Arrays.asList(ask1, ask2, ask3, ask4), Arrays.asList(bid1, bid2, bid3, bid4));
        controller.onSnapshot(snapshot);
        Mockito.verify(sellOrderModel).updateOrders(ask1);
        Mockito.verify(sellOrderModel).updateOrders(ask2);
        Mockito.verify(sellOrderModel).updateOrders(ask3);
        Mockito.verify(sellOrderModel).updateOrders(ask4);
        Mockito.verify(buyOrderModel).updateOrders(bid1);
        Mockito.verify(buyOrderModel).updateOrders(bid2);
        Mockito.verify(buyOrderModel).updateOrders(bid3);
        Mockito.verify(buyOrderModel).updateOrders(bid4);
        Mockito.verify(ui).refresh(buyOrderModel, sellOrderModel);
    }

    @Test
    public void l2update() {
        Ask ask1 = new Ask(0.0d, 0.0d);
        Ask ask2 = new Ask(0.0d, 1.0d);
        Ask ask3 = new Ask(0.0d, 2.0d);
        Ask ask4 = new Ask(0.0d, 3.0d);
        Bid bid1 = new Bid(0.0d, 4.0d);
        Bid bid2 = new Bid(0.0d, 5.0d);
        Bid bid3 = new Bid(0.0d, 6.0d);
        Bid bid4 = new Bid(0.0d, 7.0d);

        L2Update update = new L2Update("", Arrays.asList(ask1, bid1, ask2, bid2, ask3, bid3, ask4, bid4));
        controller.onL2Update(update);
        ArgumentCaptor<Order> sellOrderCaptor = ArgumentCaptor.forClass(Order.class);
        ArgumentCaptor<Order> buyOrderCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(sellOrderModel, times(4)).updateOrders(sellOrderCaptor.capture());
        Mockito.verify(buyOrderModel, times(4)).updateOrders(buyOrderCaptor.capture());

        List<Order> asks = sellOrderCaptor.getAllValues();
        List<Order> bids = buyOrderCaptor.getAllValues();

        assertEquals(4, asks.size());
        assertEquals(4, bids.size());

        assertEquals(0.0d, asks.get(0).size());
        assertEquals(1.0d, asks.get(1).size());
        assertEquals(2.0d, asks.get(2).size());
        assertEquals(3.0d, asks.get(3).size());

        assertEquals(4.0d, bids.get(0).size());
        assertEquals(5.0d, bids.get(1).size());
        assertEquals(6.0d, bids.get(2).size());
        assertEquals(7.0d, bids.get(3).size());

        Mockito.verify(ui).refresh(buyOrderModel, sellOrderModel);
    }
}
