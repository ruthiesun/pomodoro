package model;

// Manages pomodoros
public class PomodoroApp {
    private Pomodoro pomodoro;

    public PomodoroApp() {
        pomodoro = new Pomodoro("temp task name");
        pomodoro.start();
        pomodoro.run();
    }

}
