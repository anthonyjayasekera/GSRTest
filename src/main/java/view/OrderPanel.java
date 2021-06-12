package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static javax.swing.BorderFactory.createLineBorder;

public abstract class OrderPanel extends JPanel {

    protected JLabel amount;
    protected JLabel price;

    protected OrderPanel() {
        super();
        amount = newLabel();
        price = newLabel();
        setLayout(new FlowLayout());
        setBackground(new Color(120, 120, 120));
    }

    public void setPrice(String price) {
        this.price.setText(price);
    }

    public void setAmount(String amount) {
        this.amount.setText(amount);
    }

    private JLabel newLabel() {
        JLabel label = new JLabel();
        label.setBackground(new Color(200,200,200));
        label.setOpaque(true);
        label.setBorder(new EmptyBorder(2,3,2,3));
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel outer = new JLabel(){
            @Override
            public void setText(String text) {
                label.setText(text);
            }
        };
        label.setText(" - ");

        Dimension size = new Dimension(120,20);
        outer.setMinimumSize(size);
        outer.setMaximumSize(size);
        outer.setPreferredSize(size);
        outer.setBorder(createLineBorder(Color.darkGray, 2));
        outer.setLayout(new BorderLayout());
        outer.add(label, BorderLayout.CENTER);
        return outer;
    }
}
