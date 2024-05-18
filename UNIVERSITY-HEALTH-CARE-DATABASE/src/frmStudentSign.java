import javax.swing.*;

public class frmStudentSign extends JFrame {
  private static frmStudentSign instance;
  private JButton goBackButton;
  private JButton confirmButton;
  private JTextField fullNameField;
  private JTextField dobField;
  private JTextField addressField;
    private JLabel addressLabel;
  private JLabel genderLabel;
    private JLabel fullNameLabel;
  private JLabel dobLabel;
  private JPanel panel;
  private JButton clearAllButton;
    private JComboBox<String> genderField;
    private JLabel idLabel;
    private JTextField idField;
    private JLabel phoneLabel;
    private JTextField phoneField;
    private JLabel majorLable;
    private JTextField majorField;

    private frmStudentSign() {
    setContentPane(panel);
    setTitle("UNIVERSITY HEALTH CARE - Student sign-up");
    setSize(700, 700);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    clearAllButton.addActionListener(
        e -> {
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Are you sure you want to clear all field(s)?",
                  "Confirm",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            clearAllButton.setEnabled(false);
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    fullNameField.setText("");
                    addressField.setText("");
                    dobField.setText("");
                    genderField.setSelectedItem("<please choose>");
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
    confirmButton.addActionListener(
        e -> {
          if (fullNameField.getText().isEmpty()
              || addressField.getText().isEmpty()
              || dobField.getText().isEmpty()
              || String.valueOf(genderField.getSelectedItem()).equals("<please choose>")) {
            JOptionPane.showMessageDialog(
                null, "Field(s) are empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            confirmButton.setEnabled(true);
            return;
          }
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Please check your information carefully!",
                  "Confirm",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            confirmButton.setEnabled(false);
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    if (ConnectSQL.submitPatientUser(
                        frmUser.getInstance().getCredentials()[0],
                        frmUser.getInstance().getCredentials()[1],
                        fullNameField.getText(),
                        dobField.getText(),
                        String.valueOf(genderField.getSelectedItem()),
                        addressField.getText())) {
                      JOptionPane.showMessageDialog(
                          null,
                          "Account: "
                              + frmUser.getInstance().getCredentials()[0]
                              + " registered successfully. Thank you!",
                          "Success",
                          JOptionPane.INFORMATION_MESSAGE);
                      frmSignDone.getInstance().setVisible(true);
                      setVisible(false);
                    } else {
                      JOptionPane.showMessageDialog(
                          null,
                          "Error! Please check again!",
                          "Warning",
                          JOptionPane.WARNING_MESSAGE);
                      fullNameField.setText("");
                      addressField.setText("");
                      dobField.setText("");
                      genderField.setSelectedItem("<please choose>");
                    }
                    return null;
                  }

                  @Override
                  protected void done() {
                    confirmButton.setEnabled(true);
                  }
                };
            worker.execute();
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
                    frmRoles.getInstance().setVisible(true);
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

  public static synchronized frmStudentSign getInstance() {
    if (instance == null) {
      instance = new frmStudentSign();
    }
    return instance;
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (isVisible()) {
      fullNameField.setText("");
      addressField.setText("");
      dobField.setText("");
      genderField.setSelectedItem("<please choose>");
    }
  }
}
