import javax.swing.*;
import java.util.Objects;

public class frmHome extends JFrame {
    private static frmHome instance;
    private JPasswordField pwdField;
    private JTextField userField;
    private JButton logInButton;
    private JButton signUpButton;
    private JPanel panel;
    private JButton explorerModeButton;
    private String[] results;
    private JLabel titleLabel;
    private JLabel userLabel;
    private JLabel pwdLabel;
    private JLabel logoLabel;

    private frmHome() {
        setContentPane(panel);
        setTitle("UNIVERSITY HEALTH CARE - Homepage");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        logInButton.addActionListener(
                e -> {
                    logInButton.setEnabled(false);
                    if (userField.getText().isEmpty() || String.valueOf(pwdField.getPassword()).isEmpty()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Either account or password field(s) are empty!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                        logInButton.setEnabled(true);
                        return;
                    }
                    SwingWorker<Void, Void> worker =
                            new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() {
                                    results =
                                            ConnectSQL.showAuthenticateQuery(
                                                    userField.getText(), String.valueOf(pwdField.getPassword()));
                                    if (Objects.equals(results[1], "Student")) {
                                        frmStudentDashboard.getInstance().setVisible(true);
                                        setVisible(false);
                                    } else if (Objects.equals(results[1], "Doctor")) {
                                        frmDoctorDashboard.getInstance().setVisible(true);
                                        setVisible(false);
                                    } else {
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Either account not exists or credentials not correct!",
                                                "Warning",
                                                JOptionPane.WARNING_MESSAGE);
                                        pwdField.setText("");
                                    }
                                    return null;
                                }

                                @Override
                                protected void done() {
                                    logInButton.setEnabled(true);
                                }
                            };
                    worker.execute();
                });
        signUpButton.addActionListener(
                e -> {
                    signUpButton.setEnabled(false);
                    if (userField.getText().isEmpty()
                            || String.valueOf(pwdField.getPassword()).isEmpty()
                            || ConnectSQL.showAuthenticateQuery(
                            userField.getText(), String.valueOf(pwdField.getPassword()))[1]
                            != null) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Either account has existed or field(s) are empty!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                        pwdField.setText("");
                        signUpButton.setEnabled(true);
                        return;
                    }
                    SwingWorker<Void, Void> worker =
                            new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() {
                                    frmSelect.getInstance().setVisible(true);
                                    setVisible(false);
                                    return null;
                                }

                                @Override
                                protected void done() {
                                    signUpButton.setEnabled(true);
                                }
                            };
                    worker.execute();
                });
        explorerModeButton.addActionListener(
                e -> {
                    explorerModeButton.setEnabled(false);
                    SwingWorker<Void, Void> worker =
                            new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() {
                                    JPasswordField masterPwd = new JPasswordField();
                                    Object[] message = {"Master password: ", masterPwd};
                                    int option =
                                            JOptionPane.showConfirmDialog(
                                                    null,
                                                    message,
                                                    "Password Required",
                                                    JOptionPane.YES_NO_OPTION,
                                                    JOptionPane.QUESTION_MESSAGE);
                                    if (option == JOptionPane.YES_OPTION) {
                                        if (String.valueOf(masterPwd.getPassword()).equals("pdm")) {
                                            frmExplorer.getInstance().setVisible(true);
                                            setVisible(false);
                                        } else {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Master Password not correct!",
                                                    "Access Denied",
                                                    JOptionPane.WARNING_MESSAGE);
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                protected void done() {
                                    explorerModeButton.setEnabled(true);
                                }
                            };
                    worker.execute();
                });
    }

    public static synchronized frmHome getInstance() {
        if (instance == null) {
            instance = new frmHome();
        }
        return instance;
    }

    public String[] getID() {
        return results;
    }

    public String[] getCredentials() {
        return new String[] {userField.getText(), String.valueOf(pwdField.getPassword())};
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (isVisible()) {
            pwdField.setText("");
        }
    }
}
