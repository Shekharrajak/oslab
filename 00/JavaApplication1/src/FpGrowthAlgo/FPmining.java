/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FpGrowthAlgo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author c137151
 */

class PatternBaseNode
{
    //the list prefixes
    //in the hashmap key is the prefix-list and its value is the frequency
    //example
    //5:(<1,2,3>:2,<2,4,5>:3)
    //5 is the id
    HashMap<ArrayList<Integer>,Integer> list=new HashMap<ArrayList<Integer>,Integer>();
    
    PatternBaseNode(ArrayList<Integer> prefix,int count)
    {
        list.put(new ArrayList<Integer>(prefix), count);
    }
}
class HelpIt
{
    //each id
    //5-> 5:(<1,2,3>:2,<2,4,5>:3)
   public HashMap<Integer,PatternBaseNode> patBase=new HashMap<Integer,PatternBaseNode>();
    
    //tillNow keeps tracs the nodes visited in the dfs till now and freq keeps the count of the prefix
   void formPatternsBase(FPnode root,ArrayList<Integer> tillNow)
   {
       /*System.out.println(root.links.size());*/
       
       for(int i=0;i<root.links.size();i++)
       {
           FPnode link=root.links.get(i);
           
           /*System.out.println(link.value);*/
          // System.out.println(patBase.get(link.value));
           
          /* if(link.value==5)
           {
              printArrayList(tillNow);
             System.out.println();
           }*/
           
           if(patBase.containsKey(link.value))
           {
              /* if(link.value==5)
                {
                        printArrayList(tillNow);
                        System.out.println();
                }*/
               PatternBaseNode present=patBase.get(link.value);
               
               if(present.list.containsKey(tillNow))
               {
                   present.list.put(new ArrayList(tillNow),present.list.get(tillNow)+link.count);
               }
               else                  
               present.list.put(new ArrayList(tillNow),link.count);
           }
           else
           {
               /*System.out.println("Here");*/
               //add this node
               PatternBaseNode addIt=new PatternBaseNode(tillNow,link.count);
               /*System.out.println("Here");*/
               patBase.put(link.value, addIt);
               /*System.out.println("Here");*/
           }
           
           
           tillNow.add(link.value);
          
           formPatternsBase(link,tillNow);
           
           if(tillNow.size()!=0)
           tillNow.remove(tillNow.size()-1);
           
       }
           
       
       //return patBase;
   }
   
   void printArrayList(ArrayList<Integer> al,boolean file,PrintWriter p) throws FileNotFoundException
   {
        
       for(int i=0;i<al.size();i++)
       {
           System.out.print(al.get(i)+" ");
           if(file)
               p.print(al.get(i)+" ");
       }
       
       System.out.println();
       if(file)
       {
            p.println();
            // p.close();
       }
   }
   
   void printPatternsBase(HashMap<Integer,PatternBaseNode> PatternsBase,PrintWriter p) throws FileNotFoundException
   {
       Set mainSet=PatternsBase.entrySet();
       Iterator iter=mainSet.iterator();
       
       while(iter.hasNext())
        {
             Entry<Integer,PatternBaseNode> ent=(Entry<Integer,PatternBaseNode> )iter.next();
             
             //System.out.println("Id: "+ent.getKey());
             
             //ent.getValue returns PatternBaseNode which has a memeber called list
             //list is HashMap<ArrayList<Integer>,Integer>
             //we ahve to print all the elements in 
             Set st=ent.getValue().list.entrySet();
             Iterator it=st.iterator();
             
             while(it.hasNext())
             {
                 Entry <ArrayList<Integer>,Integer> e=(Entry <ArrayList<Integer>,Integer>)it.next();
              
                 //System.out.println(e.getKey().size());
                 
                /* printArrayList((ArrayList<Integer>)e.getKey(),false,p);
                   System.out.println(": "+e.getValue());*/
             }
             
        }
   }
   
   ArrayList<ArrayList<Integer>> getUniqueList(ArrayList<ArrayList<Integer>> Dpart) throws FileNotFoundException
    {
         int tran=Dpart.size();
         
         ArrayList<ArrayList<Integer>> C=new ArrayList<ArrayList<Integer>>();
       
         for(int i=0;i<tran;i++)
         {
             for(int j=0;j<Dpart.get(i).size();j++)
             {
                 //System.out.println("Item No "+item);
                ArrayList<Integer> al=new ArrayList<Integer>();
                al.add(Dpart.get(i).get(j));
                
                if(!C.contains(al))
                    C.add(al);
             }
         }
  
         return C;
    }
    int calSupport(ArrayList<Integer> al,ArrayList<ArrayList<Integer> >Dpart) throws FileNotFoundException
    {
        int no_of_tran=Dpart.size();
        
        int sup=0;
        
        for(int i=0;i<no_of_tran;i++)
        {
            //put all items of the tran in array-list
            ArrayList items=new ArrayList(Dpart.get(i));
            
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
       
        return sup;
    }
    FPnode constructFPtree(ArrayList<Integer> order,ArrayList<ArrayList<Integer>> Dpart) throws FileNotFoundException
    {
        FPnode root=new FPnode(0,0);
        root.links=new ArrayList<FPnode>();
        int tran=Dpart.size();
        
        //take each transaction and calculate
        for(int i=0;i<tran;i++)
        {
           ArrayList<Integer> Dtran=new ArrayList<Integer>(Dpart.get(i));
           
         
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
               int item=tran_order.get(j);
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
    //all susets of the prefix
    ArrayList<ArrayList<Integer>> getCombo(ArrayList<Integer> list)
    {
        ArrayList<ArrayList<Integer>> allCmbo=new ArrayList<ArrayList<Integer>>();
        
        int n=(int)Math.pow((double)2,list.size());
        
        for(int i=1;i<n;i++)
        {
            ArrayList<Integer> al=new ArrayList<Integer>();
            
            int p=i,j=0;
            while(p>0)
            {
                if(p%2==1)
                {
                    al.add(list.get(j));
                }
                j++;
                p/=2;
            }
            
            if(!allCmbo.contains(al))
            allCmbo.add(al);
        }
        
        return allCmbo;
    }
    
    //totFreqList keeps all the frequentitemsets with the suffix same as the argument suffix
    ArrayList<ArrayList<Integer> > traverse(ArrayList<Integer> prefix,FPnode root,int suffix,int min_sup,ArrayList<ArrayList<Integer>> totFreqList)
    {
        //whether to go down further this tree or not
        //at any time if the count goes less than min_sup no need to go further in that branch
        boolean Send=false;
        
        if(root.value==0 && root.count >= min_sup)
        {
            ArrayList<Integer> al=new ArrayList<Integer>();
            al.add(suffix);
            
            if(!totFreqList.contains(al))
            {
                totFreqList.add(al);
            }
            
            Send=true;
        }
        else if(root.count >= min_sup)
        {
            if(!prefix.contains(root.value))
            prefix.add(root.value);
            
            Send=true;
        }
        
        
        
        //we need to take all combination of elements of each path
        //a path may end because it encountered an infrequent prefix
        //or it may end due to having no child but still being a frequent prefix
        
        //if frequent
        if(Send)
        {
            //if prefix is frequent and having child,then proceed further down the path
            if(root.links.size()!=0)
            {
                for(int i=0;i<root.links.size();i++)
                {
                    totFreqList=traverse(prefix,root.links.get(i),suffix,min_sup,totFreqList);
                }
            }
            //if the item is frequent but the last node on the path(no child)...take all combo
            else
            {
                
                ArrayList<ArrayList<Integer>> allCmbo=getCombo(prefix);

                for(int i=0;i<allCmbo.size();i++)
                {
                    ArrayList<Integer> toPut=new ArrayList<>(allCmbo.get(i));
                    toPut.add(suffix);
                    
                    toPut.sort(null);

                    if(!totFreqList.contains(toPut))
                    totFreqList.add(toPut);
                } 
            }
        }
        else//if the path is ending due to having an infrequent prefix
        {
            ArrayList<ArrayList<Integer>> allCmbo=getCombo(prefix);

            for(int i=0;i<allCmbo.size();i++)
            {
                ArrayList<Integer> toPut=new ArrayList<>(allCmbo.get(i));
                toPut.add(suffix);
                    
                toPut.sort(null);

                if(!totFreqList.contains(toPut))
                totFreqList.add(toPut);
            } 
            
        }
        return totFreqList;
    }
    
}
public class FPmining //extends FPTree
{
    public static void main(String args[]) throws FileNotFoundException
    {
       PrintWriter p=new PrintWriter("FPminig.txt");
       
        //copy-pasted FPTree.java to form the root
        
       /*************************************************************************************************/
       /*************************************************************************************************/
        int TOT_TRAN=9,MIN_SUP=2;
        ArrayList<Integer> order=new ArrayList<Integer>();
        ArrayList<Integer> orderCount=new ArrayList<Integer>();
        
        HelpingClass hc=new HelpingClass();
        HelpIt hi=new HelpIt();
        
        ArrayList<ArrayList<Integer> > uniqueList=new ArrayList<ArrayList<Integer> >();
        uniqueList=hc.getUniqueList(TOT_TRAN);
        
        
        
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
      
       /*************************************************************************************************/ 
       /*************************************************************************************************/
        
       //here patternsbse is created
        
        HashMap<Integer,PatternBaseNode> PatternsBase=new HashMap<Integer,PatternBaseNode>();
        
        hi.formPatternsBase( root,new ArrayList<Integer>());
        
        PatternsBase=hi.patBase;
        
        
        //System.out.println("PatternsBase");
        
       hi.printPatternsBase(PatternsBase,p);
        
        /*************************************************************************************************/
        /*************************************************************************************************/
        
        //noe mine the patterns base
        
        //takes the whole patersBase in it
        Set Base=PatternsBase.entrySet();
        
        Iterator it=Base.iterator();
        
        while(it.hasNext())
        {
            //for each pattern again form a tree
            Entry<Integer,PatternBaseNode> e=(Entry<Integer,PatternBaseNode>)it.next();
            
            int currentItem=e.getKey();
            
            ArrayList<ArrayList<Integer>> Dpart=new ArrayList<ArrayList<Integer>>();
            
            //the hashMap for the prefixes of currentItem
            Set eachpat=e.getValue().list.entrySet();
            
            Iterator iter=eachpat.iterator();
            
            //now put all the item lists
            //treat as if they are transaction
            while(iter.hasNext())
            {
                Entry <ArrayList<Integer>,Integer> e2=(Entry <ArrayList<Integer>,Integer>)iter.next();
                
                //put the number of times they occured
                for(int k=0;k<e2.getValue();k++)
                Dpart.add(e2.getKey());
            }
            
            ArrayList<Integer> localorder=new ArrayList<Integer>();
            ArrayList<Integer> localorderCount=new ArrayList<Integer>();

            ArrayList<ArrayList<Integer> > localuniqueList=new ArrayList<ArrayList<Integer> >();
            localuniqueList=hi.getUniqueList(Dpart);



            for(int i=0;i<localuniqueList.size();i++)
            {

                int sup=hi.calSupport(localuniqueList.get(i),Dpart);

                boolean put=false;
                //if frequent then put it in order list in descending order
                if(sup>=MIN_SUP)
                {
                    int j;
                    for(j=0;j<localorderCount.size();j++)
                    {
                        if(localorderCount.get(j)<sup)
                        {
                            localorder.add(j,(localuniqueList.get(i)).get(0));
                            localorderCount.add(j,sup);
                            put=true;
                            break;
                        }
                    }

                    if(!put)
                    {
                        //put the item(al is one itemset containing that one item only)
                        localorder.add((localuniqueList.get(i)).get(0));
                        localorderCount.add(sup);
                    }
                }
            }

            FPnode localroot=hi.constructFPtree(localorder,Dpart);
            
            System.out.println("CurretnItem "+currentItem);
            p.println("For Item Suffix :"+currentItem);
            hc.printTree(localroot);
            
            
            //Now traverse the localfptree and generate frequent itemsets
            ArrayList<ArrayList<Integer> > fal=hi.traverse(new ArrayList<Integer>(),localroot,currentItem,MIN_SUP,new ArrayList<ArrayList<Integer>>());
            
            for(int x=0;x<fal.size();x++)
            {
                hi.printArrayList(fal.get(x),true,p);
            }
        }
        /**************************************************************************************************/
        /**************************************************************************************************/
        p.close();
    }
    
}
