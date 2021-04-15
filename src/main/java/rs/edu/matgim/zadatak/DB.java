package rs.edu.matgim.zadatak;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    String connectionString = "jdbc:sqlite:src\\main\\java\\KompanijaZaPrevoz.db";

    public void printFirma() {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {

            ResultSet rs = s.executeQuery("SELECT * FROM Firma");
            while (rs.next()) {
                int IdFil = rs.getInt("IdFir");
                String Naziv = rs.getString("Naziv");
                String Adresa = rs.getString("Adresa");
                String Tel1 = rs.getString("Tel1");
                String Tel2 = rs.getString("Tel2");

                System.out.println(String.format("%d\t%s\t%s\t%s\t%s", IdFil, Naziv, Adresa, Tel1, Tel2));
            }

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
    }
    
    public void printUkupnoPopravnki() 
    {try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {
 ResultSet rs = s.executeQuery("SELECT Sum(BrPopravljanja), Marka FROM Kamion GROUP BY Marka ORDER BY COUNT(*) DESC");
        while(rs.next()){
            
            int broj=rs.getInt(1);
            String naziv=rs.getString("Marka");
            System.out.printf("%s\t%d\n",naziv,broj);}
    }   catch (SQLException ex) {
             System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }

}

    public int zadatak(String imeiprezime,String Kategorija)
    {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {
            conn.setAutoCommit(false);
        PreparedStatement ps=conn.prepareStatement("INSERT INTO Zaposlen (IDZap,ImePrezime,Staz) VALUES (?,?,?)");
        ResultSet rs = s.executeQuery("SELECT Max(IDZap)+1 FROM Zaposlen");
        rs.next();
        int idz=rs.getInt(1);
        ps.setInt(1,idz);
        ps.setString(2,imeiprezime);
        ps.setInt(3, 0);
        ps.execute();
        
        
         PreparedStatement ps1=conn.prepareStatement("INSERT INTO Vozac (Kategorija,IDZap) VALUES (?,?)");
         ps1.setInt(2, idz);
         ps1.setString(1, Kategorija);
          ps1.execute();
         
         ResultSet rs1 = s.executeQuery("Select IDPut FROM Putovanje WHERE Status='N' EXCEPT SELECT IDPut FROM Vozi");
         if(rs1.next())
         { 
             int x=rs1.getInt(1);
             PreparedStatement ps3=conn.prepareStatement("INSERT INTO Vozi (IDZap,IDPut) VALUES (?,?)");
             ps3.setInt(1,idz);
             ps3.setInt(2, x);
             ps3.execute();
             System.out.println("Uspesna realizacija");
                conn.commit();
                conn.setAutoCommit(true);
             return x;
         }
         
         else
         {
             ResultSet rs2 = s.executeQuery("SELECT min(IDPut) FROM Putovanje WHERE Status='N'");
             if(rs2.next())
             { int x=rs2.getInt(1);
             PreparedStatement ps4=conn.prepareStatement("INSERT INTO Vozi (IDZap,IDPut) VALUES (?,?)");
             ps4.setInt(1,idz);
             ps4.setInt(2, x);
             ps4.execute();
             System.out.println("Uspesna realizacija");
                conn.commit();
                conn.setAutoCommit(true);
            return x;}
             
              System.out.println("Uspesna realizacija");
                conn.commit();
                conn.setAutoCommit(true);
            return -1;

         }
     
        
    }   catch (SQLException ex) { 
    
        System.out.println("Dogodila se greska");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
             
        }
    
}
}