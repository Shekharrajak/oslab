/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Apriori_Algorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Scanner;

/**
 *
 * @author c137151
 */
class Assistant
{
    //calculates support for each set globally
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
    //calculates support for each set in that partition(Dtran)
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
    
     //returns all unique items
    ArrayList<ArrayList<Integer>> getUniqueList(ArrayList<ArrayList<Integer>> Dpart) throws FileNotFoundException
    {
         ArrayList<ArrayList<Integer>> C=new ArrayList<ArrayList<Integer>>();
         
         for(int i=0;i<Dpart.size();i++)
         {
             ArrayList<Integer> Dtran=new ArrayList<Integer>(Dpart.get(i));
             
             //put all items of this tran which are not in C 
             for(int j=0;j<Dtran.size();j++)
             {
                 ArrayList<Integer> oneItem=new ArrayList<Integer>();
                 oneItem.add(Dtran.get(j));
                 
                
                 if(!C.contains(oneItem))
                 {
                     C.add(oneItem);
                 }
             }
             
            
         }
        
         System.out.println("Size "+C.size());
         return C;
    }
    
    //generates candidate set from L(k-1)
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
     
     //returns item set with min_sup from candidate itemset
    ArrayList<ArrayList<Integer>> minSupSet(ArrayList<ArrayList<Integer>> C,double mult,int minSup,ArrayList<ArrayList<Integer>> Dpart) throws FileNotFoundException
    {
        ArrayList<ArrayList<Integer>> L=new ArrayList<ArrayList<Integer>>();
        
        Iterator iter=C.iterator();
        while(iter.hasNext())
        {
            ArrayList<Integer> al=new ArrayList<Integer>();
            al=(ArrayList)iter.next();
            
            if(Math.floor(calSupport(al,Dpart)*mult)>=minSup)
            {
                L.add(al);
            }
        }
        return L;
    }
}
public class PartitionApriori extends Assistant
{
    public static void main(String args[]) throws FileNotFoundException
    {
        PrintWriter p=new PrintWriter("OutAprioriPartition.txt");
        Assistant As=new Assistant();
        
       int partition_size=2;
       int MIN_SUP=2;
       int TOT_TRAN=9;
       double MULT=(double)TOT_TRAN/(double)partition_size;
       
       Scanner sc=new Scanner(new FileInputStream("Transaction.txt"));
       
       //ignore the headings
       String s=sc.nextLine();
       
       //no of transactions processed
       int picked=0;
       
       //global candidates
       ArrayList<ArrayList<Integer> > Lcandidates=new ArrayList<ArrayList<Integer> >();
       ArrayList<ArrayList<Integer> > GlobalFrequents=new ArrayList<ArrayList<Integer> >();
       
       while(picked<TOT_TRAN)
       {
           //start of the tran
           int start=picked;
           
           ArrayList<ArrayList<Integer> > Dpart=new  ArrayList<ArrayList<Integer> >();
           
           //no of transaction picked in this
           int part=0;
           
           while(part<partition_size && picked<TOT_TRAN)
           {
              //ignore the tran id
              s=sc.next();
              
              //pick the transaction in dtran
              ArrayList<Integer> Dtran=new ArrayList<Integer>();
              
              
              //pick all items upto -1,as -1 is the end
              int item=sc.nextInt();
              
              while(item!=-1)
              {
                  Dtran.add(item);
                  item=sc.nextInt();
              }
              
              //put this transaction in the partition
              Dpart.add(Dtran);
              
               
              //one tran for this partition as well as total processing is picked
               picked++;
               part++;
           }
           
           //generate local candidates
           
            //calculate the 1_ItemSets those have minimum support
            ArrayList<ArrayList<Integer> > L =new ArrayList<ArrayList<Integer>>();
            //candidate set
            ArrayList<ArrayList<Integer> > C =new ArrayList<ArrayList<Integer>>();
       
            C=As.getUniqueList(Dpart);
            
            System.out.println("Uniques! "+C.size());
           /* for(int i=0;i<C.size();i++)
            {
                ArrayList<Integer> xx=new ArrayList<Integer>(C.get(i));
                System.out.println(xx.get(0));
            }*/
            
           int k=1;
           
           System.out.println("Local sets for partitition "+(int)Math.ceil(picked/2));
           p.println("Local sets for partitition "+(int)Math.ceil(picked/2));
           
           while(!C.isEmpty())
           {   
               System.out.println("Set of size "+k);
               L=As.minSupSet(C,MULT,MIN_SUP,Dpart);
               
               Iterator out=L.iterator();

               p.println("Set of size "+k);

               while(out.hasNext())
               {
                   //al one set
                   ArrayList<Integer> al=new ArrayList<Integer>();
                   //al is a local frequent item set
                   al=(ArrayList<Integer>)out.next();
                   
                   //put all local frequent item sets for global evaluation
                   Lcandidates.add(al);

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

               p.println(-1);

               C=As.generateCandidate(L,k);
               k++;

               if(C.isEmpty())
               {
                   System.out.println("Empty Candidate set!!");
                   p.println("No bigger set possible!!\n");
               }
           }

       }
       
       p.println("Global frequent items:");
       System.out.println("Global frequent items:");
       
       for(int i=0;i<Lcandidates.size();i++)
       {
           if(As.calSupport(Lcandidates.get(i),TOT_TRAN) >= MIN_SUP)
           {
               if(!GlobalFrequents.contains(Lcandidates.get(i)))
               {
                   GlobalFrequents.add(Lcandidates.get(i));

                   //one global frequent itemset
                   ArrayList<Integer> Gfreq=new ArrayList<Integer>(Lcandidates.get(i));

                   for(int j=0;j<Gfreq.size();j++)
                   {
                       System.out.print(Gfreq.get(j)+" ");
                       p.print(Gfreq.get(j)+" ");
                   }

                   System.out.println();
                   p.println();
               }
           }
       }
       
       p.close();
    
    }
}
