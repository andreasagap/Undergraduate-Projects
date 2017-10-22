/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;




import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.io.UnsupportedEncodingException;
import java.net.URI;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class Project2 extends Configured implements Tool 
{
    
    private static final String OUTPUT_PATH_TEMP = "index";
    private static final String OUTPUT_PATH_CENTERS = "center";
    
    
    public static void main(String args[]) throws Exception 
    {
      int res = ToolRunner.run(new Project2(), args);
      System.exit(res);
    }

    public int run(String[] args) throws Exception {

        if(args.length != 4) {
               System.err.println("Usage : <inDir> <outDir> <Path of stopwords.txt> <times execution KMeans");
               System.exit(2);
        }
      Path inputPath = new Path(args[0]);//input directory
      Path outputPath = new Path(args[1]); //output directory
      Configuration conf = getConf();
      Path temp=new Path(OUTPUT_PATH_TEMP);
      
      FileSystem fs = FileSystem.get(conf);
      FileStatus[] status = fs.listStatus(inputPath);
      conf.setInt("inputfiles", status.length);
   
      Job job = Job.getInstance(conf);
      job.getConfiguration().set("mapreduce.output.basename", "index");
      fs.delete(temp,true);
      fs.delete(outputPath, true);
      job.addCacheFile(new URI("file://"+args[2])); //stopwords.txt
      FileInputFormat.setInputPaths(job, inputPath);
      FileOutputFormat.setOutputPath(job, temp);
      FileInputFormat.setInputDirRecursive(job, true);
      job.setJobName("Project2");
      job.setJarByClass(Project2.class);
      job.setInputFormatClass(TextInputFormat.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(Text.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);
      job.setMapperClass(InvertedIndexMapper.class);
      job.setReducerClass(InvertedIndexReducer.class);

      boolean success = job.waitForCompletion(true);
      if (success) 
      {
          Path centers=new Path(OUTPUT_PATH_CENTERS);
          
          CreateCenters(temp,conf);

          conf.setBoolean("loop",true); 
          for(int i=0;i<Integer.parseInt(args[3]);i++)
          {
                Job job2 = Job.getInstance(conf, "JOB_2");
                Path out=new Path(centers.getName()+"/centers.txt");
                job2.addCacheArchive(new URI(out.toUri().toString()));
                job2.setMapperClass(KMeansMapper.class);
                job2.setReducerClass(KMeansReducer.class);
                job2.setMapOutputKeyClass(Text.class);
                job2.setMapOutputValueClass(Text.class);
                job2.setOutputKeyClass(Text.class);
                job2.setOutputValueClass(Text.class);
                FileInputFormat.addInputPath(job2, new Path(temp.getName()));
                FileOutputFormat.setOutputPath(job2, outputPath);
                
                job2.waitForCompletion(true);
                
                ChangeCenters(out,outputPath,conf);    
          }
          conf.setBoolean("loop",false);
          Job job2 = Job.getInstance(conf, "JOB_2");
          Path out=new Path(centers.getName()+"/centers.txt");
          job2.addCacheArchive(new URI(out.toUri().toString()));
          job2.getConfiguration().set("mapreduce.output.basename", "output");
          job2.setMapperClass(KMeansMapper.class);
          job2.setReducerClass(KMeansReducer.class);
          job2.setMapOutputKeyClass(Text.class);
          job2.setMapOutputValueClass(Text.class);
          job2.setOutputKeyClass(Text.class);
          job2.setOutputValueClass(Text.class);
          FileInputFormat.addInputPath(job2, new Path(temp.getName()));
          FileOutputFormat.setOutputPath(job2, outputPath);
          return  job2.waitForCompletion(true) ? 0:1;     
      }
      return 0;
    }
    
    private void ChangeCenters(Path centers,Path outputPath,Configuration conf) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        FileSystem fs = FileSystem.get(conf);
       
        fs.delete(centers, true);
        FileStatus[] s2 = fs.listStatus(outputPath);
        for(FileStatus s: s2)
        {
            
            if(s.getPath().getName().contains("part"))
            {
                
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(fs.create(centers,true)));
                BufferedReader in=new BufferedReader(new InputStreamReader(fs.open(s.getPath())));
                for(int i=0;i<2;i++)
                {
                    String l=in.readLine();
                   
                    writer.write(l);
                    writer.write("\n");
                }
                writer.close();
                in.close();
                fs.delete(outputPath, true);
                break;
            }
        }
        
    }
    private void CreateCenters(Path temp,Configuration conf) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        FileSystem fs = FileSystem.get(conf);
        FileStatus[] status = fs.listStatus(temp);
        BufferedReader in=new BufferedReader(new InputStreamReader(fs.open(status[1].getPath())));
        Path out=new Path(OUTPUT_PATH_CENTERS+"/centers.txt");
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(fs.create(out,true)));
        String line;
        String array[];
          for(int i=0;i<2;i++)
          {
              line = in.readLine();
              if(line == null)
              {
                  break;
              }
              line=line.replace("[", "#").replace("]", "");
              array=line.split("#");
              array[1]=array[1].replaceAll(",","");
              writer.write(array[1]);
              writer.write("\n");
          }
          writer.close();
          in.close();

    }
    

}
