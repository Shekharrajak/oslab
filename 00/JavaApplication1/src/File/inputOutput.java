package File;

import java.io.*;
import java.util.*;

public class inputOutput
{
    public static void main(String args[]) throws FileNotFoundException
    {
        PrintWriter p=new PrintWriter("Transaction.txt");
        
        p.print("10 12 15 26 15 ");
        p.print("Sayantan Ghosh!");;
        p.println(" NITW");
        p.print("Cse!");
        p.close();
        
        Scanner sc=new Scanner(new File("MyFile.txt"));
        
        int s=sc.nextInt();
        s=sc.nextInt();
        
        System.out.println(s);
        
        
        
    }
    
}
