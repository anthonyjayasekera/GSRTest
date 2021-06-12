package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JsonDeserialisationTest {

    private Gson gson;

    @Before
    public void setup() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Event.class, new Event.Deserializer());
        gson = gsonBuilder.create();

    }

    @Test
    public void snapshot() {

        String json = "{\"type\":\"snapshot\",\"product_id\":\"ETH-EUR\"" +
                ",\"asks\":[" +
                "[\"1996.50\",\"1.90000000\"],[\"1996.53\",\"15.70547177\"],[\"1996.65\",\"1.53131486\"],[\"1996.70\",\"0.49316256\"],[\"1996.80\",\"1.00000000\"]" +
                "]" +
                ",\"bids\":[" +
                "[\"1995.67\",\"2.40517593\"],[\"1995.57\",\"2.58339910\"],[\"1995.55\",\"8.28926101\"],[\"1995.29\",\"35.11423733\"],[\"1995.28\",\"3.49376052\"]" +
                "]" +
                "}";
        Event event = gson.fromJson(json, Event.class);

        assertEquals(Event.EventType.snapshot, event.type());
        Snapshot snapshot = (Snapshot)event;
        assertEquals("ETH-EUR", snapshot.productId());
        assertEquals(5, snapshot.bids().size());
        assertEquals(5, snapshot.asks().size());
        assertOrder(snapshot.asks().get(0), 1996.50d, 1.90000000d);
        assertOrder(snapshot.asks().get(1), 1996.53d, 15.70547177d);
        assertOrder(snapshot.asks().get(2), 1996.65d, 1.53131486d);
        assertOrder(snapshot.asks().get(3), 1996.70d, 0.49316256d);
        assertOrder(snapshot.asks().get(4), 1996.80d, 1.00000000d);
        assertOrder(snapshot.bids().get(0), 1995.67d, 2.40517593d);
        assertOrder(snapshot.bids().get(1), 1995.57d, 2.58339910d);
        assertOrder(snapshot.bids().get(2), 1995.55d, 8.28926101d);
        assertOrder(snapshot.bids().get(3), 1995.29d, 35.11423733d);
        assertOrder(snapshot.bids().get(4), 1995.28d, 3.49376052d);
    }

    @Test
    public void l2update() {
        String json = "{\"type\":\"l2update\",\"product_id\":\"ETH-EUR\",\"changes\":[[\"buy\",\"2003.32\",\"0.00000234\"]],\"time\":\"2021-06-12T18:38:46.033449Z\"}";

        Event event = gson.fromJson(json, Event.class);

        assertEquals(Event.EventType.l2update, event.type());
        L2Update l2update = (L2Update) event;
        assertEquals("ETH-EUR", l2update.productId());
        assertEquals(1, l2update.changes().size());
        assertOrder(l2update.changes().get(0), 2003.32d, 0.00000234d);
    }

    private void assertOrder(Order order, double price, double amount) {
        assertEquals(price, order.price());
        assertEquals(amount, order.size());
    }

}
