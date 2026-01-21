package com.tracker;

import com.tracker.gui.ExpenseTrackerGUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() ->
                new ExpenseTrackerGUI().setVisible(true)
        );
    }
}
