import javax.swing.*;

public class frmStudentDashboard extends JFrame {
    private static frmStudentDashboard instance;
    private JButton logOutButton;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultArea;
    private JPanel panel;
    private JTextArea recentArea;
    private JButton resetPwdButton;
    private JPanel searchPanel;
    private JLabel recentLabel;
    private JScrollPane recentPane;
    private JLabel insLabel;
    private JButton cancelHealingButton;
    private JLabel availableLabel;
    private JButton submitButton;
    private JLabel titleLabel;

    private frmStudentDashboard() {
        setContentPane(panel);
        setTitle("UNIVERSITY HEALTH CARE - Student Dashboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        resultArea.setEditable(false);
        logOutButton.addActionListener(
                e -> {
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to log out?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
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
                                                    "Logged out successfully!",
                                                    "Success",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                            Thread.sleep(1000);
                                            System.exit(0);
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
        searchButton.addActionListener(
                e -> {
                    searchButton.setEnabled(false);
                    resultArea.setText("");
                    if (searchField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                null, "Disease name is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                        searchButton.setEnabled(true);
                        return;
                    }
                    SwingWorker<Void, Void> worker =
                            new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() {
                                    if (ConnectSQL.showSearchQuery(searchField.getText()).length() != 0) {
                                        resultArea.selectAll();
                                        resultArea.replaceSelection("");
                                        resultArea.setText(ConnectSQL.showSearchQuery(searchField.getText()));
                                        resultArea.setEditable(false);

                                    } else {
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Cannot find the disease with name: "
                                                        + searchField.getText()
                                                        + ". Please check your keyword again!",
                                                "Warning",
                                                JOptionPane.WARNING_MESSAGE);
                                    }
                                    return null;
                                }

                                @Override
                                protected void done() {
                                    searchButton.setEnabled(true);
                                }
                            };
                    worker.execute();
                });
        submitButton.addActionListener(
                e -> {
                    submitButton.setEnabled(false);
                    SwingWorker<Void, Void> worker =
                            new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() {
                                    frmBooking frmBooking = new frmBooking();
                                    frmBooking.setVisible(true);
                                    setVisible(false);
                                    return null;
                                }

                                @Override
                                protected void done() {
                                    submitButton.setEnabled(true);
                                }
                            };
                    worker.execute();
                });
        cancelHealingButton.addActionListener(
                e -> {
                    String idHeal =
                            JOptionPane.showInputDialog(
                                    null,
                                    "Enter the appointment ID you want to cancel: ",
                                    "Cancellation",
                                    JOptionPane.INFORMATION_MESSAGE);
                    if (idHeal != null && !idHeal.isBlank()) {
                        int option =
                                JOptionPane.showConfirmDialog(
                                        null,
                                        "Confirm cancel appointment with ID: " + idHeal + "?",
                                        "Confirm",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                        if (option == JOptionPane.YES_OPTION) {
                            cancelHealingButton.setEnabled(false);
                            SwingWorker<Void, Void> worker =
                                    new SwingWorker<>() {
                                        @Override
                                        protected Void doInBackground() {
                                            if (ConnectSQL.cancelHealingUpdate(
                                                    frmHome.getInstance().getID()[0], idHeal)) {
                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Appointment with ID: "
                                                                + idHeal
                                                                + " Cancelled successfully!. Check the nearby box for confirmation!",
                                                        "Success",
                                                        JOptionPane.INFORMATION_MESSAGE);
                                                recentArea.selectAll();
                                                recentArea.replaceSelection("");
                                                recentArea.setText(
                                                        ConnectSQL.showPatientBookingQuery(frmHome.getInstance().getID()[0]));
                                            } else {
                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Cannot cancel appointment with ID: "
                                                                + idHeal
                                                                + ". Please try again later!",
                                                        "Warning",
                                                        JOptionPane.WARNING_MESSAGE);
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void done() {
                                            cancelHealingButton.setEnabled(true);
                                        }
                                    };
                            worker.execute();
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                null, "Nothing to cancel!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                });
    }

    public static synchronized frmStudentDashboard getInstance() {
        if (instance == null) {
            instance = new frmStudentDashboard();
        }
        return instance;
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
            recentArea.setText(ConnectSQL.showPatientBookingQuery(frmHome.getInstance().getID()[0]));
            recentArea.setEditable(false);
        }
    }
}
