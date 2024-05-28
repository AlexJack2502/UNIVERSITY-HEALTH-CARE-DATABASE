import javax.swing.*;

public class frmStudentSign extends JFrame {
  private static frmStudentSign instance;
  private JButton goBackButton;
  private JButton confirmButton;
  private JTextField lastNameField;
  private JTextField firstNameField;
  private JTextField dobField;
  private JTextField addressField;
  private JLabel addressLabel;
  private JLabel genderLabel;
  private JLabel lastNameLabel;
  private JLabel firstNameLabel;
  private JLabel dobLabel;
  private JPanel panel;
  private JButton deleteAllButton;
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
    deleteAllButton.addActionListener(
        e -> {
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Are you sure you want to delete all?",
                  "Confirm",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            deleteAllButton.setEnabled(false);
            SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                  @Override
                  protected Void doInBackground() {
                    lastNameField.setText("");
                    firstNameField.setText("");
                    addressField.setText("");
                    dobField.setText("");
                    genderField.setSelectedItem("<please choose>");
                    idField.setText("");
                    phoneField.setText("");
                    majorField.setText("");
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
    confirmButton.addActionListener(
        e -> {
          if (lastNameField.getText().isEmpty()
              || firstNameField.getText().isEmpty()
              || addressField.getText().isEmpty()
              || dobField.getText().isEmpty()
              || String.valueOf(genderField.getSelectedItem()).equals("<please choose>")
                  || majorField.getText().isEmpty()
                  || idField.getText().length() > 12
                  || phoneField.getText().length() != 10) {
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
                    if (ConnectSQL.submitStudentUser(
                        frmHome.getInstance().getCredentials()[0],
                        frmHome.getInstance().getCredentials()[1],
                        lastNameField.getText(),
                        firstNameField.getText(),
                        dobField.getText(),
                        String.valueOf(genderField.getSelectedItem()),
                        majorField.getText(),
                        phoneField.getText(),
                        idField.getText(),
                        addressField.getText())) {
                      JOptionPane.showMessageDialog(
                          null,
                          "Account: "
                              + frmHome.getInstance().getCredentials()[0]
                              + " You have successfully signed up!",
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
                      lastNameField.setText("");
                      addressField.setText("");
                      dobField.setText("");
                      idField.setText("");
                      majorField.setText("");
                      phoneField.setText("");
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
                    frmSelect.getInstance().setVisible(true);
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
      lastNameField.setText("");
      addressField.setText("");
      dobField.setText("");
        idField.setText("");
        majorField.setText("");
        phoneField.setText("");
      genderField.setSelectedItem("<please choose>");
    }
  }
}
