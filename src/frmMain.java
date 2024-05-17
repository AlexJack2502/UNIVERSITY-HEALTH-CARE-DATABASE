import javax.swing.*;


public class frmMain extends JFrame {
    private static frmMain instance;
    private JPanel Panel;
    private JLabel Query;
    private JTextField textField1;
    private JTable Attendance;
    private JButton button1;


    private frmMain() {
        setContentPane(Panel);
        setTitle("Lab 5 PDM");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        button1.addActionListener(
            e->{
                button1.setEnabled(false);
                if (textField1.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Please do not leave query blank!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                        button1.setEnabled(true);
                        return;
                }
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        ConnectSQL.showQuery(textField1.getText(), Attendance);
                        return null;
                    }

                    @Override
                    protected void done() {
                        button1.setEnabled(true);
                    }
                };
                worker.execute();
            }
        );

    }
    public static synchronized frmMain getInstance() {
        if (instance == null) {
            instance = new frmMain();
        }
        return instance;
    }

}