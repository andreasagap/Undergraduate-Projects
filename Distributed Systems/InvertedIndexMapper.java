/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class InvertedIndexMapper  extends Mapper<Object, Text, Text,Text> {
    private final Text finalWord = new Text();
    private final Text filename = new Text();
   
   
    @Override
    public void map(Object key, Text value,
                    Mapper.Context context) throws IOException, InterruptedException {
        
        String[] wordArray = value.toString().replaceAll("[^a-zA-Z ]", "").split(" ");
        
        Path path = new Path ("stopwords.txt");
        FileSystem fs = FileSystem.getLocal(context.getConfiguration());

        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
        String sCurrentLine="";

        while ((sCurrentLine = br.readLine()) != null)
        {
		for(int i=0;i<wordArray.length;i++)
                {
                    if(wordArray[i].equalsIgnoreCase(sCurrentLine))
                    {
                        wordArray[i]="-1"; //if word exists in stopwords.txt
                    }
                }
	}
        
        
        FileSplit filesplit=(FileSplit)context.getInputSplit();
        filename.set(filesplit.getPath().getName().replace(".txt",""));
        //Stemmer
        Stemmer stemmer = new Stemmer();
        for (String wordArray1 : wordArray) 
        {
            if (!wordArray1.equalsIgnoreCase("-1"))
            {
                char word[]= wordArray1.toCharArray();
                stemmer.add(word,word.length);
                stemmer.stem();
                finalWord.set(stemmer.toString());
                context.write(finalWord, filename);
            }            
        }
    }
}
