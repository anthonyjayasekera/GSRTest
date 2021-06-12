package view;

import json.Order;
import model.OrderModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import static javax.swing.BorderFactory.createLineBorder;

public class OrderBookFrame extends JFrame {

    private int noOfLevels;
    private List<BuyOrderPanel> buyOrders = new ArrayList<>();
    private List<SellOrderPanel> sellOrders = new ArrayList<>();

    public OrderBookFrame(int noOfLevels) throws HeadlessException {
        super();
        this.noOfLevels = noOfLevels;

        setLayout(new GridLayout(noOfLevels + 1,1));
        JPanel panel;

        panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        panel.add(header("BUY", new Color(0,100,0)));
        panel.add(header("SELL", new Color(180, 0, 0)));



        panel.setSize(120, 20);
        panel.setBackground(new Color(120, 120, 120));
        add(panel);


        JPanel panel1;
        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1,2));
        panel1.add(header("amount"));
        panel1.add(header("price"));
        panel1.setBackground(new Color(120, 120, 120));
        panel.add(panel1);


        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1,2));
        panel1.add(header("price"));
        panel1.add(header("amount"));
        panel1.setBackground(new Color(120, 120, 120));
        panel.add(panel1);
        add(panel);

        for(int i=0;i<noOfLevels;i++) {
            panel = new JPanel();
            BuyOrderPanel buyOrder = new BuyOrderPanel();
            buyOrders.add(buyOrder);
            panel.add(buyOrder);

            SellOrderPanel sellOrder = new SellOrderPanel();
            sellOrders.add(sellOrder);
            panel.add(sellOrder);

            panel.setBorder(createLineBorder(Color.darkGray, 0));
            panel.setBackground(new Color(120, 120, 120));
            add(panel);
        }

        pack();
        setResizable(false);
        setBackground(Color.blue);
        setOpacity(1f);
        setVisible(true);

//        java.util.Timer t = new java.util.Timer();
//        t.schedule(new TimerTask(){
//            @Override
//            public void run() {
//                SwingUtilities.invokeLater(() -> {
//                    long now = System.currentTimeMillis();
//                    long value = now % 1000;
//                    int level = (int)(now % noOfLevels);
//                    boolean isBuy = (now % 2)==0;
//                    boolean isAmount = ((now/10) % 2)==0;
//
//                    OrderPanel p = (isBuy?buyOrders:sellOrders).get(level);
//                    if(isAmount)p.setAmount(Long.toString(value));
//                    else p.setPrice(Long.toString(value));
//                });
//            }
//        }, 200, 200);
    }

    public void refresh(final OrderModel buyOrderModel, final OrderModel sellOrderModel) {
        SwingUtilities.invokeLater(() -> {

            for(int i=0;i<buyOrderModel.getUIOrders().length;i++) {
                Order order = buyOrderModel.getUIOrders()[i];
                buyOrders.get(i).setAmount(Optional.ofNullable(order).map(Order::size).map(it -> Double.toString(it)).orElse(""));
                buyOrders.get(i).setPrice(Optional.ofNullable(order).map(Order::price).map(it -> Double.toString(it)).orElse(""));
            }
            for(int i=0;i<sellOrderModel.getUIOrders().length;i++) {
                Order order = sellOrderModel.getUIOrders()[i];
                sellOrders.get(i).setAmount(Optional.ofNullable(order).map(Order::size).map(it -> Double.toString(it)).orElse(""));
                sellOrders.get(i).setPrice(Optional.ofNullable(order).map(Order::price).map(it -> Double.toString(it)).orElse(""));
            }
        });

    }

    private JLabel header(String text) {
        return header(text, Color.black);
    }

        private JLabel header(String text, Color color) {
        JLabel header = new JLabel();
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setForeground(color);
        header.setText(text);
        return header;
    }

    public static void main(String[] args) {

    }
}
