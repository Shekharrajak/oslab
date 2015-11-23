/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Apriori_Algorithm;

import java.io.*;
import java.util.*;
import java.math.*;

/**
 *
 * @author c137151
 */
class Helper
{
    
    int calSupport(ArrayList<Integer> al,int no_of_tran) throws FileNotFoundException
    {
        Scanner sc=new Scanner(new FileInputStream("Transaction.txt"));
        
        //to avoid headings
        String heading=sc.nextLine();
        
        //support for the set
        int sup=0;
        
        for(int i=0;i<no_of_tran;i++)
        {
            //ignore tranid
            String tran=sc.next();
            
            //put all items of the tran in array-list
            ArrayList items=new ArrayList();
            int item=sc.nextInt();
            while(item!=-1)
            {
                items.add(item);
                item=sc.nextInt();
            }
            
            //check for all items in the list is present in the transaction
            //ie- items list contains or not
            
            ListIterator iter=al.listIterator();
            boolean allfound=true;
            
            while(iter.hasNext())
            {
                allfound=allfound & items.contains(iter.next());
            }
            
            //if all the items are in this tran then increase support
            if(allfound)
                sup++;
            
            
        }
        
        sc.close();
        
        return sup;
    }
    
}
public class AssociationRules 
{
    public static void main(String [] args) throws FileNotFoundException
    {
        Scanner sc=new Scanner(new File("HashOutApriori.txt"));
        Helper h=new Helper();
        
        
        int TOT_TRAN=9;
        double MIN_ASSOC=0.5;
        
        ArrayList<ArrayList<Integer> > Result=new ArrayList<ArrayList<Integer>>();
        
        String s=sc.nextLine();
        int setsize=0;
        
        //avoid the one item set
        while(!s.equals("No bigger set possible!!"))
        {
            setsize++;
              
            //avoid 1-item sets
            if(s.equals("Set of size 1"))
            {
                while(true)
                {
                    int item=sc.nextInt();
                    
                    if(item==-1)
                        break;

                    while(item!=-1)
                    {     
                        item=sc.nextInt();
                    }
                }
            }
            else
            { 
                //no end of k-item sets
                while(true)
                {
                    ArrayList<Integer> al=new ArrayList<Integer>();
                    
                    int item=sc.nextInt();
                    
                    //if first item is -1 then this k-item set ends
                    if(item== -1)
                        break;
                    
                    while(item!=-1)
                    {
                        al.add(item);
                        item=sc.nextByte();
                    }
                    
                    Result.add(al);
                }
                
                
            }
            
            //System.out.println(s+" "+Result.size());
                      
            //to avoid the \n after -1
            s=sc.nextLine();
            //see for next larger size
            s=sc.nextLine(); 
           
        } 
        sc.close();
        
        PrintWriter p=new PrintWriter("AssocRules.txt");
        //generate assoc rules
        for(int i=0;i<Result.size();i++)
        {
            //for each item set
            ArrayList<Integer> myal=new ArrayList<Integer>(Result.get(i));
            int n=myal.size();
            int lim=(int)Math.pow(2, n);
            
            for(int v=1;v<lim-1;v++)
            {
                ArrayList<Integer> al1=new ArrayList<Integer>();
                ArrayList<Integer> al2=new ArrayList<Integer>();
                //take the binary bits
                //if 1 push in al1 else push in al2
                //generate assoc for al1=>al2
                
                int v1=v;
                int d=0;
                while(v1 > 0)
                {
                    if((v1&1)==1)
                    {
                        al2.add(myal.get(d));
                    }
                    else
                    {
                         al1.add(myal.get(d));
                    }
                        
                    v1=v1/2;
                    d++;
                }
                
                //put the rest elements in al1
                while(d<n)
                {
                    al1.add(myal.get(d));
                    d++;
                }
                
                double assoc=(double)((double)h.calSupport(myal,TOT_TRAN)/(double)h.calSupport(al1,TOT_TRAN));
                
                //if(assoc>=MIN_ASSOC)
                //{
                    //print al1
                    p.print("(");
                    
                    for(int xx=0;xx<al1.size();xx++)
                    {
                        p.print(al1.get(xx)); 
                        
                        if(xx!=al1.size()-1)
                            p.print(",");
                    }
                    
                     p.print(")");
                     
                     p.print("=>");
                     
                     //print al2
                     p.print("(");
                    
                    for(int xx=0;xx<al2.size();xx++)
                    {
                        p.print(al2.get(xx)); 
                        
                        if(xx!=al2.size()-1)
                            p.print(",");
                    }
                    
                     p.print(") ");
                     
                     p.println(" Has Confidence= "+(int)(assoc*100)+"%");
                     
                //}
            }
            
        }
        
        p.close();
        
    }
    
}