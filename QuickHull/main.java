/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickhull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Andreas Agapitos
 * AEM : 2530
 * andragap@csd.auth.gr
 */
public class main{

    /**
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        ArrayListPoints table=new ArrayListPoints();
        int diamonds=0;
        Point start=null,goal=null;
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            int i = 1;
            while(scanner.hasNextInt())
            {
                if(i==1)
                {
                    start=new Point(scanner.nextInt(),scanner.nextInt()); 
                }
                else if(i==2)
                {
                   goal=new Point(scanner.nextInt(),scanner.nextInt());
                }
                else if(i==3)
                {
                    diamonds=scanner.nextInt();
                }
                else
                {
                    table.add(new Point(scanner.nextInt(),scanner.nextInt()));
                }
                i++;
            }
        }
        System.out.println();

        table.add(start);
        table.add(goal);
        table.quickhull(); //Find the perimeter 
        minPath(table,start,goal); // Find the minimum path
        System.out.println();
        System.out.println("Number of weightings: "+FindWeights(diamonds));
        
    }
    /*
    * Find the minimum path 
    * @param table,start,goal
    */
    public static void minPath(ArrayListPoints table,Point start,Point goal)
    {
        Paths DownPath = new Paths();
        int i=0;
        for(i=0;i<table.length();i++) //table has first the down path. When we find the start,then begin the Up path
        {
            if(table.getposition(i).equal(start))
            {
                break;
            }
            else
            {
                DownPath.add(table.getposition(i));
            }
        }
        DownPath.add(start);
        DownPath.add(goal);
        Paths UpPath = new Paths();
        for(int j=i;j<table.length();j++)
        {
                UpPath.add(table.getposition(j));
        }
        DownPath.sort();
        UpPath.sort();
        DownPath.distance();
        UpPath.distance();
        if(DownPath.getDistance()<UpPath.getDistance())
        {
            System.out.println("The shortest distance is " + DownPath.getDistance());
            DownPath.pathPrint();
        }
        else
        {
            System.out.println("The shortest distance is " + UpPath.getDistance());
            UpPath.pathPrint();
        }
    }
    /*
    * Find the number of weightings . This function is recursive. I separate the diamonds in three groups.
    * @param diamonds
    */
    public static int FindWeights(int diamonds)
    {
        int group1,group2,group3;
        if(diamonds<=1)
        {
            return 0;
        }
        if(diamonds==2)
        {
            return 1;
        }
        group1=diamonds/3;
        group2=diamonds/3;
        if(diamonds%3!=0) //if it is true,there are decimals.
        {
            group3=diamonds/3 + diamonds%3; 
        }
        else
        {
            group3=diamonds/3;
        }
        int z=zygos();
        if(z==1)
        {
            diamonds=group1;
        }
        else if(z==0)
        {
            diamonds=group3;
        }
        else
        {
            diamonds=group2;
        }
        return FindWeights(diamonds)+1;
    }
    /*
    * Simulation of scale
    */
    public static int zygos(){
        Random randomGenerator = new Random();
        int x = randomGenerator.nextInt(100);
        if(x<34)
            return 1;//left
        else if (x < 67)
            return 0;//same weight
        else
            return -1;//right
    }
}
