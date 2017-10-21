/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickhull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Andreas Agapitos
 * AEM : 2530
 * andragap@csd.auth.gr
 */
public class Paths {
    private ArrayList<Point> path;
    private float distance;
    public Paths()
    {
        path=new ArrayList<>();
        distance=0;
       
    }
    /*
    * Add a point
    * @param p
    */
    public void add(Point p)
    {
        path.add(p);
    }
    /*
    * return the size of the path
    */
    public int  length()
    {
        return path.size();
    }
    /*
    * return a point
    * @param i
    */
    public Point getposition(int i)
    {
        return path.get(i);
    }
    /*
    *  sorted in ascending order
    */
    public void sort()
    {
         Collections.sort(path, new Comparator<Point>() {
                @Override
                public int compare(Point o1, Point o2) {
                    return o1.getX() > o2.getX() ? 1 : (o2.getX() > o1.getX() ) ? -1 : 0;
                }
            });
    }
    /*
    * calculate the distance
    */
    public void distance()
    {
        for (int i = 1; i < path.size(); i++)
        {
            distance+=path.get(i-1).distance(path.get(i));
        }
    }
    /*
    * return the distance
    */
    public float getDistance()
    {
        return distance;
    }
    /*
    * Print the path
    */
    public void pathPrint()
    {
        System.out.print("The shortest path is: ");
        int i=0;
        for(i=0;i<path.size()-1;i++)
        {
            System.out.print("("+ path.get(i).getX()+","+path.get(i).getY()+")-->");
        }
        System.out.print("("+ path.get(i).getX()+","+path.get(i).getY()+")");
    }
}
