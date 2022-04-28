package ui;

import javax.swing.*;
import java.awt.*;

/*
 *  Panel with customization settings for a pomodoro
 */
public class SettingsPanel extends JPanel {
    private JCheckBox loopCheckBox;
    private JTextField lengthWorkField;
    private JTextField lengthShortBreakField;
    private JTextField lengthLongBreakField;
    private JTextField numRepsField;

    private JLabel loopCheckLabel;
    private JLabel lengthWorkLabel;
    private JLabel lengthShortBreakLabel;
    private JLabel lengthLongBreakLabel;
    private JLabel numRepsFieldLabel;

    public SettingsPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        initFields();
        setup();
    }

    // EFFECTS: helper for constructor; initializes fields
    private void initFields() {
        loopCheckBox = new JCheckBox();
        lengthWorkField = new JTextField();
        lengthShortBreakField = new JTextField();
        lengthLongBreakField = new JTextField();
        numRepsField = new JTextField();

        loopCheckLabel = new JLabel("loop indefinitely");
        lengthWorkLabel = new JLabel("length of work period (minutes): ");
        lengthShortBreakLabel = new JLabel("length of short break (minutes): ");
        lengthLongBreakLabel = new JLabel("length of long break (minutes): ");
        numRepsFieldLabel = new JLabel("number of repetitions: ");
    }

    // EFFECTS: helper for constructor; sets up GUI components
    private void setup() {
        JPanel loopCheckPanel = new JPanel();
        loopCheckPanel.setLayout(new FlowLayout());
        loopCheckPanel.add(loopCheckBox);
        loopCheckPanel.add(loopCheckLabel);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridLayout(4,2));
        textFieldPanel.add(lengthWorkLabel);
        textFieldPanel.add(lengthWorkField);
        textFieldPanel.add(lengthShortBreakLabel);
        textFieldPanel.add(lengthShortBreakField);
        textFieldPanel.add(lengthLongBreakLabel);
        textFieldPanel.add(lengthLongBreakField);
        textFieldPanel.add(numRepsFieldLabel);
        textFieldPanel.add(numRepsField);

        this.add(loopCheckPanel);
        this.add(textFieldPanel);
    }

    // REQUIRES: f is a field of this
    // MODIFIES: f
    // EFFECTS: returns true and removes highlight if field can be converted to an integer;
    //          returns false and highlights field otherwise
    private boolean fieldValid(JTextField f) {
        String input = f.getText();
        if (input.length() == 0) {
            return true;
        }

        try {
            Integer.parseInt(input);
            f.setBackground(Color.white);
            return true;
        } catch (NumberFormatException ex) {
            f.setBackground(Color.yellow);
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: for all fields in this,
    //              removes highlight if field can be converted to an integer; highlights field otherwise
    //          return true if all fields can be converted to an integer, false otherwise
    public boolean allInputsValid() {
        boolean f1 = fieldValid(lengthWorkField);
        boolean f2 = fieldValid(lengthShortBreakField);
        boolean f3 = fieldValid(lengthLongBreakField);
        boolean f4 = fieldValid(numRepsField);

        return f1 && f2 && f3 && f4;
    }

    // REQUIRES: input is empty or can be parsed to an int
    // EFFECTS: returns the value inputted by the user or the default work length if no value was entered
    public int getLengthWork () {
        String input = lengthWorkField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_WORK_DURATION;
        } else {
            return Integer.parseInt(input)*60;
        }
    }

    // REQUIRES: input is empty or can be parsed to an int
    // EFFECTS: returns the value inputted by the user or the default short break length if no value was entered
    public int getLengthShortBreak() {
        String input = lengthShortBreakField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_SHORT_BREAK_DURATION;
        } else {
            return Integer.parseInt(input)*60;
        }
    }

    // REQUIRES: input is empty or can be parsed to an int
    // EFFECTS: returns the value inputted by the user or the default long break length if no value was entered
    public int getLengthLongBreak() {
        String input = lengthLongBreakField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_LONG_BREAK_DURATION;
        } else {
            return Integer.parseInt(input)*60;
        }
    }

    // REQUIRES: input is empty or can be parsed to an int
    // EFFECTS: returns the value inputted by the user or the default number of reps if no value was entered
    public int getNumReps() {
        String input = numRepsField.getText();
        if (input.length() == 0) {
            return PomodoroApp.DEFAULT_NUM_REPS;
        } else {
            return Integer.parseInt(input);
        }
    }

    // EFFECTS: returns true if the user has indicated that the pomodoro should loop infinitely, false otherwise
    public boolean infinite() {
        return loopCheckBox.isSelected();
    }
}
