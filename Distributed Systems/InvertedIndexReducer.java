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




public class InvertedIndexReducer extends Reducer<Object, Text, Text, Text> {
    private int numFiles;
    private final Text finalKey=new Text();
    private final Text finalValue=new Text();
    @Override
    public void reduce(Object key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
    {
     
      Configuration conf = context.getConfiguration();
      numFiles= conf.getInt("inputfiles",0);
      int[] ArrayFiles = new int[numFiles];
      for(int i=0;i<ArrayFiles.length;i++)
      {
            ArrayFiles[i]=0;
      }     
      for (Text value : values)
      {     
          String filename=value.toString();
          ArrayFiles[Integer.parseInt(filename)-1]=1;
      }
      String line=" [";
      for(int i=0;i<ArrayFiles.length-1;i++)
      {
          line=line+ArrayFiles[i]+", ";
      }
      line=line+ArrayFiles[ArrayFiles.length-1]+"]";
      finalKey.set(key.toString());
      finalValue.set(line);
      context.write(finalKey, finalValue);
      
 
    }
    
}