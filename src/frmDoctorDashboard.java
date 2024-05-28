import javax.swing.*;

public class frmDoctorDashboard extends JFrame {
    private static frmDoctorDashboard instance;
    private JButton logOutButton;
    private JTextArea recentArea;
    private JButton postButton;
    private JLabel titleLabel;
    private JPanel panel;
    private JLabel insLabel;
    private JLabel upcomingLabel;
    private JScrollPane recentPane;
    private JTextField priceField;
    private JTextField dateField;
    private JTextField healthField;
    private JTextField bookingField;
    private JLabel fillLabel;
    private JLabel dateLabel;
    private JLabel priceLabel;
    private JLabel copyrightLabel;
    private JButton resetPwdButton;
    private JButton deleteAllButton;
    private JButton delistHealingButton;
    private JButton updateButton;
    private JLabel bookingLabel;
    private JLabel healthLabel;
    private String idHealing;

    private frmDoctorDashboard() {
        setContentPane(panel);
        setTitle("UNIVERSITY HEALTH CARE - Doctor Dashboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        logOutButton.addActionListener(
                e -> {
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to log out?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        logOutButton.setEnabled(false);
                        SwingWorker<Void, Void> worker =
                                new SwingWorker<>() {
                                    @Override
                                    protected Void doInBackground() {
                                        try {
                                            setVisible(false);
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Logged out successfully! ",
                                                    "Success",
                                                    JOptionPane.WARNING_MESSAGE);
                                            Thread.sleep(1000);
//                                            System.exit(0);
                                            frmHome.getInstance().setVisible(true);
                                            setVisible(false);
                                        } catch (InterruptedException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void done() {
                                        logOutButton.setEnabled(true);
                                    }
                                };
                        worker.execute();
                    }
                });
        resetPwdButton.addActionListener(
                e -> {
                    JPasswordField oldPwd = new JPasswordField();
                    JPasswordField newPwd = new JPasswordField();
                    Object[] message = {"Old password: ", oldPwd, "New password: ", newPwd};
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    message,
                                    "Changing Password",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    boolean conditionCheck =
                            !String.valueOf(oldPwd.getPassword()).equals(String.valueOf(newPwd.getPassword()));
                    if ((option == JOptionPane.YES_OPTION) && conditionCheck) {
                        resetPwdButton.setEnabled(false);
                        SwingWorker<Void, Void> worker =
                                new SwingWorker<>() {
                                    @Override
                                    protected Void doInBackground() {
                                        if (ConnectSQL.submitPasswordUpdate(
                                                frmHome.getInstance().getCredentials()[0],
                                                String.valueOf(oldPwd.getPassword()),
                                                String.valueOf(newPwd.getPassword()))) {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Password changed successfully!",
                                                    "Success",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                            oldPwd.setText("");
                                            newPwd.setText("");

                                        } else {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "The old password is incorrect!",
                                                    "Warning",
                                                    JOptionPane.WARNING_MESSAGE);
                                            oldPwd.setText("");
                                            newPwd.setText("");
                                            resetPwdButton.setEnabled(true);
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void done() {
                                        resetPwdButton.setEnabled(true);
                                    }
                                };
                        worker.execute();
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "The new password must different from old one!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                });
        deleteAllButton.addActionListener(
                e -> {
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to delete all ?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        deleteAllButton.setEnabled(false);
                        SwingWorker<Void, Void> worker =
                                new SwingWorker<>() {
                                    @Override
                                    protected Void doInBackground() {
                                        dateField.setText("");
                                        priceField.setText("");
                                        healthField.setText("");
                                        bookingField.setText("");
                                        return null;
                                    }

                                    @Override
                                    protected void done() {
                                        deleteAllButton.setEnabled(true);
                                    }
                                };
                        worker.execute();
                    }
                });

        postButton.addActionListener(
                e -> {
                    postButton.setEnabled(false);
                    if (dateField.getText().isEmpty()
                            || bookingField.getText().isEmpty()
                            || priceField.getText().isEmpty()
                            || healthField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                null, "Field(s) are empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                        postButton.setEnabled(true);
                        return;
                    }
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to post appointment information?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        SwingWorker<Void, Void> worker =
                                new SwingWorker<>() {
                                    @Override
                                    protected Void doInBackground() {
                                        if (ConnectSQL.submitHealingUpdate(
                                                frmHome.getInstance().getID()[0],
                                                dateField.getText(),
                                                healthField.getText(),
                                                bookingField.getText(),
                                                priceField.getText())) {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Appointment information posted! ",
                                                    "Success",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                            dateField.setText("");
                                            healthField.setText("");
                                            bookingField.setText("");
                                            priceField.setText("");
                                            recentArea.selectAll();
                                            recentArea.replaceSelection("");
                                            recentArea.setText(
                                                    ConnectSQL.showDoctorBookingQuery(frmHome.getInstance().getID()[0]));
                                        } else {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Appointment information cannot be posted. Please try again!",
                                                    "Warning",
                                                    JOptionPane.WARNING_MESSAGE);
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void done() {
                                        postButton.setEnabled(true);
                                    }
                                };
                        worker.execute();
                    }
                });
        delistHealingButton.addActionListener(
                e -> {
                    String idHeal =
                            JOptionPane.showInputDialog(
                                    null,
                                    "Enter the appointment ID you want to delist: ",
                                    "Cancellation",
                                    JOptionPane.INFORMATION_MESSAGE);
                    if (idHeal != null && !idHeal.isBlank() ) {
                        int option =
                                JOptionPane.showConfirmDialog(
                                        null,
                                        "Are you sure you want to delist appointment information with ID: " + idHeal + "?",
                                        "Confirm",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                        if (option == JOptionPane.YES_OPTION) {
                            delistHealingButton.setEnabled(false);
                            SwingWorker<Void, Void> worker =
                                    new SwingWorker<>() {
                                        @Override
                                        protected Void doInBackground() {
                                            if (ConnectSQL.delistHealingUpdate(
                                                    frmHome.getInstance().getID()[0], idHeal)) {
                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Appointment with ID: "
                                                                + idHeal
                                                                + " Delisted.",
                                                        "Success",
                                                        JOptionPane.INFORMATION_MESSAGE);
                                                recentArea.selectAll();
                                                recentArea.replaceSelection("");
                                                recentArea.setText(
                                                        ConnectSQL.showDoctorBookingQuery(
                                                                frmHome.getInstance().getID()[0]));
                                            } else {
                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Cannot delist appointment with ID: "
                                                                + idHeal
                                                                + """
                                !""",
                                                        "Warning",
                                                        JOptionPane.WARNING_MESSAGE);
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void done() {
                                            delistHealingButton.setEnabled(true);
                                        }
                                    };
                            worker.execute();
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                null, "Nothing to delisted!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                });
        updateButton.addActionListener(
                e -> {
                    idHealing =
                            JOptionPane.showInputDialog(
                                    null,
                                    "Enter the appointment ID you want to update: ",
                                    "Cancellation",
                                    JOptionPane.INFORMATION_MESSAGE);
                    if (idHealing != null && !idHealing.isBlank() ) {
                        int option =
                                JOptionPane.showConfirmDialog(
                                        null,
                                        "Are you sure you want to update appointment information with ID: " + idHealing + "?",
                                        "Confirm",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                        if (option == JOptionPane.YES_OPTION) {
                            updateButton.setEnabled(false);
                            SwingWorker<Void, Void> worker =
                                    new SwingWorker<>() {
                                        @Override
                                        protected Void doInBackground() {
                                            if (ConnectSQL.checkID(
                                                    frmHome.getInstance().getID()[0], idHealing)) {
                                                frmUpdate.getInstance().setVisible(true);
                                                setVisible(false);

                                                recentArea.selectAll();
                                                recentArea.replaceSelection("");
                                                recentArea.setText(
                                                        ConnectSQL.showDoctorBookingQuery(
                                                                frmHome.getInstance().getID()[0]));
                                            } else {
                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Cannot update appointment with ID: "
                                                                + idHealing
                                                                + """
                                !""",
                                                        "Warning",
                                                        JOptionPane.WARNING_MESSAGE);
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void done() {
                                            updateButton.setEnabled(true);
                                        }
                                    };
                            worker.execute();
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                null, "Nothing to updating!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                });

}
    public static synchronized frmDoctorDashboard getInstance() {
        if (instance == null) {
            instance = new frmDoctorDashboard();
        }
        return instance;
    }
    public String getIdHealing() {
        return idHealing;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (isVisible()) {
            insLabel.setText(
                    "Welcome back, "
                            + ConnectSQL.showNameQuery(
                            frmHome.getInstance().getID()[0], frmHome.getInstance().getID()[1])
                            + "!");
            recentArea.selectAll();
            recentArea.replaceSelection("");
            recentArea.setText(ConnectSQL.showDoctorBookingQuery(frmHome.getInstance().getID()[0]));
            recentArea.setEditable(false);
        }
    }
}
