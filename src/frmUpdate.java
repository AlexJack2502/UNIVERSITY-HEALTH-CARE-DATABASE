import javax.swing.*;

public class frmUpdate extends JFrame {
    private static frmUpdate instance;
    private JButton goBackButton;
    private JTextArea recentArea;
    private JLabel titleLabel;
    private JPanel panel;
    private JLabel insLabel;
    private JLabel upcomingLabel;
    private JScrollPane recentPane;
    private JTextField priceField;
    private JTextField dateField;
    private JTextField healthField;
    private JTextField bookingField;
    private JLabel dateLabel;
    private JLabel priceLabel;
    private JButton deleteAllButton;
    private JButton updateButton;
    private JLabel bookingLabel;
    private JLabel healthLabel;

    private frmUpdate() {
        setContentPane(panel);
        setTitle("UNIVERSITY HEALTH CARE - Update page");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


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
        updateButton.addActionListener(
                e -> {
                    updateButton.setEnabled(false);
                    if (dateField.getText().isEmpty()
                            || priceField.getText().isEmpty()
                            || healthField.getText().isEmpty()
                            || bookingField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                null, "Field(s) are empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                        updateButton.setEnabled(true);
                        return;
                    }
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to update appointment information?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        SwingWorker<Void, Void> worker =
                                new SwingWorker<>() {
                                    @Override
                                    protected Void doInBackground() {
                                        if (ConnectSQL.healingUpdate(
                                                frmDoctorDashboard.getInstance().getIdHealing(),
                                                dateField.getText(),
                                                priceField.getText(),
                                                healthField.getText(),
                                                bookingField.getText())) {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Appointment information updated!",
                                                    "Success",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                            dateField.setText("");
                                            priceField.setText("");
                                            healthField.setText("");
                                            bookingField.setText("");
                                            recentArea.selectAll();
                                            recentArea.replaceSelection("");
                                            recentArea.setText(
                                                    ConnectSQL.showDoctorBookingQuery(frmHome.getInstance().getID()[0]));
                                        } else {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Appointment information updating error. Please try again!",
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
                });

        goBackButton.addActionListener(
                e -> {
                    goBackButton.setEnabled(false);
                    SwingWorker<Void, Void> worker =
                            new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() {
                                    frmDoctorDashboard.getInstance().setVisible(true);
                                    setVisible(false);
                                    return null;
                                }
                                @Override
                                protected void done() {
                                    goBackButton.setEnabled(true);
                                }
                            };
                    worker.execute();
                });
    }
    public static synchronized frmUpdate getInstance() {
        if (instance == null) {
            instance = new frmUpdate();
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
            recentArea.setText(ConnectSQL.showDoctorBookingQuery(frmHome.getInstance().getID()[0]));
            recentArea.setEditable(false);
        }
    }
}
