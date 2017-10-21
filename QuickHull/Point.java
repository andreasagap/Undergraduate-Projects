/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickhull;

/**
 *
 * @author Andreas Agapitos
 * AEM : 2530
 * andragap@csd.auth.gr
 */
public class Point {
    private float x,y;
    public Point()
    {
        x=0;
        y=0;
    }
    public Point(float x1,float y1)
    {
        x=x1;
        y=y1;
    }
    /*
    * return x
    */
    public float getX()
    {
        return x;
    }
    /*
    * return y
    */
    public float getY()
    {
        return y;
    }
    /*
    * set x
    * @param a
    */
    public void setX(float a)
    {
        x=a;
    }
    /*
    * set y
    * @param b
    */
    public void setY(float b)
    {
        y=b;
    }
    /*
    * check equality
    * @param o
    */
    public boolean equal(Point o)
    {
        return this.x==o.x && this.y==o.y;
       
    }
    /*
    * calculate distance
    * @param o
    */
    public double distance(Point o)
    {
        return Math.sqrt(Math.pow(o.x-this.x,2) + Math.pow(o.y-this.y,2));
    }
}
