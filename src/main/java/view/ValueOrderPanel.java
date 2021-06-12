package view;

public class ValueOrderPanel extends OrderPanel {


    public void setPrice(double price) {
        this.price.setText(Double.toString(price));
    }

    public void setAmount(double amount) {
        this.amount.setText(Double.toString(amount));
    }



}
