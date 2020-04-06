/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import addetions.PairCompair;
import addetions.pair;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author DELL
 */
public class Generator {
        
    static int[] zomptyps=new int[]{1,1,1,1,1,1,2,2,3,4}; 
    static int[] typsMxIndex=new int[]{6,8,9,10}; 
    
    static int mnzombie=60,Mxzombie=500;
    static Random rand=new Random();
    static int counter =0, MX=3;
    public static  PriorityQueue<pair>  genrate (int level)
    {
        counter=0;
        float dourt=300;
        int numOfWaves=1;
        if(level%5==4)
            numOfWaves=3;
        else if(level%5>2)
            numOfWaves =2;
        int totalZombies=Math.max(Math.min(level*7,Mxzombie),mnzombie);
        int MXtyp=level;
        
        PriorityQueue<pair>q=new PriorityQueue<>(new PairCompair());
     
        if(numOfWaves==1)
        {
            Normal(10, 270, typsMxIndex[0], q);
        }
        else if(numOfWaves ==2)
        {
               Normal(10, 140, typsMxIndex[0], q);
               Wave(140, 170, typsMxIndex[Math.min(MX, MXtyp)], q, totalZombies/(numOfWaves+2));     
               Normal(185, 270, typsMxIndex[Math.min(1, MXtyp)], q);
               
        }
        else
        {
            
               Normal(10, 80, typsMxIndex[0], q);
               Wave(80, 110, typsMxIndex[Math.min(MX, MXtyp)], q, totalZombies/(numOfWaves+2));     
               Normal(125, 170, typsMxIndex[Math.min(1, MXtyp)], q);
               Wave(170, 200, typsMxIndex[Math.min(MX, MXtyp)], q, totalZombies/(numOfWaves+2));     
               Normal(215, 270, typsMxIndex[Math.min(1, MXtyp)], q);
            
            
            
        }       
        Wave(270, 300, typsMxIndex[Math.min(MX, MXtyp)], q, totalZombies/(numOfWaves+2));
        complet(q,totalZombies);
return q;
    }
   
    private static void Normal(float from, float to, int mxindex,PriorityQueue<pair>q)
    {
        
       float now=from;
     while(now<=to)
     {
         q.add(randomezompie(now,now+1,mxindex));
         counter++;
         now+=5;
     }
        
    }
    private static void Wave(float from, float to, int mxindex,PriorityQueue<pair>q,int  total)
    {
        counter+=total;
        for(int i=0;i<total;i++)
        {
           q.add(randomezompie(from , to ,mxindex));
        }
    
        
    }
    private static void complet(PriorityQueue<pair>q,int totalZombies)
    {
        while(counter<totalZombies)
        {
           q.add(randomezompie(10 , 300 ,typsMxIndex[0]));
            counter++;
        }
    }
    
    private static pair randomezompie(float from, float to, int mxindex)
    {
        
        pair z=new pair();
        z.first=rand.nextFloat()+from+Math.abs(rand.nextInt()%(to-from));
        z.second=zomptyps[Math.abs(rand.nextInt())%mxindex];
        return z;

    }
    
    
}
