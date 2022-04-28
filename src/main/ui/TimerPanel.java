package ui;

import model.Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class TimerPanel extends Observable implements ActionListener {
    public static final int SECOND = 1000; // 1 second in ms

    private int counter;

    JPanel panel;
    JTextField time;
    JTextField message;
    JButton button;

    Timer timer;

    // EFFECTS: constructor
    public TimerPanel(Status status) {
        super();
        initFields();

        switch (status) {
            case WORK:
                counter = PomodoroApp.WORK_DURATION;
                break;
            case BREAK:
                counter = PomodoroApp.BREAK_DURATION;
                break;
            case LONG_BREAK:
                counter = PomodoroApp.LONG_BREAK_DURATION;
                break;
        }

        timer = new Timer(SECOND, this);
        timer.start();
    }

    // EFFECTS: helper for constructor; initializes Swing components
    private void initFields() {
        time = new JTextField(counter);
        message = new JTextField("message");
        button = new JButton("button");
        button.addActionListener(this);
        button.setVisible(false);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(time);
        panel.add(message);
        panel.add(button);
    }

    // MODFIIES:
    // EFFECTS:
    private void displayTime() {
        time = new JTextField("count: " + counter);

    }

    // MODFIIES:
    // EFFECTS:
    private void displayNotif() {
        time = new JTextField(counter);
        message = new JTextField("done this phase");
        button.setVisible(true);
    }

    // MODFIIES: counter
    // EFFECTS:
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            System.out.println("clicked button");
            setChanged();
            notifyObservers(); // tell observers that user has dismissed notif
        } else { // timer has ticked
            System.out.println(counter);
            if (counter==0) {
                displayTime();
                displayNotif();
                timer.stop();
            } else if (counter>0){
                displayTime();
                counter--;
            }
            refresh();
        }
    }

    private void refresh() {
        panel.removeAll();
        panel.add(time);
        panel.add(message);
        panel.add(button);
        panel.validate();
        panel.repaint();
    }
}
