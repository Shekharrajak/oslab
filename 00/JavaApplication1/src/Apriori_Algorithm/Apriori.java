/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*sayantan 137151*/
package Apriori_Algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.*;
import java.util.Scanner;


/**
 *
 * @author c137151
 */
class Calculation
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
    
    ArrayList<ArrayList<Integer>> generateCandidate(ArrayList<ArrayList<Integer>> L,int k)
    {
         ArrayList<ArrayList<Integer> > C =new ArrayList<ArrayList<Integer>>();
         
         Iterator iter=L.iterator();
         int index=0;
         
         //for each list search from its next list whether it can combine with any of them to form a
         //larger list  
         while(iter.hasNext())
         {
             ArrayList<Integer> al=new ArrayList<Integer>();
             al=(ArrayList<Integer>)iter.next();
             
             //check from its next
             int size=L.size();
             
             for(int j=index+1;j<size;j++)
             {
                 //first k-1 item of al2[j] and al should match
                 
                 ArrayList<Integer> al2=new ArrayList<Integer>();
                 al2=(ArrayList<Integer>)L.get(j);
                 
                 int matched=0;
                 for(matched=0;matched<k-1;matched++)
                 {
                     if(al.get(matched)!=al2.get(matched))
                        break;      
                 }
                 //System.out.println(matched);
                 if(matched == k-1)
                 {
                     //to maintain the sorted order put the larger elem at the end
                     if(al.get(k-1) > al2.get(k-1))
                     {
                        ArrayList<Integer> newal=new ArrayList<Integer>(al2); 
                        newal.add(al.get(k-1));
                        
                        if(prune(L,newal))
                        C.add(newal);
                     }
                     else
                     {
                         ArrayList<Integer> newal=new ArrayList<Integer>(al);
                         newal.add(al2.get(k-1));
                         
                         if(prune(L,newal))
                         C.add(newal);
                     }
        
                 }
             }
             
             index++;
             
         }
         
         
         return C;
    }
    
    
    ArrayList<ArrayList<Integer>> minSupSet(ArrayList<ArrayList<Integer>> C,int minSup,int tran) throws FileNotFoundException
    {
        ArrayList<ArrayList<Integer>> L=new ArrayList<ArrayList<Integer>>();
        
        Iterator iter=C.iterator();
        while(iter.hasNext())
        {
            ArrayList<Integer> al=new ArrayList<Integer>();
            al=(ArrayList)iter.next();
            
            if(calSupport(al,tran)>=minSup)
            {
                L.add(al);
            }
        }
        return L;
    }
    
    ArrayList<ArrayList<Integer>> getUniqueList(int tran) throws FileNotFoundException
    {
         ArrayList<ArrayList<Integer>> C=new ArrayList<ArrayList<Integer>>();
         
         Scanner sc=new Scanner(new FileInputStream("Transaction.txt"));
         sc.nextLine();
         
         for(int i=0;i<tran;i++)
         {
             //System.out.println("Tran No "+i);
             String s=sc.next();
             
             int item=sc.nextInt();
             while(item!=-1)
             {
                 //System.out.println("Item No "+item);
                ArrayList<Integer> al=new ArrayList<Integer>();
                al.add(item);
                
                if(!C.contains(al))
                    C.add(al);
                
                item=sc.nextInt();
             }
         }
          
          
         sc.close();
         return C;
    }
    
    boolean prune(ArrayList<ArrayList<Integer>> L,ArrayList<Integer> al)
    {
        int size=al.size();
        boolean flag=true;
        
        //check all k-1 set present in L(k-1) or not
        //for that from k-item set each time one one element is removed and checked
        for(int i=0;i<size;i++)
        {
            ArrayList<Integer> newal=new ArrayList<Integer> (al);
            newal.remove(i);
            
            //if any set is not present its superset can't be frequent
            //so return false immediately
            if(!L.contains(newal))
            {
                    flag=false;
                    break;
            }
            
        }
        
        return flag;
    }
    
}
public class Apriori extends Calculation
{
    
    
    public static void main(String [] args) throws FileNotFoundException
    {
       PrintWriter p=new PrintWriter("OutApriori.txt");
       
       //Minimum support and max-no-of-item in one transaction
       int MIN_SUP=2;
       //int MAX_ITEM_IN_A_TRAN=4;
       int TOT_TRAN=9;
       //int TOT_ITEM=5;
       
       Calculation Cal=new Calculation();
       
       //calculate the 1_ItemSets those have minimum support
       ArrayList<ArrayList<Integer> > L =new ArrayList<ArrayList<Integer>>();
       //candidate set
       ArrayList<ArrayList<Integer> > C =new ArrayList<ArrayList<Integer>>();
       
       C=Cal.getUniqueList(TOT_TRAN);
       
       int k=1;
       while(!C.isEmpty())
       {   
           System.out.println("Set of size "+k);
           L=Cal.minSupSet(C,MIN_SUP,TOT_TRAN);
           
           Iterator out=L.iterator();
           
           p.println("Set of size "+k);
           
           while(out.hasNext())
           {
               //al one set
               ArrayList<Integer> al=new ArrayList<Integer>();
               al=(ArrayList<Integer>)out.next();
               
               //all k items in set al
               Iterator in=al.iterator();
               while(in.hasNext())
               {
                   Integer I=(Integer)in.next();
                   System.out.print(I+" ");
                   p.print(I+" ");
               }
               p.println();
               System.out.println();
               
           }
           
           C=Cal.generateCandidate(L,k);
           k++;
           
           if(C.isEmpty())
           {
               System.out.println("Empty Candidate set!!");
               p.println("No bigger set possible!!\n");
           }
       }
      
       p.close();
       
    }
}
