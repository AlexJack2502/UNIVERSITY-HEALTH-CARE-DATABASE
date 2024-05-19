import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.Vector;
import java.util.Objects;

public class ConnectSQL {
    static final String connectionUrl =
         "jdbc:sqlserver://sql.bsite.net\\MSSQL2016;databaseName=monoalice_UniversityHealthCare;user=monoalice_UniversityHealthCare;password=hehe;encrypt=true;trustServerCertificate=true;";

    public static void closeConnect(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection");
            }
        }
    }

    public static void showAvailableAppointmentQuery(JTable resultTable) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                            SELECT A.A_ID, A.Date, B.Price, D.FirstName, D.LastName, D.Gender
                            FROM [Appointment].[Appointment] A
                            	INNER JOIN [Billing].[Billing] B ON A.A_ID = B. Appointment_ID
                            	INNER JOIN [Appointment].[Doctor_Appointment] DA A.A_ID = DA.Appointment_ID
                            	INNER JOIN [Account].[Doctor] D ON D.D_ID = DA.Doctor_ID
                            WHERE A.Date >= CAST(GETDATE() AS DATE)
                            ORDER BY A.Date ASC""";
            stmt = con.prepareStatement(preparedQuery);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No available appointment found! Please try again later",
                        "Message",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                resultTable.setModel(Objects.requireNonNull(DbUtils.resultSetToTableModel(rs)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(con);
        }
    }
    public static boolean submitPatientHealingUpdate(String studentTxt, String appointmentTxt) {
        Connection con = null;
        PreparedStatement stmt;
        int rs;
        boolean isUpdated = false;
        try {
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            String updateString =
                    """
                            INSERT INTO [Appointment].[Student_Appointment] ([Student_ID],[Appointment_ID])
                            VALUES (?, ?)""";
            stmt = con.prepareStatement(updateString);
            stmt.setString(1, studentTxt);
            stmt.setString(2, appointmentTxt);
            rs = stmt.executeUpdate();
            if (rs > 0) {
                isUpdated = true;
            }
            con.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(con);
        }
        return isUpdated;
    }
    public static String[] showAuthenticateQuery(String accountTxt, String pwdTxt) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        String[] results = new String[2];
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                            DECLARE @UserName AS VARCHAR(20) = ?
                            DECLARE @Pwd AS VARCHAR(255) = ?
                            SELECT
                            CASE
                            	WHEN U.U_ID = S.User_ID AND S.User_ID IS NOT NULL THEN 'Student'
                            	WHEN U.U_ID = D.User_ID AND D.User_ID IS NOT NULL THEN 'Doctor'
                            	ELSE NULL
                            	END AS Role,
                            CASE
                            	WHEN U.U_ID = S.User_ID AND S.User_ID IS NOT NULL THEN S.St_ID
                            	WHEN U.U_ID = D.User_ID AND D.User_ID IS NOT NULL THEN D.D_ID
                            	ELSE NULL
                            	END AS ID
                            FROM [Account].[User] U, [Account].[Student] S, [Account].[Doctor] D
                            WHERE U.User_name = @UserName AND U.Password = @Pwd
                            """;
            stmt = con.prepareStatement(preparedQuery);
            stmt.setString(1, accountTxt);
            stmt.setString(2, pwdTxt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                results[0] = rs.getString("ID");
                results[1] = rs.getString("Role");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(con);
        }
        return results;
    }

    public static boolean submitPasswordUpdate(String userTxt, String oldPwdTxt, String newPwdTxt) {
        Connection con = null;
        PreparedStatement stmt;
        int rs;
        boolean isUpdated = false;
        try {
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            String updateString =
                    """
                            DECLARE @UserName AS VARCHAR(20) = ?
                            DECLARE @oldPwd AS VARCHAR(255) = ?
                            DECLARE @newPwd AS VARCHAR(255) = ?
                            UPDATE [Account].[User]
                            SET Password = @newPwd
                            WHERE User_name = @UserName AND Password = @oldPwd""";
            stmt = con.prepareStatement(updateString);
            stmt.setString(1, userTxt);
            stmt.setString(2, oldPwdTxt);
            stmt.setString(3, newPwdTxt);
            rs = stmt.executeUpdate();
            if (rs > 0) {
                isUpdated = true;
            }
            con.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(con);
        }
        return isUpdated;
    }

    public static String showSearchQuery(String queryTxt) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        StringBuilder result = new StringBuilder();
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                            SELECT T.IllnessType AS [Symptom], M.Name AS [Solution], M.Description AS [Description], M.Name AS [Medicine], M.Quantity AS [Quantity]
                            FROM [Treatment].[Treatment] T, [Treatment].[Medicine] M
                            WHERE T.Treat_ID = M.Treatment_ID""";
            stmt = con.prepareStatement(preparedQuery);
            stmt.setString(1, queryTxt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result
                        .append("Possible symptom: ")
                        .append(rs.getString("Symptom"))
                        .append(" has the solution of: ")
                        .append(rs.getString("Solution"))
                        .append(". Please see description: ")
                        .append(rs.getString("Description"))
                        .append(". Use medicine  ")
                        .append(rs.getString("Medicine"))
                        .append("with following quantity: ")
                        .append(rs.getString("Quantity"))
                        .append("\n\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(con);
        }
        return result.toString();
    }
    public static boolean cancelHealingUpdate(String patientTxt, String healingTxt) {
        Connection con = null;
        PreparedStatement stmt;
        int rs;
        boolean isUpdated = false;
        try {
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            String updateString =
                    """
                            DELETE FROM [Appointment].[Student_Appointment]
                            WHERE Student_ID = ? AND Appointment_ID = ?
                            """;
            stmt = con.prepareStatement(updateString);
            stmt.setString(1, patientTxt);
            stmt.setString(2, healingTxt);
            rs = stmt.executeUpdate();
            if (rs > 0) {
                isUpdated = true;
            }
            con.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(con);
        }
        return isUpdated;
    }

    public static String showPatientBookingQuery(String patientTxt) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        StringBuilder result = new StringBuilder();
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                            SELECT A.A_ID AS [ID], A.Date AS [Date], D.FirstName AS [FirstName],D.LastName AS [LastName], D.PhoneNumber AS [Phone]
                            FROM [Appointment].[Appointment] A, [Account].[Doctor] D, [Account].[Student] S
                            WHERE D.D_ID = A.Doctor_ID AND S.St_ID = A.Student_ID AND S.St_ID = ? AND A.Date >= CAST(GETDATE() AS DATE)
                            ORDER BY A.Date ASC""";
            stmt = con.prepareStatement(preparedQuery);
            stmt.setString(1, patientTxt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result
                        .append("ID: ")
                        .append(rs.getString("ID"))
                        .append("\n")
                        .append("Date: ")
                        .append(rs.getString("Date"))
                        .append("\n")
                        .append("Doctor name: ")
                        .append(rs.getString("FirstName"))
                        .append(" ")
                        .append(rs.getString("LastName"))
                        .append("\n")
                        .append("Phone: ")
                        .append(rs.getString("Phone"))
                        .append("\n\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(con);
        }
        return result.toString();
    }
    public static String showNameQuery(String userTxt, String roleTxt) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        String result = "";
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                            DECLARE @UserID AS INT = ?
                            DECLARE @Role AS VARCHAR(30) = ?
                            SELECT CONCAT(LastName, ' ', FirstName) AS [FullName]
                            FROM (
                            	SELECT
                            	CASE
                            		WHEN @UserID = S.St_ID AND @Role = 'Student' THEN S.FirstName
                            		WHEN @UserID = D.D_ID AND @Role = 'Doctor' THEN D.FirstName
                            		END AS FirstName,
                            	CASE
                            		WHEN @UserID = S.St_ID AND @Role = 'Student' THEN S.LastName
                            		WHEN @UserID = D.D_ID AND @Role = 'Doctor' THEN D.LastName
                            		END AS LastName
                            FROM [Account].[User] U
                            FULL JOIN [Account].[Student] S ON U.U_ID = S.User_ID
                            FULL JOIN [Account].[Doctor] D ON U.U_ID = D.User_ID) AS SUBQUERY
                            WHERE FirstName IS NOT NULL AND LastName IS NOT NULL""";
            stmt = con.prepareStatement(preparedQuery);
            stmt.setInt(1, Integer.parseInt(userTxt));
            stmt.setString(2, roleTxt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("FullName");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(con);
        }
        return result;
    }

    public static boolean submitHealingUpdate(
            String doctorTxt,
            String studentTxt,
            String dateTxt,
            String HealthStatusTxt,
            String BookingStatusTxt ) {
        Connection con = null;
        PreparedStatement stmt;
        int rs;
        boolean isUpdated = false;
        try {
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            String updateString =
                    """
                            SELECT*FROM Appointment.Appointment;
                            INSERT INTO [Appointment].[Appointment] ([Doctor_ID], [Student_ID], [Date], [HealthStatus], [BookingStatus]
                            VALUES (?, ?, ?, ?,?))""";
            stmt = con.prepareStatement(updateString);
            stmt.setString(1, doctorTxt);
            stmt.setString(2, studentTxt);
            stmt.setDate(3, java.sql.Date.valueOf(dateTxt));
            stmt.setString(4, HealthStatusTxt);
            stmt.setString(5, BookingStatusTxt);


            rs = stmt.executeUpdate();
            if (rs > 0) {
                isUpdated = true;
            }
            con.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(con);
        }
        return isUpdated;
    }

    public static String showDoctorBookingQuery(String specialistTxt) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        StringBuilder result = new StringBuilder();
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                            SELECT A.A_ID AS [ID], A.Date AS [Date], B.Price AS [Price],CONCAT(S.LastName , ' ' , S.FirstName) AS [FullName], S.Gender AS [Gender], S.PhoneNumber AS [Phone],
                            CASE
                            	WHEN AD.Appointment_ID = ASA.Appointment_ID THEN 'Confirmed'
                            	ELSE 'Pending'
                            	END AS State
                            FROM [Appointment].[Appointment] A
                            	INNER JOIN [Billing].[Billing] B ON B.Appointment_ID = A.A_ID
                            	INNER JOIN [Account].[Student] S ON A.Student_ID = S.St_ID
                            	INNER JOIN [Appointment].[Doctor_Appointment] AD ON AD.Appointment_ID = A.A_ID
                            	INNER JOIN [Appointment].[Student_Appointment] ASA ON ASA.Appointment_ID = A.A_ID
                            WHERE AD.Doctor_ID = ? AND A.Date >= CAST(GETDATE() AS Date)
                            ORDER BY A.Date ASC""";
            stmt = con.prepareStatement(preparedQuery);
            stmt.setString(1, specialistTxt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result
                        .append("ID: ")
                        .append(rs.getString("ID"))
                        .append("\n")
                        .append("State: ")
                        .append(rs.getString("State"))
                        .append("\n")
                        .append("Date: ")
                        .append(rs.getString("Date"))
                        .append("\n")
                        .append("Price: ")
                        .append(rs.getString("Price"))
                        .append("\n")
                        .append("Patient name: ")
                        .append(rs.getString("FullName"))
                        .append("\n")
                        .append("Gender: ")
                        .append(rs.getString("Gender"))
                        .append("\n")
                        .append("Phone: ")
                        .append(rs.getString("Phone"))
                        .append("\n")
                        .append("\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(con);
        }
        return result.toString();
    }

    public static boolean delistHealingUpdate(String doctorTxt, String appointmentTxt) {
        Connection con = null;
        PreparedStatement stmt;
        int rs;
        boolean isUpdated = false;
        try {
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            String updateString =
                    """
                    DELETE FROM [Appointment].[Doctor_Appointment]
                    WHERE Student_ID = ? AND Appointment_ID = ?""";
            stmt = con.prepareStatement(updateString);
            stmt.setString(1, doctorTxt);
            stmt.setString(2, appointmentTxt);
            rs = stmt.executeUpdate();
            if (rs > 0) {
                isUpdated = true;
            }
            con.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(con);
        }
        return isUpdated;
    }

    public static boolean submitDoctorUser(
            String accountTxt,
            String pwdTxt,
            String FirstNameTxt,
            String LastNameTxt,
            String dobTxt,
            String genderTxt,
            String emailTxt,
            String phoneTxt,
            String doctorIdTxt ) {
        Connection con = null;
        PreparedStatement stmt1;
        PreparedStatement stmt2;
        int rs1;
        int rs2;
        boolean isUpdated = false;
        try {
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            String updateString1 =
                    """
                            DECLARE @UserName AS VARCHAR(20) = ?
                            DECLARE @Pwd AS VARCHAR(255) = ?
                            INSERT INTO [Account].[User] ([User_name], [Password])
                            VALUES (@UserName, @Pwd)""";
            stmt1 = con.prepareStatement(updateString1);
            stmt1.setString(1, accountTxt);
            stmt1.setString(2, pwdTxt);
            rs1 = stmt1.executeUpdate();
            String updateString2 =
                    """
                            INSERT INTO [Account].[Doctor] ([D_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Email], [PhoneNumber])
                            VALUES (?, (SELECT U_ID FROM [Account].[User] U WHERE U.User_name = ?), ?, ?,?, ?, ?, ?)""";
            stmt2 = con.prepareStatement(updateString2);
            stmt2.setString(1, doctorIdTxt);
            stmt2.setString(2, accountTxt);
            stmt2.setString(3, FirstNameTxt);
            stmt2.setString(4, LastNameTxt);
            stmt2.setString(5, genderTxt);
            stmt2.setDate(6, java.sql.Date.valueOf(dobTxt));
            stmt2.setString(7, emailTxt);
            stmt2.setString(8, phoneTxt);

            rs2 = stmt2.executeUpdate();
            if (rs1 > 0 && rs2 > 0) {
                isUpdated = true;
            }
            con.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(con);
        }
        return isUpdated;
    }

    public static boolean submitStudentUser(
            String accountTxt,
            String pwdTxt,
            String FirstNameTxt,
            String LastNameTxt,
            String dobTxt,
            String genderTxt,
            String majorTxt,
            String phoneTxt,
            String StudentIdTxt,
            String addressTxt) {
        Connection con = null;
        PreparedStatement stmt1;
        PreparedStatement stmt2;
        int rs1;
        int rs2;
        boolean isUpdated = false;
        try {
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            String updateString1 =
                    """
                            DECLARE @UserName AS VARCHAR(20) = ?
                            DECLARE @Pwd AS VARCHAR(255) = ?
                            INSERT INTO [Account].[User] ([User_name], [Password])
                            VALUES (@UserName, @Pwd)""";
            stmt1 = con.prepareStatement(updateString1);
            stmt1.setString(1, accountTxt);
            stmt1.setString(2, pwdTxt);
            rs1 = stmt1.executeUpdate();
            String updateString2 =
                    """
                            INSERT INTO [Account].[Student] ([St_ID], [User_ID], [FirstName], [LastName], [Gender], [DateOfBirth], [Address], [PhoneNumber], [Major])
                            VALUES (?, (SELECT U_ID FROM [Account].[User] U WHERE U.User_name = ?), ?, ?,?, ?, ?, ?, ?)""";
            stmt2 = con.prepareStatement(updateString2);

            stmt2.setString(1, StudentIdTxt);
            stmt2.setString(2, accountTxt);
            stmt2.setString(3, FirstNameTxt);
            stmt2.setString(4, LastNameTxt);
            stmt2.setString(5, genderTxt);
            stmt2.setDate(6, java.sql.Date.valueOf(dobTxt));
            stmt2.setString(7, addressTxt);
            stmt2.setString(8, phoneTxt);
            stmt2.setString(9, majorTxt);
            rs2 = stmt2.executeUpdate();
            if (rs1 > 0 && rs2 > 0) {
                isUpdated = true;
            }
            con.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(con);
        }
        return isUpdated;
    }
    public static void showQuery(String preparedQuery, JTable resultTable) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(connectionUrl);
            stmt = con.prepareStatement(preparedQuery);
            rs = stmt.executeQuery();
            if (!rs.next())
                JOptionPane.showMessageDialog(
                        null,
                        "No available table found! Please try again later",
                        "Message",
                        JOptionPane.WARNING_MESSAGE);
            else {
                resultTable.setModel(DbUtils.resultSetToTableModel(rs));
                JOptionPane.showMessageDialog(
                        null,
                        "Available table is found!",
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {

            StringWriter output = new StringWriter();
            e.printStackTrace(new PrintWriter(output));
            JOptionPane.showMessageDialog(
                    null,
                    output.toString(),
                    "Message",
                    JOptionPane.WARNING_MESSAGE);
        } finally {
            closeConnect(con);
        }
    }
}
