package view;

public class OrderPanelHeader extends OrderPanel {

    protected OrderPanelHeader(){
        super();
        setAmount("Amount");
        setPrice("Price");
    }

    public static class Buy extends OrderPanelHeader {

        public Buy() {
            super();
            add(amount);
            add(price);
        }
    }

    public static class Sell extends OrderPanelHeader {

        public Sell() {
            super();
            add(price);
            add(amount);
        }
    }
}
