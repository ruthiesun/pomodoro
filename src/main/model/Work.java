package model;

import java.util.TimerTask;

// Timer for work period in a Pomodoro
public class Work extends TimerTask {
    @Override
    public void run() {
        System.out.println("working...");
    }
}
