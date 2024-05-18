import javax.swing.*;

public class frmRoles extends JFrame {
  private static frmRoles instance;
  private JButton patientButton;
  private JButton specialistButton;
    private JPanel panel;
  private JButton goBackButton;

    private frmRoles() {
    setContentPane(panel);
    setTitle("UNIVERSITY HEALTH CARE - Choose your role");
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    patientButton.addActionListener(
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
                  .equals(frmUser.getInstance().getCredentials()[1])) {
                patientButton.setEnabled(false);
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
                        patientButton.setEnabled(true);
                      }
                    };
                worker.execute();
              } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Passwords are not identical! Try again, or go back and change password.",
                    "Failed to verify",
                    JOptionPane.WARNING_MESSAGE);
              }
            }
          }
        });
    specialistButton.addActionListener(
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
                  .equals(frmUser.getInstance().getCredentials()[1])) {

                specialistButton.setEnabled(false);
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
                        specialistButton.setEnabled(true);
                      }
                    };
                worker.execute();
              } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Passwords are not identical! Try again, or go back and change password.",
                    "Failed to verify",
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
                    frmUser.getInstance().setVisible(true);
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

  public static synchronized frmRoles getInstance() {
    if (instance == null) {
      instance = new frmRoles();
    }
    return instance;
  }
}
