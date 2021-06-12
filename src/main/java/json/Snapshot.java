package json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Snapshot extends Event {

    private final String productId;
    private final List<Ask> asks;
    private final List<Bid> bids;

    public Snapshot(String productId, List<Ask> asks, List<Bid> bids) {
        super(Event.EventType.snapshot);
        this.productId = productId;
        this.asks = asks;
        this.bids= bids;
    }

    public String productId() {
        return productId;
    }

    public List<Ask> asks() {
        return asks;
    }

    public List<Bid> bids() {
        return bids;
    }

    public static class Deserializer implements JsonDeserializer<Snapshot> {

        Ask.Deserializer askDeserializer = new Ask.Deserializer();
        Bid.Deserializer bidDeserializer = new Bid.Deserializer();

        @Override
        public Snapshot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            String productId = object.get("product_id").getAsString();

            JsonArray jsonAsks = object.get("asks").getAsJsonArray();
            Iterator<JsonElement> iterAsks = jsonAsks.iterator();
            List<Ask> asks = new ArrayList<>();
            while(iterAsks.hasNext()) {
                asks.add(askDeserializer.deserialize(iterAsks.next(), Ask.class, context));
            }

            JsonArray jsonBids = object.get("bids").getAsJsonArray();
            Iterator<JsonElement> iterBids = jsonBids.iterator();
            List<Bid> bids = new ArrayList<>();
            while(iterBids.hasNext()) {
                bids.add(bidDeserializer.deserialize(iterBids.next(), Bid.class, context));
            }

            return new Snapshot(productId, asks, bids);
        }
    }

}
