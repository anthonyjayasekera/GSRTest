package json;

import com.google.gson.*;

import java.lang.reflect.Type;

public abstract class Event {


    public enum EventType{subscriptions, snapshot, l2update}

    private final EventType type;

    protected Event(EventType type) {
        this.type = type;
    }

    public EventType type() {
        return type;
    }

    public static class Deserializer implements JsonDeserializer<Event> {

        private JsonDeserializer<Snapshot> snapshotDeserializer = new Snapshot.Deserializer();
        private JsonDeserializer<L2Update> l2UpdateDeserializer = new L2Update.Deserializer();
        @Override
        public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            EventType type = EventType.valueOf(object.get("type").getAsString());
            switch(type) {
                case snapshot:
                    return snapshotDeserializer.deserialize(json, typeOfT, context);
                case l2update:
                    return l2UpdateDeserializer.deserialize(json, L2Update.class, context);
                case subscriptions:
                    return new Subscriptions();
            }
            throw new RuntimeException("Unexpected event type");
        }
    }

}

