package model;

// TimerTaskBase for work period in a Pomodoro
public class Work extends TimerTaskBase {
    public Work(int counter) {
        super(counter);
    }

    @Override
    protected void displayTime() {
        System.out.println("time to work");
    }

    @Override
    protected void displayNotif() {
        System.out.println("time to take a break");
    }

    @Override
    public void run() {
        System.out.println("working...");
        super.run();
    }
}
