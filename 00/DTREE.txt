/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTree;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sayantan
 */
class Helper
{
    int infoDCal(int data[][],int num_class)
    {
        int classCounts[]=new int[num_class];
        int total=data.length;
        
        for(int i=0;i<num_class;i++)
        {
            classCounts[i]=0;
        }
        
        for(int i=0;i<data.length;i++)
        {
            int className=data[i][data[i].length-1];
            classCounts[className]++;
        }
        
        int infoD=0;
        
        for(int i=0;i<num_class;i++)
        {
            double p=(double)classCounts[i]/(double)total;
            //log2(p)=log10(p)*log2(10)
            infoD+= -p*(Math.log10(p)*3.321928);
        }
        
        return infoD;
        
    }
    int attributeSelectoin(ArrayList<Integer> attributeList,int data[][],HashMap<Integer,Integer> cardinality,int num_class)
    {
        int min_req=1;
        int cur_best=-1;
        //check for each attribute
        for(int i=0;i<attributeList.size();i++)
        {
            //check for the division
            //|dj|/|d|gain(dj)
            int att=attributeList.get(i);
            int cardinalityA=cardinality.get(att);
            
            //keeps size of each dj created
            int eachValCount[]=new int[cardinalityA+1];
            
            for(int j=1;j<=cardinalityA;j++)
            {
                eachValCount[j]=0;
            }
            
            for(int j=0;j<data.length;j++)
            {
                data[j][att]++;
            }
            
            int dj[][][]=new int[cardinalityA+1][][];
            for(int j=1;j<=cardinalityA;j++)
            {
                dj[j]=new int[eachValCount[j]][data[0].length];
            }
            
            //create the partitions
        }
        
        return cur_best;
    }
}
public class DecisonTree 
{
    public static void main(String [] args)
    {
        
    }
    
}
