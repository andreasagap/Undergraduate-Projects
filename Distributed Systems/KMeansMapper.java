/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;


public class KMeansMapper extends Mapper<Object, Text, Text,Text> {
    private static Point[] centers;
    private final Text word = new Text();
    private final Text finalkey=new Text();
    private int numFiles;
            
    @Override
    public void map(Object key, Text value,Mapper.Context context) throws IOException, InterruptedException 
    {
        Configuration conf = context.getConfiguration();
        numFiles= conf.getInt("inputfiles",0);
        centers = new Point[2];
        centers[0]= new Point(numFiles);
        centers[1]= new Point(numFiles);
	String line = value.toString();
        
        Path path = new Path ("centers.txt");
        FileSystem fs = FileSystem.getLocal(context.getConfiguration());

        Scanner scanner = new Scanner(new InputStreamReader(fs.open(path)));
        int i=0,k=0;
        while (scanner.hasNextDouble())
        {
            if(i<numFiles)
            {
                centers[k].setCoordinate(scanner.nextDouble());
                if(i+1==numFiles)
                {
                    k++;
                }
            }
            else
            {
                centers[k].setCoordinate(scanner.nextDouble());
            }
            i++;
	}
        
        
        String array[];
        line=line.replace("[", "#").replace("]", "");
        array=line.split("#");
        array[0]=array[0].replace("\t", "");

        array[1]=array[1].replaceAll(",","");
        word.set(array[0]+array[1]);
        String point[]=array[1].split(" ");
        
        double sum1=0; // x1*y1+x2*y2...
        double sum2=0; //x1^2+x2^2 ...
        double sum3=0; //y1^2+y2^2 ...
        double dist=-1;
        double d;
        int position=-1;
        for(k=0;k<centers.length;k++)// Find the minimum center from a point
        {
            for(i=0;i<centers[k].getNum();i++)
            {
                sum1=sum1+(centers[k].getCoordinate(i) * Double.parseDouble(point[i]));
                sum2=sum2 + Math.pow(Double.parseDouble(point[i]),2);
                sum3=sum3+Math.pow(centers[k].getCoordinate(i),2);
            }
            sum2=Math.pow(sum2,0.5);
            sum3=Math.pow(sum3,0.5);
            d=Math.abs(1-(sum1/(sum2*sum3)));
            if(dist<d)
            {
                dist=Math.abs(1-(sum1/(sum2*sum3)));
                position=k+1;
            }
            
        }
        finalkey.set(String.valueOf(position));
	// Emit the nearest center and the point
	context.write(finalkey,word);
}
}