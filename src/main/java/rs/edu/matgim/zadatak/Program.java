package rs.edu.matgim.zadatak;

import java.sql.SQLException;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) throws SQLException {

                DB _db = new DB();
        _db.printUkupnoPopravnki();
        Scanner sc=new Scanner(System.in);
        System.out.println("Unesi ime:");
        String i=sc.next();
        System.out.println("Unesi kategoriju:");
        String j=sc.next();


        _db.zadatak(i, j);
        
    }
}
