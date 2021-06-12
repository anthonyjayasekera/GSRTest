package json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class L2Update extends Event {

    private final String productId;
    private final List<Order> changes;

    public L2Update(String productId, List<Order> changes) {
        super(EventType.l2update);
        this.productId = productId;
        this.changes = changes;
    }

    public String productId() {
        return productId;
    }

    public List<Order> changes() {
        return changes;
    }

    public List<Bid> bids() {
        return changes().stream().filter(Order::isBid).map(Order::toBid).map(Optional::get).collect(toList());
    }

    public List<Ask> asks() {
        return changes().stream().filter(Order::isAsk).map(Order::toAsk).map(Optional::get).collect(toList());
    }

    public static class Deserializer implements JsonDeserializer<L2Update> {

        Order.Deserializer changeDeserializer = new Order.Deserializer();

        @Override
        public L2Update deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject objects = json.getAsJsonObject();
            String productId = objects.get("product_id").getAsString();

            JsonArray jsonChanges = objects.get("changes").getAsJsonArray();
            Iterator<JsonElement> iterChanges = jsonChanges.iterator();
            List<Order> changes = new ArrayList<>();
            while(iterChanges.hasNext()) {
                changes.add(changeDeserializer.deserialize(iterChanges.next(), Order.class, context));
            }

            return new L2Update(productId, changes);
        }
    }

}
