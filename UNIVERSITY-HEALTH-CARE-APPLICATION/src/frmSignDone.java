import javax.swing.*;

public class frmSignDone extends JFrame {
    private static frmSignDone instance;
    private JButton goToLogInButton;
    private JLabel insLabel;
    private JPanel panel;

    private frmSignDone() {
        setContentPane(panel);
        setTitle("UNIVERSITY HEALTH CARE - Sign-up complete");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        goToLogInButton.addActionListener(
                e -> {
                    goToLogInButton.setEnabled(false);
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
                                    goToLogInButton.setEnabled(true);
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
