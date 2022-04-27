package ui;

import model.Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class TimerPanel implements ActionListener, Observer{
    JPanel panel;
    JTextField time;
    JTextField message;
    JButton button;

    public TimerPanel() {
        super();
        initFields();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(time);
        panel.add(message);
        panel.add(button);
    }

    private void initFields() {
        time = new JTextField("time");
        message = new JTextField("message");
        button = new JButton("button");
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((Status) arg) {
            case DONE_WORK:
                System.out.println("update timer iwth break stuff");
                break;
            case DONE_BREAK:
                System.out.println("update timer wtih work stuff");
                break;
        }
    }
}
