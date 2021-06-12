import json.L2Update;
import json.Snapshot;
import json.Subscriptions;
import model.OrderModel;
import view.OrderBookFrame;

import java.util.Timer;
import java.util.TimerTask;

public class Application {

    public Application() {
    }

    public static void main(String[] args) {
        int updateLatency = 0;
        int size = 10;
        String productId = args[0];
        OrderModel buyOrderModel = new OrderModel(size, model -> model.descendingKeySet().iterator());
        OrderModel sellOrderModel = new OrderModel(size, model -> model.keySet().iterator());
        OrderBookFrame ui = new OrderBookFrame(size);

        Connection connection = new Connection(productId, new Connection.Events() {
            @Override
            public void onSnapshot(Snapshot snapshotEvent) {
                snapshotEvent.bids().stream().forEach(buyOrderModel::updateOrders);
                snapshotEvent.asks().stream().forEach(sellOrderModel::updateOrders);
                if(updateLatency == 0) {
                    ui.refresh(buyOrderModel,sellOrderModel);
                }
            }

            @Override
            public void onL2Update(L2Update l2UpdateEvent) {
                l2UpdateEvent.bids().stream().forEach(buyOrderModel::updateOrders);
                l2UpdateEvent.asks().stream().forEach(sellOrderModel::updateOrders);
                if(updateLatency == 0) {
                    ui.refresh(buyOrderModel,sellOrderModel);
                }
            }

            @Override
            public void onSubscribed(Subscriptions subscriptionsEvent) {

            }

            @Override
            public void onReadError(Throwable error) {

            }

            @Override
            public void onTransportError(Throwable error) {

            }
        });
        new Thread(connection).start();
        if(updateLatency > 0) {
            Timer refresher = new Timer();
            refresher.schedule(new TimerTask(){
                @Override
                public void run() {
                    ui.refresh(buyOrderModel,sellOrderModel);
                }
            }, updateLatency, updateLatency);
        }
    }

}
