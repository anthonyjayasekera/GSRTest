import json.L2Update;
import json.Snapshot;
import json.Subscriptions;
import model.OrderModel;
import view.OrderBookFrame;

public class Controller implements Connection.Events {

    private final int updateLatency;
    private final OrderModel buyOrderModel;
    private final OrderModel sellOrderModel;
    private final OrderBookFrame ui;

    public Controller(int updateLatency, OrderModel buyOrderModel, OrderModel sellOrderModel,  OrderBookFrame ui) {
        this.updateLatency = updateLatency;
        this.buyOrderModel = buyOrderModel;
        this.sellOrderModel = sellOrderModel;
        this.ui = ui;
    }

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

}
