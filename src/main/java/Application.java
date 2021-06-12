import json.L2Update;
import json.Snapshot;
import json.Subscriptions;
import model.OrderModel;
import view.OrderBookFrame;

import java.util.Timer;
import java.util.TimerTask;

public class Application {

    private int updateLatency = 0;
    private int size = 10;
    OrderModel buyOrderModel = new OrderModel(size, model -> model.descendingKeySet().iterator());
    OrderModel sellOrderModel = new OrderModel(size, model -> model.keySet().iterator());
    OrderBookFrame ui = new OrderBookFrame(size);


    public Application(String productId) {

        Controller controller = new Controller(updateLatency, buyOrderModel, sellOrderModel, ui);
        Connection connection = new Connection(productId, controller);



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

    public static void main(String[] args) {

        new Application(args[0]);
    }

}
