/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickhull;

import java.util.ArrayList;
/**
 *
 * @author Andreas Agapitos
 * AEM : 2530
 * andragap@csd.auth.gr
 */
public class ArrayListPoints {
    private ArrayList<Point> Points;
    public ArrayListPoints()
    {
        Points=new ArrayList<>();
       
    }
    /*
    * add a point
    * @param p
    */
    public void add(Point p)
    {
        Points.add(p);
    }
    /*
    * delete all the points of the arraylist
    */
    public void delete()
    {
        Points.clear();
    }
    /*
    * return the size of ArrayList
    */
    public int length()
    {
        return Points.size();
    }
    /*
    * return a point
    * @param i
    */
    public Point getposition(int i)
    {
        return Points.get(i);
    }
    /*
    * Find the points of the perimeter
    */
    public void quickhull()
    {
        QuickHull qh = new QuickHull();
        ArrayList<Point> p = qh.quickHull(Points);
        Points.clear();
        Points.addAll(p);
        p.clear();
    }
}
