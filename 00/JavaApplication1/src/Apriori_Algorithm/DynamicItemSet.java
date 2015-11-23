/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Apriori_Algorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Scanner;

/**
 *
 * @author c137151
 */
class Helpers
{
    //calculate support for an  item set in this partition
    int calSupport(ArrayList<Integer> al,ArrayList<ArrayList<Integer>> Dpart) throws FileNotFoundException
    {
        //support for the set
        int sup=0;
        
        for(int i=0;i<Dpart.size();i++)
        {
            //put all items of the tran in array-list
            ArrayList Dtran=new ArrayList(Dpart.get(i));
        
            //check for all items in the list is present in the transaction
            //ie- items list contains or not
            
            ListIterator iter=al.listIterator();
            boolean allfound=true;
            
            while(iter.hasNext())
            {
                allfound=allfound & Dtran.contains(iter.next());
            }
            
            //if all the items are in this tran then increase support
            if(allfound)
                sup++;    
            
        } 
        return sup;
    }
    
    //generates candidate set from L(k-1)
    ArrayList<ArrayList<Integer>> generateCandidateandResolve(int checkpoint,int tran,ArrayList<ArrayList<Integer> > C,int min_sup) throws FileNotFoundException
    {            
         ArrayList<ArrayList<Integer> > L =new ArrayList<ArrayList<Integer>>();
         ArrayList<ArrayList<Integer> > sureInfrequent=new ArrayList<ArrayList<Integer> >();
         
         //keeps counts of itemset
         HashMap<ArrayList<Integer> , Integer> hmCount=new HashMap<ArrayList<Integer> , Integer>();
         
         //checked for how many transactions
         HashMap<ArrayList<Integer> , Integer> hmChecked=new HashMap<ArrayList<Integer> , Integer>();
         
         Scanner sc=new Scanner(new FileInputStream("Transaction.txt"));
         
         //to ignore the headings
         String s=sc.nextLine();
         
        int pos=0;
        while(C.size()!=0)
        {
             int count=0;
             //take checkpoint no. of items to generate candidate
              ArrayList<ArrayList<Integer> > Dpart=new ArrayList<ArrayList<Integer> >();
              
              while(count<checkpoint)
              {
                  //to avoid the tranID
                  s=sc.next();
                  
                  //take one transaction
                  ArrayList<Integer> Dtran=new ArrayList<Integer>();
                  
                  int item=sc.nextInt();
                  
                  while(item!=-1)
                  {
                      Dtran.add(item);
                      item=sc.nextInt();
                  }
                  
                  Dpart.add(Dtran);
      
                  count++;
                  pos++;
                  
                  //if EOF
                  if(pos==tran)
                  {
                      pos=0;
                      
                      sc.close();
                      sc=new Scanner(new FileInputStream("Transaction.txt"));
                      
                      //avoid heading
                      s=sc.nextLine();
                      
                      break;
                  }
              }
             
              //container for new candidates
              ArrayList<ArrayList<Integer>> cBackUp=new ArrayList<ArrayList<Integer>>(C);
              //check for all candidates in c
              //once they are found frequent they are put in L
              for(int i=0;i<C.size();i++)
              {
                  ArrayList<Integer> itemSet=new ArrayList<Integer>(C.get(i));
                  
                  //if the candidate is already entered but not in frequent list see if its count can be increased
                  //else make an entry of the item with count for the current partition
                  if(!hmCount.containsKey(itemSet))
                  {
                      hmCount.put(itemSet,calSupport(itemSet,Dpart));
                      
                      //will be chekced for 'count' number of partitions
                      hmChecked.put(itemSet,count);
                  }
                  else
                  {
                      //add count for this partition with count so far
                      int count_so_far=hmCount.get(itemSet);
                      int current_count=count_so_far+calSupport(itemSet,Dpart);
                      
                      hmCount.put(itemSet, current_count);
                      
                      int old_checked=hmChecked.get(itemSet);
                      int new_checked=old_checked+count;
                      
                      hmChecked.put(itemSet, new_checked);
                  }
               
                      //if the cuurent_count is greater than min_sup put it in L as its frequent
                      //and remove from c and add candidates that come from this frequent set
                  
                      if(hmCount.get(itemSet)>=min_sup)
                      {
                          /*If total count is required this can be commented*/
                          cBackUp.remove(itemSet);
                          hmCount.remove(itemSet);
                          
                          
                          //add this set
                           L.add(itemSet);
                                                 
                          //add candidates
                          //check for all same size sets in L
                          for(int j=0;j<L.size();j++)
                          {
                              ArrayList<Integer> set=L.get(j);
                              
                              if(set.size()==itemSet.size() && !set.equals(itemSet))
                              {
                                  ArrayList<Integer> newCand=new ArrayList<Integer>();
                                  
                                  int k=itemSet.size();
                                  //if their first k-1 items are equal
                                  int m=0;
                                  for(m=0;m<k-1;m++)
                                  {
                                      if(set.get(m)!=itemSet.get(m))
                                          break;
                                      else
                                          newCand.add(itemSet.get(m));
                                  }
                                  
                                  //first k-1 items are equal the only new can it put with the two k-th items
                                  //from each set
                                  if(m==k-1)
                                  {
                                      //maintain sorted order
                                     if(itemSet.get(k-1)<set.get(k-1))
                                     {
                                         newCand.add(itemSet.get(k-1));
                                         newCand.add(set.get(k-1));
                                     }
                                     else if(itemSet.get(k-1)>set.get(k-1))
                                     {
                                         newCand.add(set.get(k-1));
                                         newCand.add(itemSet.get(k-1));
                                     }
                                      
                                     //put the new candidate in C after prunig test is passed
                                     if(prune(sureInfrequent,newCand))
                                         cBackUp.add(newCand);
                                     else
                                         sureInfrequent.add(newCand);
                                     
                                  }
                              }
                              
                          }
                          
                      }                    
                      //check if the count for the itemset is equal to number of ransactions
                      //That means its checked for all transactions.And still not frequent                  
                      else if (hmChecked.get(itemSet)>=tran)
                      {
                          cBackUp.remove(itemSet);
                          sureInfrequent.add(itemSet);
                      }                  
                  
              }
              C=new ArrayList<ArrayList<Integer>>(cBackUp);
              
        }
         
        return L;
    }
    
    boolean prune(ArrayList<ArrayList<Integer>> sureInfrequent,ArrayList<Integer> al)
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
            if(sureInfrequent.contains(newal))
            {
                    flag=false;
                    break;
            }
            
        }
        
        return flag;
    }
    
    //returns all unique items
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
    
}
public class DynamicItemSet 
{
    public static void main(String args[]) throws FileNotFoundException
    {
       Helpers helper=new Helpers();
        
       int CheckPoint=2;
        
       //Minimum support and max-no-of-item in one transaction
       int MIN_SUP=2;
       
       //Total number of transaction
       int TOT_TRAN=9;
       
       ArrayList<ArrayList<Integer>> Freq=new ArrayList<ArrayList<Integer>>();
       ArrayList<ArrayList<Integer>> C=new ArrayList<ArrayList<Integer>>();
       
       //take all unique itams as candidate primarily
       C=helper.getUniqueList(TOT_TRAN);
       
       Freq=helper.generateCandidateandResolve(CheckPoint,TOT_TRAN,C,MIN_SUP);
       
       PrintWriter p=new PrintWriter("DIC_Output.txt");
       
       p.println("Frequent item sets are:");
       System.out.println("Frequent item sets are:");
       
       for(int i=0;i<Freq.size();i++)
       {
           ArrayList<Integer> item=new ArrayList<Integer>(Freq.get(i));
           
           //print all the items
          // p.print("< ");
           for(int j=0;j<item.size();j++)
           {
               p.print(item.get(j)+" ");
               System.out.print(item.get(j)+" ");        
           }
           //p.print(">");
           p.println();
           System.out.println();
       }
       
       p.close();
       
    }
}
