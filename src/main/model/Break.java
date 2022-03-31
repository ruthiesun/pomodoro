package model;

import java.util.TimerTask;

// Timer for break period in a Pomodoro
public class Break extends TimerTask {
    @Override
    public void run() {
        System.out.println("taking a break...");
    }
}
