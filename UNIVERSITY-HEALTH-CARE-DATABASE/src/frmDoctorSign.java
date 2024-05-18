import javax.swing.*;

public class frmDoctorSign extends JFrame {
  private static frmDoctorSign instance;
  private JTextField fullNameField;
  private JTextField emailField;
  private JTextField idField;
  private JTextField graduateField;
  private JButton goBackButton;
  private JButton confirmButton;
  private JPanel panel;
  private JLabel titleLabel;
  private JLabel insLabel;
  private JLabel fullNameLabel;
  private JLabel dobLabel;
  private JTextField dobField;
  private JLabel emailLabel;
  private JLabel genderLabel;
  private JLabel idLabel;
  private JLabel graduateLabel;
  private JComboBox<String> genderField;
  private JButton clearAllButton;
  private JLabel phoneLabel;
  private JTextField phoneField;
  private JLabel copyrightLabel;

  private frmDoctorSign() {
    setContentPane(panel);
    setTitle("UNIVERSITY HEALTH CARE - Doctor sign-up");
    setSize(800, 800);
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
                    emailField.setText("");
                    dobField.setText("");
                    genderField.setSelectedItem("<please choose>");
                    idField.setText("");
                    graduateField.setText("");
                    phoneField.setText("");
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
              || emailField.getText().isEmpty()
              || dobField.getText().isEmpty()
              || String.valueOf(genderField.getSelectedItem()).equals("<please choose>")
              || idField.getText().length() != 12
              || graduateField.getText().isEmpty()
              || phoneField.getText().length() != 10) {
            JOptionPane.showMessageDialog(
                null,
                "Field(s) are empty or in incorrect format!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
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
                    if (ConnectSQL.submitSpecialistUser(
                        frmUser.getInstance().getCredentials()[0],
                        frmUser.getInstance().getCredentials()[1],
                        fullNameField.getText(),
                        dobField.getText(),
                        String.valueOf(genderField.getSelectedItem()),
                        emailField.getText(),
                        phoneField.getText(),
                        idField.getText(),
                        graduateField.getText())) {
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

  public static synchronized frmDoctorSign getInstance() {
    if (instance == null) {
      instance = new frmDoctorSign();
    }
    return instance;
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (isVisible()) {
      fullNameField.setText("");
      emailField.setText("");
      dobField.setText("");
      genderField.setSelectedItem("<please choose>");
      idField.setText("");
      graduateField.setText("");
      phoneField.setText("");
    }
  }
}
