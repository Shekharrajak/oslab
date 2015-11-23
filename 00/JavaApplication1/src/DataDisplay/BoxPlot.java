/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataDisplay;

import java.applet.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author c137151
 */
/*class helper
{
    int q1,q2,q3,q4,iqr,lim,low,high;
}*/
class Helper
{
    //nor,malized to fall b/w 50-550
    ArrayList<Integer> normalize(ArrayList<Integer> al)
    {
        int min=al.get(0);
        int max=al.get(al.size()-1);
        int range=400;
          
        ArrayList<Integer> normalize=new ArrayList<Integer>();
        
        for(int i=0;i<al.size();i++)
        {
            int x=al.get(i);
            x=((x-min)*range)/(max-min)+50;
            normalize.add(x);
            
            //System.out.print(x+" ");
        }
       // System.out.println();
        return normalize;
    }
    
    ArrayList<Integer> sort(ArrayList<Integer> al)
    {
        ArrayList<Integer> toPut=new ArrayList<Integer>();
        
        for(int i=0;i<al.size();i++)
        {
            int j=0;
            while(j<toPut.size() && toPut.get(j)<al.get(i))
                j++;
            if(j<toPut.size())
                toPut.add(j,al.get(i));
            else
                toPut.add(al.get(i));
        }
        
        return toPut;
    }
}
public class BoxPlot extends Applet implements Runnable
{
    Thread t=null;
    ArrayList<ArrayList<Integer>> data=new ArrayList<ArrayList<Integer>>();
    int q1,median,q3,q4,iqr,lim,low,high;
    
    @Override
    public void init()
    {
        setBackground(Color.GRAY);
      //  setForeground(Color.GRAY);
        setSize(new Dimension(1400,700));
    }
    
    public void start()
    {
        Scanner sc;
        
        try 
        {
            sc=new Scanner(new FileInputStream("List.txt"));
            
            //number of data sets
            int n=sc.nextInt();
            
            for(int i=0;i<n;i++)
            {
                ArrayList<Integer> al=new ArrayList<Integer>();
                int x=sc.nextInt();
                
                while(x!=-1)
                {
                    al.add(x);
                    
                    x=sc.nextInt();
                }
                
                ArrayList<Integer> newal=new ArrayList<Integer>(new Helper().sort(al));
                
                /*for(int j=0;j<newal.size();j++)
                    System.out.print(newal.get(j)+" ");
                
                System.out.println();*/
                
                ArrayList<Integer> norm=new ArrayList<Integer>(new Helper().normalize(newal));
                
                data.add(new ArrayList<Integer>(norm));
            }
            
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(BoxPlot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*for(int i=0;i<data.size();i++)
        {
             for(int j=0;j<data.get(i).size();j++)
                    System.out.print(data.get(i).get(j)+" ");
             System.out.println("End");
        }*/
        t=new Thread(this);
        t.start();
    }
    
    
    @Override
    public void run() 
    {
        repaint();
    }
    
    public void paint(Graphics g)
    {
        Dimension d=getSize();
        int n=data.size(),x=10,y;
        int limy=(int)getSize().getHeight();
        
        int perx=50;//(int) Math.floor(d.getWidth()/(2*n));
        
        int yaxis=limy;
        while(yaxis>0)
        {
            g.drawString(yaxis+"", x,limy-yaxis);
            yaxis-=20;
        }
        
        
        
       x=50;
       for(int i=0;i<n;i++)
       {
            ArrayList<Integer> al=new ArrayList<Integer>(data.get(i));
            
           System.out.println("Data Set "+(int)(i+1));
           for(int j=0;j<al.size();j++)
                System.out.print(al.get(j)+" ");
            
            System.out.println();
            
            int size=al.size();
            
            if(size%2==1)
            {
                median=al.get(size/2);
            }
            else
            {
                median=(al.get(size/2-1)+al.get(size/2))/2;
            }
            
            q1=al.get((size/4-1));
            q3=al.get((3*size/4-1));
            iqr=q3-q1;
            lim=(int)Math.floor(1.5*iqr);
            low=q1-lim;
            high=q3+lim;  
            int lowerWhisker=q1,upperWhisker=q3;
            
            int k=0;
            while(al.get(k)<low)
                k++;
            
            if(k<al.size())
            lowerWhisker=al.get(k);
            
            k=al.size()-1;
            
            while(al.get(k)>high)
                k--;
            
            if(k>0)
                upperWhisker=al.get(k);
            
            System.out.println("Q1 "+q1+" Median "+median+" Q3 "+q3);
            System.out.println("High "+high+" Low "+low+" UW "+upperWhisker+" LW "+lowerWhisker+"\n");
            
            g.setColor(Color.green);
            //the box
            g.drawRect(x,limy-q3,perx,iqr);
            
            g.setColor(Color.orange);
            g.drawLine(x,limy-median,x+perx,limy-median);
           
            
            
            //the whiskers
            
            //whiskerWidth
            int wd=perx/2;
            
            g.setColor(Color.cyan);
            //upper
            g.drawLine(x+wd/2,limy-upperWhisker,x+3*wd/2,limy-upperWhisker);
            g.drawLine(x+wd,limy-q3,x+wd,limy-upperWhisker);
            
            //lower
            g.drawLine(x+wd/2,limy-lowerWhisker,x+3*wd/2,limy-lowerWhisker);
            g.drawLine(x+wd,limy-q1,x+wd,limy-lowerWhisker);
            
            
            g.setColor(Color.red);
            //draw loweroutliers
            k=0;
            while(al.get(k)<low)
            {
                g.drawOval(x+wd-5, limy-al.get(k)-5, 10, 10);
                k++;
            }
            
             k=al.size()-1;
            
            while(al.get(k)>high)
            {
                g.drawOval(x+wd/2, limy-al.get(k), 5, 5);
                k--;
            }
            
            
            x+=2*perx;
            
        }
        
    }
    
}
