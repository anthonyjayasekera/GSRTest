import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.Event;
import json.L2Update;
import json.Snapshot;
import json.Subscriptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

public class Connection implements Runnable, Consumer<String[]> {

    private static Logger LOG = LoggerFactory.getLogger(Connection.class);

    public static void main(String[] args) {
        new Thread(new Connection("ETH-EUR", null)).start();
        Scanner sysIn = new Scanner(System.in);  // Create a Scanner object
        String input = sysIn.nextLine();
    }

    private String productId;
    private Optional<Events> eventHandler;

    public Connection(String productId, Events eventHandler) {
        this.productId = productId;
        this.eventHandler = Optional.ofNullable(eventHandler);
    }

    public void run() {
        accept(new String[]{"wss://ws-feed.pro.coinbase.com/"});
    }
    public void accept(String[] args) {

        String endpoint = args[0];
        LOG.info("Connecting To [{}]", endpoint);
        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(new StandardWebSocketClient(), new Handler(), endpoint);
        connectionManager.start();

    }


    private class Handler extends TextWebSocketHandler {

        private Gson gson;

        public Handler() {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Event.class, new Event.Deserializer());
            gson = gsonBuilder.create();
        }

        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) {
            LOG.info("Message Received [" + message.getPayload() + "]");
            Event event = null;
            try {
                event = gson.fromJson(message.getPayload(), Event.class);
            }
            catch(Throwable error){
                eventHandler.ifPresent(it -> it.onReadError(error));
            }

            if(event != null) {
                final Event finalEvent = event;
                switch(event.type()) {
                    case snapshot:
                        eventHandler.ifPresent(it -> it.onSnapshot((Snapshot)finalEvent));
                        break;
                    case l2update:
                        eventHandler.ifPresent(it -> it.onL2Update((L2Update) finalEvent));
                        break;
                    case subscriptions:
                        eventHandler.ifPresent(it -> it.onSubscribed((Subscriptions) finalEvent));
                        break;
                }
            }
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            LOG.info("Connected");
            String payload =    "{" +
                                    "\"type\": \"subscribe\"," +
                                    "\"product_ids\": [" +
                                        "\""+productId+"\"" +
                                    "]," +
                                    "\"channels\":[" +
                                        "\"level2\"" +
                                    "]" +
                                "}";
            LOG.info("Sending [" + payload + "]");
            session.sendMessage(new TextMessage(payload));
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) {
            LOG.error("Transport Error", exception);
            eventHandler.ifPresent(it -> it.onTransportError(exception));
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
            LOG.info("Connection Closed [" + status.getReason() + "]");
        }
    }

    public interface Events {
        public void onSnapshot(Snapshot snapshotEvent);
        public void onL2Update(L2Update l2UpdateEvent);
        public void onSubscribed(Subscriptions subscriptionsEvent);
        public void onReadError(Throwable error);
        public void onTransportError(Throwable error);
    }
}
