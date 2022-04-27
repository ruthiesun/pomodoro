package model;

import java.util.TimerTask;

/*
 *  Abstract class for work and break periods
 */
public abstract class TimerTaskBase extends TimerTask {
    private int counter;

    // EFFECTS: initialization
    public TimerTaskBase(int counter) {
        super();
        this.counter = counter;
    }

    protected abstract void displayTime();

    protected abstract void displayNotif();

    @Override
    public void run() {
        if (counter==0) {
            displayNotif();
        } else if (counter>0){
            displayTime();
            counter--;
        }
        // else do nothing (waiting for user to dismiss notif)
    }
}
