/**
 * 
 */
package WordCountHadoop;

import java.io.BufferedWriter;
import java.io.File;

/**
 * @author Adam
 *
 */
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import WordCountHadoop.BTreeNode;
import WordCountHadoop.Point;

/**
 * This is an example Hadoop Map/Reduce application.
 * It reads the text input files, breaks each line into words
 * and counts them. The output is a locally sorted list of words and the 
 * count of how often they occurred.
 *
 * To run: bin/hadoop jar build/hadoop-examples.jar wordcount
 *            [-m <i>maps</i>] [-r <i>reduces</i>] <i>in-dir</i> <i>out-dir</i> 
 */
public class WordCount extends Configured implements Tool {

  final static int MAXPOINTS=18;
  final static int NUM_KIDS = 6;
  final static int NUM_DATA = 6;
  final static int NUM_LEV = 5;
  final static int DELTA = 4;
  
  private static BTreeNode root = new BTreeNode();
  private static Map<String, BTreeNode> data = new HashMap<String,BTreeNode>();
  
  //function 
  //This function takes iterator in num points and 
  //points(consist of location, traveltime,staytime, and encoding) format and maxnumber of points
  //and returns arraylist<point> of iterator of points
  public static ArrayList<Point> getpoints(Iterator<Text> in, final int MAXPOINTS){
	  ArrayList<Point> pers = new ArrayList<Point>();
	  int points = Integer.parseInt(in.next().toString());
	  for(int i=0; i<points; i++){
		  Point temp = new Point();
		  temp.name = in.next().toString();
		  temp.staytime = Integer.parseInt(in.next().toString());
		  temp.traveltime = Integer.parseInt(in.next().toString());
		  temp.encoding = Integer.parseInt(in.next().toString());
		  pers.add(temp);	
		}
		for(int i=0; i<MAXPOINTS-points;i++){
			Point temp = new Point();
			pers.add(temp);
		}
		return pers;
	}
  
  //takes a list of points and number of points it has, as well as map and level desired
  //Returns an array list with locations from corresponding level, assuming locations in pers
  //map to BTreeNode data
  static ArrayList<String> getnames(final ArrayList<Point> pers,final int points,
			Map<String, BTreeNode> data,final int level)
	{
			ArrayList<String> locs = new ArrayList<String>();
			ArrayList<BTreeNode> temp = new ArrayList<BTreeNode>();
			BTreeNode previous = new BTreeNode();
			for(int i=0; i<points; i++)
			{
				String nme = pers.get(i).name;
				if(data.containsKey(nme))
				{
					locs.add(new String(nme));
					temp.add(new BTreeNode(data.get(nme)));
				}
			}
			for(int k=0; k<temp.size();k++){
				for(int i=0; i<level;i++){
					if (temp.get(k).parent!=null){
						previous = temp.get(k);
						temp.set(k, temp.get(k).parent);
					}
					else{
						System.out.println("It is null\n");
					}
					if(temp.get(k)==null)
						temp.set(k, previous);
				}
			}
			if(level>0){
				for(int i =0; i<locs.size() && i<temp.size(); i++){
					locs.set(i,temp.get(i).data.get(0));
				}
			}
			return locs;
	}
  
    //Pre: person a and b are non empty arrays of point structs
	//Post:max length of common sequence is returned
	static int Comparesimilarity( ArrayList<Point> a, final ArrayList<String> locations1, int points1, ArrayList<Point> b, 
							final ArrayList<String> locations2, int points2, int deltaT)
	{
		boolean quit = false;
		ArrayList<String> seq = new ArrayList<String>(), finals= new ArrayList<String>();
		//vector to record locations of indxs we want to keep
		ArrayList<Integer> locs1 = new ArrayList<Integer>(), locs2= new ArrayList<Integer>(), 
				           final1 = new ArrayList<Integer>(),final2 = new ArrayList<Integer>();
		boolean locationsmatch = true;
		int temptime1=0, temptime2=0, tempindx1=0,tempindx2=0;
		int time1 = 0, time2 = 0, loopcount = 0, p1indx, p2indx, maxmatch = 0, match=0;
		while(!quit)
		{
		    p1indx=loopcount;
		    p2indx=0;
		    match=0;
		    while(locationsmatch)
		    {
		        //if they are at the same location and time difference in range, we know its a match
		    	//also to ensure same length we have "" points skip those
				if((time2-time1)<=deltaT && p1indx<points1 &&
				   p2indx<points2-1&&maxmatch<(match+(points1-p1indx)) &&
				   locations1.get(p1indx) != "" && locations2.get(p2indx) != "")   
				{
		            if(locations2.get(p2indx)==locations1.get(p1indx))
		            {
		                match++;
			   			time1= time1 + a.get(p1indx).staytime + a.get(p1indx).traveltime;
		                time2= time2 + b.get(p2indx).staytime + b.get(p2indx).traveltime;
		                seq.add(locations1.get(p1indx));
		                locs1.add(p1indx);
		                locs2.add(p2indx);
		    			p1indx++;
		    			p2indx++;
		    			temptime1=time1;
		    			temptime2=time2;  
		    			tempindx1=p1indx;
		    			tempindx2=p2indx;
		            }//if we are still in time 
					//range need to increment to next point
		            else
		            {  
		                time2= time2 + b.get(p2indx).staytime + b.get(p2indx).traveltime;
		                p2indx++;
					}//if those two fail we go to the next node for person a and see if new max
				}
		        else if((p1indx==points1 || maxmatch>(match+(points1-p1indx))||
						tempindx1 == points1) || locations1.get(p1indx) != "")
		        {	//time to quit
		            if(maxmatch<match)
		            {
		                maxmatch=match;
		                finals= new ArrayList<String>(seq);
		                final1= new ArrayList<Integer>(locs1);
		                final2= new ArrayList<Integer>(locs2);
		     	    }
		     	    seq.clear();
		     	    locs1.clear();
		     	    locs2.clear();
					locationsmatch=false;
		        }
		        else
		        {
					//if here we need to skip a node and check for max sequence
		        	temptime1=temptime1+a.get(p1indx).staytime + a.get(p1indx).traveltime;
		        	tempindx1++;
		        	p1indx = tempindx1;
					time1  = temptime1;
					time2  = temptime2;
					p2indx = tempindx2;
		        }
		    }
		    time1=0;
		    time2=0;
		    loopcount++;
		    for(int i =0; i<loopcount;i++)
				time1=time1+a.get(i).staytime + a.get(i).traveltime;
		    //if we have got to last indx 
		    locationsmatch=true;
		    //if we reached the end we need to quit
		    if(loopcount==points1)
		    	quit=true;
		}
		//fout<<"Matching sequence:";
//		for(int i=0; i<final.size();i++)
		//	fout<<final[i]<<" ";
		//fout<<endl;
		//now create sequence that only uses points of best sequence
		//to do, find better way to assign elements, stupid java won't pass by reference
		ArrayList<Point> newpers1 = new ArrayList<Point>();
		for(int i=0; i<final1.size();i++)
		{
			newpers1.add(new Point(a.get(final1.get(i))));
		}
		a.clear();
		for(int i=0; i<newpers1.size();i++)
			a.add(newpers1.get(i));
		//for(int i=newpers1.size();i<a.size();i++)
		//	a.remove(a.size()-1);
		ArrayList<Point> newpers2 = new ArrayList<Point>();
		for(int i=0; i<final2.size();i++)
		{
			newpers2.add(new Point(b.get(final2.get(i))));
		}
		b.clear();
		for(int i=0; i<newpers2.size();i++)
			b.add(newpers2.get(i));
		//for(int i=newpers2.size();i<b.size();i++)
			//b.remove(b.size()-1);
		return maxmatch;
	}
	
	/**
   * Counts the words in each line.
   * For each line of input, break the line into words and emit them as
   * (<b>word</b>, <b>1</b>).
   */
  public static class MapClass extends MapReduceBase
    implements Mapper<LongWritable, Text, DoubleWritable, Text> {
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    
    public void map(LongWritable key, Text value, 
                    OutputCollector<DoubleWritable, Text> output, 
                    Reporter reporter) throws IOException {
      String line = value.toString();
      double average = 0;
      int count = 0;
      StringTokenizer itr = new StringTokenizer(line);
      //first input is location id, ignore that
      itr.nextToken();
      while (itr.hasMoreTokens()) {
    	count++;
    	//first is string for location staytime & traveltime, not used for key
        itr.nextToken();
        itr.nextToken();
        itr.nextToken();
        //finally we have the encoding of a location
    	average += Integer.parseInt(itr.nextToken());
      }
      average = average/count;
      output.collect(new DoubleWritable(average), value);
    }
  }
  
  /**
   * A reducer class that just emits the sum of the input values.
   */
  public static class Reduce extends MapReduceBase
    implements Reducer<DoubleWritable, Text, String, String> {
    
	
    public void reduce(DoubleWritable key, Iterator<Text> values,
                       OutputCollector<String, String> output, 
                       Reporter reporter) throws IOException {
      //first we convert all text to trajectories
      //WordCountHadoop.BTreeNode.print(root);
      ArrayList<ArrayList<Point>> people = new ArrayList<ArrayList<Point>>();
      while(values.hasNext()){
    	  people.add(getpoints(values,MAXPOINTS));
      }
      //then we do a double for loop of all trajectories comparing similarity
      //this will output key of one trajectory and key of another, provided they are
      //a good enough match
      final int maxsimilarity = 200;
      for(int i=0; i<people.size(); i++){
    	  for( int k=0; k<people.size();k++){
    		  //get each person and compare levels
    		  if(i!=k){
					ArrayList<Point> pers1, pers2;
					pers1 = new ArrayList<Point>(people.get(i));
					pers2 = new ArrayList<Point>(people.get(k));
					ArrayList<String> locs1 = new ArrayList<String>(), locs2= new ArrayList<String>();
					int level = NUM_LEV-2;
					int points1,points2;
					final int SCALAR = 5;
					final int MINSCORE=200;
					int weight = 1;
					int score =0;
					do{
						points1 = pers1.size();
						points2 = pers2.size();
						locs1 = getnames(pers1,points1,data,level);
						locs2 = getnames(pers2,points2,data,level);
						points1 = locs1.size();
						points2 = locs2.size();
						if(points1>0 && points2>0){
							int temp = 
							score += SCALAR * weight * Comparesimilarity(pers1,locs1,points1,pers2,locs2,points2,DELTA);
						}
						locs1.clear();
						locs2.clear();
						level--;
						weight++;
					}while(level>=0 && points1>0 && points2>0);
					if(score>MINSCORE)
						output.collect(pers1.toString(), pers2.toString());
					pers1.clear();
					pers2.clear();
				}
    	  }
      }
      
    }
  }
  
  static int printUsage() {
    System.out.println("wordcount [-m <maps>] [-r <reduces>] <input> <output>");
    ToolRunner.printGenericCommandUsage(System.out);
    return -1;
  }
  
  /**
   * The main driver for word count map/reduce program.
   * Invoke this method to submit the map/reduce job.
   * @throws IOException When there is communication problems with the 
   *                     job tracker.
   */
  public int run(String[] args) throws Exception {
	  
	// build our BTreeNode
	final int MAXPOINTS=18;
	data = new HashMap<String,BTreeNode>();
	Scanner in = new Scanner(new File("data1.txt"));
	BTreeNode root = new BTreeNode();
	BTreeNode.fillnode(in,root);
	BTreeNode.buildtree(in, root, data);
	BTreeNode.print(root);
	in.close();
		
    JobConf conf = new JobConf(getConf(), WordCount.class);
    conf.setJobName("wordcount");
 
    // the keys are words (strings)
    conf.setOutputKeyClass(Text.class);
    // the values are counts (ints)
    conf.setOutputValueClass(IntWritable.class);
    
    conf.setMapperClass(MapClass.class);        
    conf.setCombinerClass(Reduce.class);
    conf.setReducerClass(Reduce.class);
    
    List<String> other_args = new ArrayList<String>();
    for(int i=0; i < args.length; ++i) {
      try {
        if ("-m".equals(args[i])) {
          conf.setNumMapTasks(Integer.parseInt(args[++i]));
        } else if ("-r".equals(args[i])) {
          conf.setNumReduceTasks(Integer.parseInt(args[++i]));
        } else {
          other_args.add(args[i]);
        }
      } catch (NumberFormatException except) {
        System.out.println("ERROR: Integer expected instead of " + args[i]);
        return printUsage();
      } catch (ArrayIndexOutOfBoundsException except) {
        System.out.println("ERROR: Required parameter missing from " +
                           args[i-1]);
        return printUsage();
      }
    }
    // Make sure there are exactly 2 parameters left.
    if (other_args.size() != 2) {
      System.out.println("ERROR: Wrong number of parameters: " +
                         other_args.size() + " instead of 2.");
      return printUsage();
    }
    FileInputFormat.setInputPaths(conf, other_args.get(0));
    FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));
        
    JobClient.runJob(conf);
    return 0;
  }
  
  
  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new WordCount(), args);
    System.exit(res);
  }

}

