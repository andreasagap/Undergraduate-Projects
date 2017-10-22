/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;


import java.io.IOException;

import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class KMeansReducer extends Reducer<Object, Text, Text, Text> 
{
    private final Text word = new Text();
    private final Text newCenter=new Text();
    private int numFiles;
    static final Logger log = Logger.getLogger(KMeansReducer.class.getName());
    @Override
    public void reduce(Object key, Iterable<Text> values,Context context) throws IOException , InterruptedException 
    {
        
        Configuration conf = context.getConfiguration();
        numFiles= conf.getInt("inputfiles",0);
        word.set("");
        if(conf.getBoolean("loop", true))
        {
            
            double sum[] = new double[numFiles];
            int no_elements = 0;
            String point = "";

            for (Text value : values)
            {     

                String array[]=value.toString().split(" ");

                for(int i=1;i<numFiles;i++)
                {

                    sum[i-1]=sum[i-1]+Double.valueOf(array[i]);
                }
                no_elements++;

            }

            // We have new center now
            for(int i=0;i<numFiles;i++)
            {
                point=point+(sum[i] / no_elements)+" ";
            }
            newCenter.set(point);

            // Emit new center and point

            context.write(newCenter,word);
            
        }
        else 
        {
            String w="";
            for (Text value : values)
            {     

                String array[]=value.toString().split(" ");
                w=w+" "+array[0];
            }
            word.set(w);
            context.write(new Text(key.toString()),word);
        }
        
    }
}