import javax.swing.*;

public class frmSelect extends JFrame {
    private static frmSelect instance;
    private JButton studentButton;
    private JButton doctorButton;
    private JPanel panel;
    private JButton goBackButton;
    private JLabel insLabel;

    private frmSelect() {
        setContentPane(panel);
        setTitle("UNIVERSITY HEALTH CARE - Select your role");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        studentButton.addActionListener(
                e -> {
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you are a Student?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        JPasswordField inputPwd = new JPasswordField();
                        Object[] message = {"Re-enter your password: ", inputPwd};
                        int option2 =
                                JOptionPane.showConfirmDialog(
                                        null,
                                        message,
                                        "Password Verification",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                        if (option2 == JOptionPane.YES_OPTION) {
                            if (String.valueOf(inputPwd.getPassword())
                                    .equals(frmHome.getInstance().getCredentials()[1])) {
                                studentButton.setEnabled(false);
                                SwingWorker<Void, Void> worker =
                                        new SwingWorker<>() {
                                            @Override
                                            protected Void doInBackground() {
                                                frmStudentSign.getInstance().setVisible(true);
                                                setVisible(false);
                                                return null;
                                            }

                                            @Override
                                            protected void done() {
                                                studentButton.setEnabled(true);
                                            }
                                        };
                                worker.execute();
                            } else {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Passwords are not same! Please try again!",
                                        "Failed",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                });
        doctorButton.addActionListener(
                e -> {
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you are a Doctor?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        JPasswordField inputPwd = new JPasswordField();
                        Object[] message = {"Re-enter your password: ", inputPwd};
                        int option2 =
                                JOptionPane.showConfirmDialog(
                                        null,
                                        message,
                                        "Password Verification",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                        if (option2 == JOptionPane.YES_OPTION) {
                            if (String.valueOf(inputPwd.getPassword())
                                    .equals(frmHome.getInstance().getCredentials()[1])) {

                                doctorButton.setEnabled(false);
                                SwingWorker<Void, Void> worker =
                                        new SwingWorker<>() {
                                            @Override
                                            protected Void doInBackground() {
                                                frmDoctorSign.getInstance().setVisible(true);
                                                setVisible(false);
                                                return null;
                                            }

                                            @Override
                                            protected void done() {
                                                doctorButton.setEnabled(true);
                                            }
                                        };
                                worker.execute();
                            } else {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Passwords are not same! Please try again!",
                                        "Failed",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                });
        goBackButton.addActionListener(
                e -> {
                    int option =
                            JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to go back?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        goBackButton.setEnabled(false);
                        SwingWorker<Void, Void> worker =
                                new SwingWorker<>() {
                                    @Override
                                    protected Void doInBackground() {
                                        frmHome.getInstance().setVisible(true);
                                        setVisible(false);
                                        return null;
                                    }

                                    @Override
                                    protected void done() {
                                        goBackButton.setEnabled(true);
                                    }
                                };
                        worker.execute();
                    }
                });
    }

    public static synchronized frmSelect getInstance() {
        if (instance == null) {
            instance = new frmSelect();
        }
        return instance;
    }

    public static class frmSignDone extends JFrame {
        private static frmSignDone instance;
        private JButton backToHomeButton;
        private JLabel insLabel;
        private JPanel panel;

        private frmSignDone() {
            setContentPane(panel);
            setTitle("UNIVERSITY HEALTH CARE - Sign-up complete");
            setSize(500, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            backToHomeButton.addActionListener(
                    e -> {
                        backToHomeButton.setEnabled(false);
                        SwingWorker<Void, Void> worker =
                                new SwingWorker<>() {
                                    @Override
                                    protected Void doInBackground() {
                                        frmHome.getInstance().setVisible(true);
                                        setVisible(false);
                                        return null;
                                    }

                                    @Override
                                    protected void done() {
                                        backToHomeButton.setEnabled(true);
                                    }
                                };
                        worker.execute();
                    });
        }

        public static synchronized frmSignDone getInstance() {
            if (instance == null) {
                instance = new frmSignDone();
            }
            return instance;
        }
    }
}
