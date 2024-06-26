import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class frmOption extends JFrame {
  private static frmOption instance;
  private JButton backToHomeButton;
  private JTextField queryField;
  private JTable resultTable;
  private JButton runQueryButton;
  private JButton clearAllButton;
  private JPanel panel;
    private JLabel queryLabel;
  private JLabel resultLabel;

    private frmOption() {
    setContentPane(panel);
    setTitle("UNIVERSITY HEALTH CARE - Optional mode");
    setSize(1200, 800);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    backToHomeButton.addActionListener(
        e -> {
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Are you sure you want to return to the home page?",
                  "Confirm",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
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
          }
        });
    runQueryButton.addActionListener(
        e -> {
          runQueryButton.setEnabled(false);
          if (queryField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                null, "Please do not leave query blank!", "Warning", JOptionPane.WARNING_MESSAGE);
            runQueryButton.setEnabled(true);
            return;
          }
          SwingWorker<Void, Void> worker =
              new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                  ConnectSQL.showQuery(queryField.getText(), resultTable);
                  return null;
                }

                @Override
                protected void done() {
                  runQueryButton.setEnabled(true);
                }
              };
          worker.execute();
        });
    clearAllButton.addActionListener(
        e -> {
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Are you sure you want to delete all ?",
                  "Confirm",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            clearAllButton.setEnabled(false);
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    queryField.setText("");
                    resultTable.setModel(new DefaultTableModel());
                    return null;
                  }

                  @Override
                  protected void done() {
                    clearAllButton.setEnabled(true);
                  }
                };
            worker.execute();
          }
        });
  }

  public static synchronized frmOption getInstance() {
    if (instance == null) {
      instance = new frmOption();
    }
    return instance;
  }
}
