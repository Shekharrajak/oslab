/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FpGrowthAlgo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 *
 * @author c137151
 */
class FPnode
{
    int value;
    int count;
    ArrayList<FPnode> links;//=new ArrayList<FPnode>();
    
    FPnode(FPnode f)
    {
        value=f.value;
        count=f.count;
        ArrayList<FPnode> links=new ArrayList<FPnode>();
        links=f.links;
    }
    
    FPnode(int v,int c)
    {
       value=v;
       count=c; 
       ArrayList<FPnode> links=new ArrayList<FPnode>();
       
       if(links==null)
            System.out.println("Ghapla 2222 ");
    }
    
    FPnode(int v,int c,ArrayList<FPnode> l)
    {
        value=v;
        count=c;
        ArrayList<FPnode> links=new ArrayList<FPnode>();
        links=l;
    }
}

class HelpingClass
{
    //calculates support for each set
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
    FPnode constructFPtree(ArrayList<Integer> order,int tran) throws FileNotFoundException
    {
        FPnode root=new FPnode(0,0);
        root.links=new ArrayList<FPnode>();
        
        Scanner sc=new Scanner(new FileInputStream("Transaction.txt"));
        String s=sc.nextLine();
        
        //take each transaction and calculate
        for(int i=0;i<tran;i++)
        {
           ArrayList<Integer> Dtran=new ArrayList<Integer>();
           
           //System.out.println("Tran No "+i);
           s=sc.next();
                  
           int item=sc.nextInt();
                  
           while(item!=-1)
           {
               //take the item only if its frequent
               if(order.contains(item))
               Dtran.add(item);
               
               item=sc.nextInt();
           }
           
           //order the items in the transaction
           ArrayList<Integer> tran_order=new ArrayList<Integer>();
           
           int count=0;
           for(int j=0;j<order.size();j++)
           {
               if(Dtran.contains(order.get(j)))
               {
                   tran_order.add(order.get(j));
                   count++;
               }
               
               //break if all the items are processed
               if(count==Dtran.size())
                   break;
           }
           
           
           //now add them in the tree
           //add each item
           //if current node contains a link for that then add and proceed else add a new node
           FPnode current=root;
           
           for(int j=0;j<=tran_order.size();j++)
           {
               
               if(j==tran_order.size())
               {
                   current.count++;
                   break;
               }
               //item to be added
               item=tran_order.get(j);
             //  System.out.println(item);
               
               ArrayList<FPnode> current_list=current.links;
               
              /* if(current.links==null)
               System.out.println("See");*/
               
               //search the full list of link for the current value
               boolean found=false;
               for(int k=0;k<current_list.size();k++)
               {
                   if((current_list.get(k)).value==item)
                   {
                       current.count++;
                       current=(current.links).get(k);
                       found=true;
                   }
               }
               
               //if not found add a node to the curent list
               if(!found)
               {
                   current.count++;
                   
                   FPnode node=new FPnode(item,0);
                   node.links=new ArrayList<FPnode>();
                   
                   current.links.add(node);
                   
                   current=(current.links).get(current.links.size()-1);
               }
           }
           
        }
        
        return root;
    }
    
    void printTree(FPnode root)
    {
        FPnode dummy=new FPnode(-1,-1);
        
        ArrayList<FPnode> reserve=new ArrayList<FPnode>();
        reserve.add(root);
        reserve.add(dummy);
        
        int pos=0;
        while(pos<reserve.size())
        {
            FPnode cur=reserve.get(pos);
            
            if(cur.value==-1)
            {
                System.out.println();
                if(pos!=reserve.size()-1)
                    reserve.add(dummy);
            }
            else
            {
                System.out.println(cur.value+" : "+cur.count);

                for(int i=0;i<cur.links.size();i++)
                {
                    reserve.add(cur.links.get(i));
                }
            }
            
            pos++;
        }
    }
}
public class FPTree 
{
    public static void main(String [] args) throws FileNotFoundException
    {      
        int TOT_TRAN=9;
        int MIN_SUP=2;
        
        HelpingClass hc=new HelpingClass();
        
        ArrayList<ArrayList<Integer> > uniqueList=new ArrayList<ArrayList<Integer> >();
        uniqueList=hc.getUniqueList(TOT_TRAN);
        
        
        ArrayList<Integer> order=new ArrayList<Integer>();
        ArrayList<Integer> orderCount=new ArrayList<Integer>();
        
        for(int i=0;i<uniqueList.size();i++)
        {
            
            int sup=hc.calSupport(uniqueList.get(i),TOT_TRAN);
            
            boolean put=false;
            //if frequent then put it in order list in descending order
            if(sup>=MIN_SUP)
            {
                int j;
                for(j=0;j<orderCount.size();j++)
                {
                    if(orderCount.get(j)<sup)
                    {
                        order.add(j,(uniqueList.get(i)).get(0));
                        orderCount.add(j,sup);
                        put=true;
                        break;
                    }
                }
                
                if(!put)
                {
                    //put the item(al is one itemset containing that one item only)
                    order.add((uniqueList.get(i)).get(0));
                    orderCount.add(sup);
                }
            }
        }
        
        FPnode root=hc.constructFPtree(order,TOT_TRAN);
        
        hc.printTree(root);
        
    }
    
}
