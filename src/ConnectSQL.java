import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.Vector;

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
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(connectionUrl);
            String preparedQuery =
                    """
                            SELECT H.HealingInformation_ID AS ID, H.Date, H.Place, CAST(H.Fee AS INT) AS Fee, S.FullName, S.Sex
                            FROM [Booking].[HealingInformation] H
                            INNER JOIN [Account].[Specialist] S
                            ON S.Specialist_ID = H.SpecialistID
                            WHERE H.HealingInformation_ID NOT IN (SELECT DISTINCT B.HealingInformationID
                            FROM [Booking].[Booking] B) AND H.Date >= CAST(GETDATE() AS DATE ) ORDER BY H.Date""";
            stmt = con.prepareStatement(preparedQuery);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No available healing found! Please try again later",
                        "Message",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                resultTable.setModel(Objects.requireNonNull(resultSetToTableModel(rs)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnect(con);
        }
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