package json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Optional;

public class Order {

    public enum Side{buy, sell};

    private final Side side;
    private final double price;
    private final double size;

    public Order(Side side, double price, double size) {
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public Side side() {
        return side;
    }

    public double price() {
        return price;
    }

    public Double size() {
        return size;
    }

    public boolean isBid() {
        return side() == Side.buy;
    }

    public boolean isAsk() {
        return side() == Side.sell;
    }

    public Optional<Bid> toBid() {
        return Optional.of(side()).filter(Side.buy::equals).map(it -> new Bid(price,size));
    }

    public Optional<Ask> toAsk() {
        return Optional.of(side()).filter(Side.sell::equals).map(it -> new Ask(price,size));
    }

    public static class Deserializer implements JsonDeserializer<Order> {

        @Override
        public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray tuple = json.getAsJsonArray();
            return new Order(Side.valueOf(tuple.get(0).getAsString()), tuple.get(1).getAsDouble(), tuple.get(2).getAsDouble());
        }
    }

}
