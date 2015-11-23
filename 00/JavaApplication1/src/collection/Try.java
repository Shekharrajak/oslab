/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sayantan
 */
public class Try 
{
    public static void main(String args[])
    {
        ArrayList al=new ArrayList();
        
        al.add(7);
        al.add(0,"la");
        al.add(true);
        
        Iterator i=al.iterator();
        
        while(i.hasNext())
        {
            System.out.println(i.next());
        }
        
        HashMap hm=new HashMap();
        
        hm.put("AB",5);
        hm.put(5, "kk");
        
        Set set=hm.entrySet();
        
        i = set.iterator();
        
        while(i.hasNext())
        {
            Map.Entry me=(Map.Entry)i.next();
            
            System.out.println(me.getValue()+" : "+me.getKey());
        }
        
        int a[]={1,5,9,4,3};
        
        Arrays.sort(a);
        
        for(int x=0;x<5;x++)
            System.out.println(a[x]);
        
        
    }
}
