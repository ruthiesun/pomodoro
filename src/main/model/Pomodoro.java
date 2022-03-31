package model;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// Representation of a single pomodoro
public class Pomodoro extends TimerTask {
    private String taskName; // name of task that the pomodoro is for
    private int workDuration; // duration of work period in minutes
    private int breakDuration; // duration of break period in minutes
    private int longBreakDuration; // duration of long break period in minutes
    private int numReps; // number of work-break repetitions
    private Timer timer;
    private Work workTimer;
    private Break breakTimer;
    private boolean done;

    // EFFECTS: sets taskName to name, workDuration to 25 minutes, breakDuration to 5 minutes, and numReps to 4
    public Pomodoro(String name) {
        taskName = name;
        workDuration = 25;
        breakDuration = 5;
        longBreakDuration = 25;
        numReps = 4;
        done = false;
    }


    // MODIFIES: workDuration, breakDuration, numReps
    // EFFECTS: starts the timer
    public void start() {
        timer = new Timer();
        workTimer = new Work();
        breakTimer = new Break();

        long workMS = 500000;//workDuration * 60 * 1000;
        long breakMS = 500000;//breakDuration * 60 * 1000;

        timer.scheduleAtFixedRate(this, new Date(), (workMS + breakMS) * numReps);
        timer.scheduleAtFixedRate(workTimer, 0, breakMS);
        timer.scheduleAtFixedRate(breakTimer, workMS, workMS);
    }

    public void takeLongBreak() {
        timer.cancel();
        timer = new Timer();
        breakTimer = new LongBreak();
        System.out.println("done work-break cycles; take long break now");

        long longBreakMS = 500000;//longBreakDuration * 60 * 1000;
        timer.scheduleAtFixedRate(breakTimer, 0, longBreakMS);
        done = true;
    }

    @Override
    public void run() {
        if (!done) {
            takeLongBreak();
        }
        timer.cancel();
        System.out.println("done pomodoro");
    }
}
