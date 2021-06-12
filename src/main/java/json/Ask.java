package json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Ask extends Order {

    public Ask(double price, double size) {
        super(Side.sell, price, size);
    }

    public static class Deserializer implements JsonDeserializer<Ask> {

        @Override
        public Ask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray tuple = json.getAsJsonArray();
            return new Ask(tuple.get(0).getAsDouble(), tuple.get(1).getAsDouble());
        }
    }



}
