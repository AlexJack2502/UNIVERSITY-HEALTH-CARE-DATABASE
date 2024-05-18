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
         "jdbc:sqlserver://sql.bsite.net\\MSSQL2016;databaseName=monoalice_PDM_Lab5;user=monoalice_PDM_Lab5;password=MonoAlice;encrypt=true;trustServerCertificate=true;";

    public static void closeConnect(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection");
            }
        }
    }

    public static void showAvailableHealingQuery(JTable resultTable) {
        Connection connection = null;
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            connection = DriverManager.getConnection(connectionUrl);
            String query =
                    """
                    SELECT H.HealingInformation_ID AS ID, H.Date, H.Place, CAST(H.Fee AS INT) AS Fee, S.FullName, S.Sex
                    FROM [Booking].[HealingInformation] H
                    INNER JOIN [Account].[Specialist] S
                    ON S.Specialist_ID = H.SpecialistID
                    WHERE H.HealingInformation_ID NOT IN (SELECT DISTINCT B.HealingInformationID
                    FROM [Booking].[Booking] B) AND H.Date >= CAST(GETDATE() AS DATE ) ORDER BY H.Date""";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No available healing sessions found! Please try again later",
                        "Message",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                resultTable.setModel(Objects.requireNonNull(DbUtils.resultSetToTableModel(resultSet)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(connection);
        }
    }
    
    public static boolean submitPatientHealingUpdate(String patientId, String healingSessionId) {
        Connection connection = null;
        PreparedStatement statement;
        int affectedRows;
        boolean updateSuccess = false;
        try {
            connection = DriverManager.getConnection(connectionUrl);
            connection.setAutoCommit(false);
            String updateQuery =
                    """
                    INSERT INTO [Booking].[Booking] ([PatientID], [HealingInformationID])
                    VALUES (?,?)""";
            statement = connection.prepareStatement(updateQuery);
            statement.setString(1, patientId);
            statement.setString(2, healingSessionId);
            affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                updateSuccess = true;
            }
            connection.commit();
        } catch (SQLException e) {
            return updateSuccess;
        } finally {
            closeConnect(connection);
        }
        return updateSuccess;
    }

    //Boundary 1 
    public static String[] showAuthenticateQuery(String accountTxt, String pwdTxt) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        String[] results = new String[2];
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                              DECLARE @UserName AS VARCHAR(50) = ?
                              DECLARE @Pwd AS VARCHAR(30) = ?
          
                              SELECT
                                     CASE
                                         WHEN A.User_ID = P.UserID AND P.UserID IS NOT NULL THEN 'Patient'
                                         WHEN A.User_ID = S.UserID AND S.UserID IS NOT NULL THEN 'Specialist'
                                         ELSE NULL
                                         END AS Role,
                                     CASE
                                         WHEN A.User_ID = P.UserID AND P.UserID IS NOT NULL THEN P.Patient_ID
                                         WHEN A.User_ID = S.UserID AND S.UserID IS NOT NULL THEN S.Specialist_ID
                                         ELSE NULL
                                         END AS ID
                              FROM [Account].[Account] A
                                       FULL JOIN [Account].[Patient] P ON A.User_ID = P.UserID
                                       FULL JOIN [Account].[Specialist] S ON A.User_ID = S.UserID
                              WHERE A.User_name = @UserName AND A.Password = HASHBYTES('SHA1', CONCAT(@Pwd, (SELECT A.Salt FROM [Account].[Account] A WHERE A.User_name = @UserName)))
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
                              DECLARE @UserName AS VARCHAR(50) = ?
                              DECLARE @oldPwd AS VARCHAR(30) = ?
                              DECLARE @newPwd AS VARCHAR(30) = ?
                              DECLARE @Salt AS VARCHAR(5)
                              SET @Salt
                                  = CONCAT(
                                              CHAR(FLOOR(RAND() * 10) + 48),
                                              CHAR(FLOOR(RAND() * 26) + 65),
                                              CHAR(FLOOR(RAND() * 26) + 97),
                                              CHAR(FLOOR(RAND() * 15) + 33),
                                              CHAR(FLOOR(RAND() * 10) + 48))
                              UPDATE [Account].[Account]
                              SET Password = HASHBYTES('SHA1', CONCAT(@newPwd, @Salt)),
                                  Salt = @Salt
                              WHERE User_name = @UserName
                                AND Password  = HASHBYTES(
                                      'SHA1', CONCAT(@oldPwd, (SELECT A.Salt FROM [Account].[Account] A WHERE A.User_name = @UserName)))
                                                                      """;
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

    //Boundary 2 

    public static String showSearchQuery(String queryText) {
        Connection connection = null;
        PreparedStatement statement;
        ResultSet resultSet;
        StringBuilder result = new StringBuilder();
        try {
            connection = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                    SELECT S.Name AS symptom, So.Name AS solution, So.Platform, So.Description FROM [Disease].[Disease] D INNER JOIN [Disease].[DiseaseSymptom] DS ON D.Disease_ID = DS.DiseaseID
                    INNER JOIN [Disease].[Symptom] S ON DS.SymptomID = S.Symptom_ID
                    INNER JOIN [Solution].[CureOneByOne] C ON S.Symptom_ID = C.SymptomID
                    INNER JOIN [Solution].[Solution] So ON C.SolutionID = So.Solution_ID  WHERE D.Name = ?""";
            statement = connection.prepareStatement(preparedQuery);
            statement.setString(1, queryText);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result
                        .append("Possible symptom: ")
                        .append(resultSet.getString("symptom"))
                        .append(" has the solution of: ")
                        .append(resultSet.getString("solution"))
                        .append(". Please see at: ")
                        .append(resultSet.getString("platform"))
                        .append(", more details: ")
                        .append(resultSet.getString("description"))
                        .append("\n\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(connection);
        }
        return result.toString();
    }
    
    public static boolean cancelHealingUpdate(String patientId, String healingId) {
        Connection connection = null;
        PreparedStatement statement;
        int affectedRows;
        boolean isUpdated = false;
        try {
            connection = DriverManager.getConnection(connectionUrl);
            connection.setAutoCommit(false);
            String updateQuery =
                    """
                    DELETE FROM [Booking].[Booking]
                    WHERE PatientID = ? AND HealingInformationID = ?
                    """;
            statement = connection.prepareStatement(updateQuery);
            statement.setString(1, patientId);
            statement.setString(2, healingId);
            affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                isUpdated = true;
            }
            connection.commit();
        } catch (SQLException e) {
            return isUpdated;
        } finally {
            closeConnect(connection);
        }
        return isUpdated;
    }
    

    //Boundary 3 

    public static String showPatientBookingQuery(String patientId) {
        Connection connection = null;
        PreparedStatement statement;
        ResultSet resultSet;
        StringBuilder result = new StringBuilder();
        try {
            connection = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                    SELECT H.HealingInformation_ID AS ID, CONCAT(DAY(H.Date), ' / ' , MONTH(H.Date)) AS DATE , H.Place, S.FullName, S.Phone
                    FROM [Account].[Specialist] S
                    INNER JOIN [Booking].[HealingInformation] H
                    ON S.Specialist_ID = H.SpecialistID
                    INNER JOIN [Booking].[Booking] B
                    ON H.HealingInformation_ID = B.HealingInformationID
                    INNER JOIN [Account].[Patient] P
                    ON B.PatientID = P.Patient_ID
                    WHERE P.Patient_ID = ? AND H.Date >= CAST(GETDATE() AS DATE ) ORDER BY H.Date""";
            statement = connection.prepareStatement(preparedQuery);
            statement.setString(1, patientId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result
                        .append("ID: ")
                        .append(resultSet.getString("id"))
                        .append("\n")
                        .append("Date: ")
                        .append(resultSet.getString("date"))
                        .append("\n")
                        .append("Place: ")
                        .append(resultSet.getString("place"))
                        .append("\n")
                        .append("Specialist name: ")
                        .append(resultSet.getString("fullname"))
                        .append("\n")
                        .append("Phone: ")
                        .append(resultSet.getString("phone"))
                        .append("\n\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(connection);
        }
        return result.toString();
    }
    
    public static String showNameQuery(String userId, String role) {
        Connection connection = null;
        PreparedStatement statement;
        ResultSet resultSet;
        String result = "";
        try {
            connection = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                    DECLARE @UserID AS INT = ?
                    DECLARE @Role AS VARCHAR(30) = ?
                    SELECT FullName
                    FROM (
                             SELECT
                                 CASE
                                     WHEN @UserID = P.Patient_ID AND @Role = 'Patient' THEN P.FullName
                                     WHEN @UserID = S.Specialist_ID AND @Role = 'Specialist' THEN S.FullName
                                     END AS FullName
                             FROM [Account].[Account] A
                                      FULL JOIN [Account].[Patient] P ON A.User_ID = P.UserID
                                      FULL JOIN [Account].[Specialist] S ON A.User_ID = S.UserID
                         ) AS SUBQUERY
                    WHERE FullName IS NOT NULL
                    """;
            statement = connection.prepareStatement(preparedQuery);
            statement.setInt(1, Integer.parseInt(userId));
            statement.setString(2, role);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getString("fullname");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(connection);
        }
        return result; 
    }  
    

    //Boundary 4 

    public static boolean submitHealingUpdate(
        String specialistId,
        String place,
        String date,
        String fee,
        String description,
        String extraInfo) {
    Connection connection = null;
    PreparedStatement statement;
    int result;
    boolean isAdded = false;
    try {
        connection = DriverManager.getConnection(connectionUrl);
        connection.setAutoCommit(false);
        String insertQuery =
                """
                INSERT INTO [Booking].[HealingInformation] ([SpecialistID], [Place], [Date], [Fee], [Description], [Extra_Information])
                VALUES (?,?,?,?,?,?)""";
        statement = connection.prepareStatement(insertQuery);
        statement.setString(1, specialistId);
        statement.setString(2, place);
        statement.setDate(3, java.sql.Date.valueOf(date));
        statement.setString(4, fee);
        statement.setString(5, description);
        statement.setString(6, extraInfo);
        result = statement.executeUpdate();
        if (result > 0) {
            isAdded = true;
        }
        connection.commit();
    } catch (SQLException e) {
        return isAdded;
    } finally {
        closeConnect(connection);
    }
    return isAdded;
}

public static String showSpecialistBookingQuery(String specialistId) {
    Connection connection = null;
    PreparedStatement statement;
    ResultSet resultSet;
    StringBuilder result = new StringBuilder();
    try {
        connection = DriverManager.getConnection(connectionUrl);
        String preparedQuery =
                """
                SELECT H.HealingInformation_ID AS ID, CONCAT(DAY(H.Date), ' / ' , MONTH(H.Date)) AS DATE, H.Place, H.Fee, P.FullName, P.Sex, P.Email,
                CASE
                    WHEN H.HealingInformation_ID = B.HealingInformationID THEN 'RESERVED'
                    ELSE 'VACANT' 
                END AS State
                FROM [Account].[Specialist] S
                FULL JOIN [Booking].[HealingInformation] H ON S.Specialist_ID = H.SpecialistID
                FULL JOIN [Booking].[Booking] B ON H.HealingInformation_ID = B.HealingInformationID
                FULL JOIN [Account].[Patient] P ON B.PatientID = P.Patient_ID
                WHERE S.[Specialist_ID] = ? AND H.Date >= CAST(GETDATE() AS DATE) ORDER BY H.Date""";
        statement = connection.prepareStatement(preparedQuery);
        statement.setString(1, specialistId);
        resultSet = statement.executeQuery();
        while (resultSet.next()) {
            result
                    .append("ID: ")
                    .append(resultSet.getString("id"))
                    .append("\n")
                    .append("State: ")
                    .append(resultSet.getString("state"))
                    .append("\n")
                    .append("Date: ")
                    .append(resultSet.getString("date"))
                    .append("\n")
                    .append("Place: ")
                    .append(resultSet.getString("place"))
                    .append("\n")
                    .append("Fee: ")
                    .append(resultSet.getString("fee"))
                    .append("\n")
                    .append("Patient name: ")
                    .append(resultSet.getString("fullname"))
                    .append("\n")
                    .append("Sex: ")
                    .append(resultSet.getString("sex"))
                    .append("\n")
                    .append("Email: ")
                    .append(resultSet.getString("email"))
                    .append("\n")
                    .append("\n");
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        closeConnect(connection);
    }
    return result.toString();
} 


    //Boundary 5 

    public static boolean delistHealingUpdate(String specialistId, String healingId) {
        Connection connection = null;
        PreparedStatement statement;
        int result;
        boolean isRemoved = false;
        try {
            connection = DriverManager.getConnection(connectionUrl);
            connection.setAutoCommit(false);
            String deleteQuery =
                    """
                    DELETE FROM [Booking].[HealingInformation]
                    WHERE SpecialistID = ? AND HealingInformation_ID = ?
                    """;
            statement = connection.prepareStatement(deleteQuery);
            statement.setString(1, specialistId);
            statement.setString(2, healingId);
            result = statement.executeUpdate();
            if (result > 0) {
                isRemoved = true;
            }
            connection.commit();
        } catch (SQLException e) {
            return isRemoved;
        } finally {
            closeConnect(connection);
        }
        return isRemoved;
    }
    
    public static boolean submitSpecialistUser(
            String username,
            String password,
            String fullName,
            String dob,
            String sex,
            String email,
            String phone,
            String id,
            String graduationUniversity) {
        Connection connection = null;
        PreparedStatement stmt1;
        PreparedStatement stmt2;
        int rs1;
        int rs2;
        boolean isAdded = false;
        try {
            connection = DriverManager.getConnection(connectionUrl);
            connection.setAutoCommit(false);
            String insertQuery1 =
                    """
                    DECLARE @Username AS VARCHAR(50) = ?
                    DECLARE @Pwd AS VARCHAR(30) = ?
                    DECLARE @Salt AS VARCHAR(5)
                    SET @Salt
                        = CONCAT(
                                    CHAR(FLOOR(RAND() * 10) + 48),
                                    CHAR(FLOOR(RAND() * 26) + 65),
                                    CHAR(FLOOR(RAND() * 26) + 97),
                                    CHAR(FLOOR(RAND() * 15) + 33),
                                    CHAR(FLOOR(RAND() * 10) + 48))
                    INSERT INTO [Account].[Account] ([User_name],
                                                     [Password],
                                                     [Salt])
                    VALUES (@Username, HASHBYTES('SHA1', CONCAT(@Pwd, @Salt)), @Salt)
                    """;
            stmt1 = connection.prepareStatement(insertQuery1);
            stmt1.setString(1, username);
            stmt1.setString(2, password);
            rs1 = stmt1.executeUpdate();
            String insertQuery2 =
                    """
                    INSERT INTO [Account].[Specialist] ([UserID],
                                                        [FullName],
                                                        [DoB],
                                                        [Sex],
                                                        [Email],
                                                        [Phone],
                                                        [IdentifyNumber],
                                                        [GraduationUniversity])
                    VALUES ((SELECT User_ID FROM [Account].[Account] A WHERE A.User_name = ?), ?, ?, ?, ?, ?, ?, ?)
                    """;
            stmt2 = connection.prepareStatement(insertQuery2);
            stmt2.setString(1, username);
            stmt2.setString(2, fullName);
            stmt2.setDate(3, java.sql.Date.valueOf(dob));
            stmt2.setString(4, sex);
            stmt2.setString(5, email);
            stmt2.setString(6, phone);
            stmt2.setString(7, id);
            stmt2.setString(8, graduationUniversity);
            rs2 = stmt2.executeUpdate();
            if (rs1 > 0 && rs2 > 0) {
                isAdded = true;
            }
            connection.commit();
        } catch (SQLException e) {
            return isAdded;
        } finally {
            closeConnect(connection);
        }
        return isAdded;
    } 
    

    //Boundary 6 

    public static boolean submitPatientUser(
        String username,
        String password,
        String fullName,
        String dob,
        String sex,
        String email) {
    Connection connection = null;
    PreparedStatement stmt1;
    PreparedStatement stmt2;
    int rs1;
    int rs2;
    boolean isAdded = false;
    try {
        connection = DriverManager.getConnection(connectionUrl);
        connection.setAutoCommit(false);
        String insertQuery1 =
                """
                DECLARE @Username AS VARCHAR(50) = ?
                DECLARE @Pwd AS VARCHAR(30) = ?
                DECLARE @Salt AS VARCHAR(5)
                SET @Salt
                    = CONCAT(
                                CHAR(FLOOR(RAND() * 10) + 48),
                                CHAR(FLOOR(RAND() * 26) + 65),
                                CHAR(FLOOR(RAND() * 26) + 97),
                                CHAR(FLOOR(RAND() * 15) + 33),
                                CHAR(FLOOR(RAND() * 10) + 48))
                INSERT INTO [Account].[Account] ([User_name],
                                                 [Password],
                                                 [Salt])
                VALUES (@Username, HASHBYTES('SHA1', CONCAT(@Pwd, @Salt)), @Salt)
                """;
        stmt1 = connection.prepareStatement(insertQuery1);
        stmt1.setString(1, username);
        stmt1.setString(2, password);
        rs1 = stmt1.executeUpdate();
        String insertQuery2 =
                """
                INSERT INTO [Account].[Patient] ([UserID],
                                                 [FullName],
                                                 [DoB],
                                                 [Sex],
                                                 [Email])
                VALUES ((SELECT User_ID FROM [Account].[Account] A WHERE A.User_name = ?), ?, ?, ?, ?)
                """;
        stmt2 = connection.prepareStatement(insertQuery2);
        stmt2.setString(1, username);
        stmt2.setString(2, fullName);
        stmt2.setDate(3, java.sql.Date.valueOf(dob));
        stmt2.setString(4, sex);
        stmt2.setString(5, email);
        rs2 = stmt2.executeUpdate();
        if (rs1 > 0 && rs2 > 0) {
            isAdded = true;
        }
        connection.commit();
    } catch (SQLException e) {
        return isAdded;
    } finally {
        closeConnect(connection);
    }
    return isAdded;
}

public static void showQuery(String preparedQuery, JTable resultTable) {
    Connection connection = null;
    PreparedStatement statement;
    ResultSet resultSet;
    try {
        connection = DriverManager.getConnection(connectionUrl);
        statement = connection.prepareStatement(preparedQuery);
        resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No available table found! Please try again later",
                    "Message",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            resultTable.setModel(DbUtils.resultSetToTableModel(resultSet));
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
        closeConnect(connection);
    }
}
}