/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.util.ArrayList;


public class Point {
    private ArrayList<Double> c;
    private int numCoordinate;
    public Point(int num)
    {
        c=new ArrayList();
        numCoordinate=num;
    }
    public void setCoordinate(double co)
    {
        c.add(co);
    }
    public int getNum()
    {
        return numCoordinate;
    }
    public double getCoordinate(int i)
    {
        return c.get(i);
    }
}
