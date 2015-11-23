/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data_Cube_Computation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author sayantan
 */
class Helper
{
    int aggregate(int data[][])
    {
        int sum=0;
        
        for(int i=0;i<data.length;i++)
        {
            sum+=data[i][data[i].length-1];
        }
        
        return sum;
    }
    int count(int dim,int val,int data[][])
    {
        int sum=0;
        
        for(int i=0;i<data.length;i++)
        {
            if(data[i][dim]==val)
                sum++;
        }
        
        return sum;
    }
    void print(char[] a)
    {
        for(int i=0;i<a.length;i++)
            System.out.print(a[i]);
        
        System.out.print(" ");
    }
    
    HashMap<char[],Integer> BUC(int dim,int num_dim,int cardinality[],int min_sup,int dataCount[][],HashMap<char[],Integer> outRec,char current[],int data[][])
    {
        outRec.put(current,aggregate(data));
        
        print(current);
        System.out.println(aggregate(data));
        for(int d=dim;d<num_dim;d++)
        {
            int c=cardinality[d];
            
            int partitions[][][]=new int[c+1][][];
             int sizes[]=new int[c+1];
            
            for(int i=1;i<=c;i++)
            {
                sizes[i]=0;
                partitions[i]=new int[count(d,i,data)][num_dim+1];
            }
            
            //create the partitions
            for(int i=0;i<data.length;i++)
            {
                //d=a1 or a2 ..like that
                int v=data[i][d];
                
                for(int j=0;j<data[i].length;j++)
                {
                   partitions[v][sizes[v]][j]=data[i][j];
                }
                
                sizes[v]++;
            }
            
            int k=0;
            
            for(int i=1;i<=c;i++)
            {
                int cn=dataCount[d][i];
                
                if(cn>=min_sup)
                {
                    current[d]=(char)(i+48);
                    
                    outRec=BUC(d+1,num_dim,cardinality,min_sup,dataCount,outRec,current,partitions[i]);
                    
                    current[d]='*';
                } 
            }
        }
        
        
        return outRec;
    }
}
public class BUC 
{
    public static void main(String args[]) throws FileNotFoundException
    {
        Helper hl=new Helper();
        
        int tot_tran=9;
        int num_dim=3;
        int min_sup=3;
        int cardinality[]={ 4, 4 , 2};
        int dim=0;
        
        int data[][]=new int[tot_tran][num_dim+1];
        int dataCount[][]=new int[num_dim][];
        
        char current[]=new char[num_dim];
        for(int i=0;i<num_dim;i++)
        current[i]='*';
        
        HashMap<char[],Integer> outputRec=new HashMap<char[],Integer>();
        
        
        
        for(int i=0;i<num_dim;i++)
        {
            dataCount[i]=new int[cardinality[i]+1];
            
            //values are 1 2 3 ... cardinalty[i]
            for(int j=1;j<=cardinality[i];j++)
                dataCount[i][j]=0;
        }
        
        Scanner sc=new Scanner(new File("BUC_INPUT.txt"));
        
        //avoid the header
        sc.nextLine();
        
        for(int i=0;i<tot_tran;i++)
        {
            for(int j=0;j<=num_dim;j++)
            {
                data[i][j]=sc.nextInt();
                
                if(j!=num_dim)
                dataCount[j][data[i][j]]++;
            }
        }
  
        outputRec=hl.BUC(dim,num_dim,cardinality,min_sup,dataCount,outputRec,current,data);
    }
    
}
