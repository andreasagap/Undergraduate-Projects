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
 * URL : http://www.ahristov.com/tutorial/geometry-games/convex-hull.html 
 */
import java.util.ArrayList;

 
public class QuickHull
{
    public ArrayList<Point> quickHull(ArrayList<Point> points)
    {
        ArrayList<Point> convexHull = new ArrayList<>();

        if (points.size() < 3)
            return (ArrayList) points.clone();
        
        int minPoint = -1, maxPoint = -1;

        float minX = Integer.MAX_VALUE;

        float maxX = Integer.MIN_VALUE;

        // We take the maxX and the minX. Here the minX is the "start" and the maxX is the "goal"
        for (int i = 0; i < points.size(); i++)
        {
            if (points.get(i).getX() < minX)
            {
                minX = points.get(i).getX();
                minPoint = i;
            }
            if (points.get(i).getX() > maxX)
            {
                maxX = points.get(i).getX();
                maxPoint = i;
            }
        }
        Point A = points.get(minPoint);
        Point B = points.get(maxPoint);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);
        ArrayList<Point> leftSet = new ArrayList<>();
        ArrayList<Point> rightSet = new ArrayList<>();
        // divide the space into two levels: The right and the left and we separate the points proportionally.
        for (int i = 0; i < points.size(); i++)
        {
            Point p = points.get(i);

            if (pointLocation(A, B, p) == -1)

                leftSet.add(p);

            else if (pointLocation(A, B, p) == 1)

                rightSet.add(p);

        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);
        return convexHull;
    }
    /*
    * calculate the distance
    * @param A,B,C
    */
    public float  distance(Point A, Point B, Point C)
    {
        float ABx = B.getX() - A.getX();

        float ABy = B.getY() - A.getY();

        float num = ABx * (A.getY() - C.getY()) - ABy * (A.getX() - C.getX());

        if (num < 0)

            num = -num;

        return num;
    }
    
    public void hullSet(Point A, Point B, ArrayList<Point> set, ArrayList<Point> hull)
    {
        int insertPosition = hull.indexOf(B);

        if (set.isEmpty())

            return;

        if (set.size() == 1)

        {

            Point p = set.get(0);

            set.remove(p);

            hull.add(insertPosition, p);

            return;

        }

        float dist = Integer.MIN_VALUE;

        int furthestPoint = -1;

        for (int i = 0; i < set.size(); i++)
        {
            Point p = set.get(i);

            float distance = distance(A, B, p);

            if (distance > dist)

            {

                dist = distance;

                furthestPoint = i;

            }

        }

        Point P = set.get(furthestPoint);

        set.remove(furthestPoint);

        hull.add(insertPosition, P);

        // Determine who's to the left of AP

        ArrayList<Point> leftSetAP = new ArrayList<>();

        for (int i = 0; i < set.size(); i++)
        {
            Point M = set.get(i);
            if (pointLocation(A, P, M) == 1)
            {

                leftSetAP.add(M);
            }
        }
        // Determine who's to the left of PB

        ArrayList<Point> leftSetPB = new ArrayList<>();

        for (int i = 0; i < set.size(); i++)
        {
            Point M = set.get(i);

            if (pointLocation(P, B, M) == 1)
            {
                leftSetPB.add(M);

            }

        }

        hullSet(A, P, leftSetAP, hull);

        hullSet(P, B, leftSetPB, hull);
    }
    /*
    * check the location of a point between two others
    * @param A,B,P
    */
    public int pointLocation(Point A, Point B, Point P)
    {
        float cp1 = (B.getX() - A.getX()) * (P.getY() - A.getY()) - (B.getY() - A.getY()) * (P.getX() - A.getX());
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }
}
