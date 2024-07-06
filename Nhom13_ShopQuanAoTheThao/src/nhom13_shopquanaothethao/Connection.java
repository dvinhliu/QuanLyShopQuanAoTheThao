/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ACER
 */
public class Connection {
    public static String constr = "jdbc:sqlserver://localhost:1433;databaseName=QL_ShopQuanAoTheThao;user=sa;password=123456;encrypt=false;trustServerCertificate=true";
    public static java.sql.Connection GetConnection()
    {
        java.sql.Connection conn = null;
        try
        {
            conn = DriverManager.getConnection(constr);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return conn;
    }
}
