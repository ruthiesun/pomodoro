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
        lengthWorkField.setColumns(2);
        lengthWorkField.setText(Integer.toString(PomodoroApp.DEFAULT_WORK_DURATION/60));
        lengthShortBreakField = new JTextField();
        lengthShortBreakField.setColumns(2);
        lengthShortBreakField.setText(Integer.toString(PomodoroApp.DEFAULT_SHORT_BREAK_DURATION/60));
        lengthLongBreakField = new JTextField();
        lengthLongBreakField.setColumns(2);
        lengthLongBreakField.setText(Integer.toString(PomodoroApp.DEFAULT_LONG_BREAK_DURATION/60));
        numRepsField = new JTextField();
        numRepsField.setColumns(2);
        numRepsField.setText(Integer.toString(PomodoroApp.DEFAULT_NUM_REPS));

        loopCheckLabel = new JLabel("Loop indefinitely?");
        lengthWorkLabel = new JLabel("Length of work period (minutes): ");
        lengthShortBreakLabel = new JLabel("Length of short break (minutes): ");
        lengthLongBreakLabel = new JLabel("Length of long break (minutes): ");
        numRepsFieldLabel = new JLabel("Number of work periods before a long break: ");
    }

    // EFFECTS: helper for constructor; sets up GUI components
    private void setup() {
        JPanel loopCheckPanel = new JPanel();
        loopCheckPanel.setLayout(new FlowLayout());
        loopCheckPanel.add(loopCheckBox);
        loopCheckPanel.add(loopCheckLabel);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        textFieldPanel.add(lengthWorkLabel, gbc);
        gbc.gridy = 1;
        textFieldPanel.add(lengthShortBreakLabel, gbc);
        gbc.gridy = 2;
        textFieldPanel.add(lengthLongBreakLabel, gbc);
        gbc.gridy = 3;
        textFieldPanel.add(numRepsFieldLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        textFieldPanel.add(lengthWorkField, gbc);
        gbc.gridy = 1;
        textFieldPanel.add(lengthShortBreakField, gbc);
        gbc.gridy = 2;
        textFieldPanel.add(lengthLongBreakField, gbc);
        gbc.gridy = 3;
        textFieldPanel.add(numRepsField, gbc);

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
            return false;
        }

        try {
            int i = Integer.parseInt(input);
            if (i < 1) {
                f.setBackground(Color.yellow);
                return false;
            }
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
    // EFFECTS: returns the value inputted by the user, converted to seconds
    public int getLengthWork () {
        return 6;
        //String input = lengthWorkField.getText();
        //return Integer.parseInt(input)*60;
    }

    // REQUIRES: input is empty or can be parsed to an int
    // EFFECTS: returns the value inputted by the user, converted to seconds
    public int getLengthShortBreak() {
        return 5;
        //String input = lengthShortBreakField.getText();
        //return Integer.parseInt(input)*60;
    }

    // REQUIRES: input is empty or can be parsed to an int
    // EFFECTS: returns the value inputted by the user, converted to seconds
    public int getLengthLongBreak() {
        return 4;
        //String input = lengthLongBreakField.getText();
        //return Integer.parseInt(input)*60;
    }

    // REQUIRES: input is empty or can be parsed to an int
    // EFFECTS: returns the value inputted by the user or the default number of reps if no value was entered
    public int getNumReps() {
        String input = numRepsField.getText();
        return Integer.parseInt(input);
    }

    // EFFECTS: returns true if the user has indicated that the pomodoro should loop infinitely, false otherwise
    public boolean infinite() {
        return loopCheckBox.isSelected();
    }
}
