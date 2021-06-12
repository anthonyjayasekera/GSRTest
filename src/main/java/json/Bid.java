package json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Bid extends Order {

    public Bid(double price, double size) {
        super(Side.buy, price, size);
    }

    public static class Deserializer implements JsonDeserializer<Bid> {

        @Override
        public Bid deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray tuple = json.getAsJsonArray();
            return new Bid(tuple.get(0).getAsDouble(), tuple.get(1).getAsDouble());
        }
    }

}
