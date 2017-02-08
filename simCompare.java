import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.Scanner;
import java.lang.Math;

public class simCompare {

  final static double MAX_DIF = 0.2;

  public static void main(String[] args) {

    try {
    long starttime = System.nanoTime();
    File file = new File("gendPpl.txt");
    Scanner input =  new Scanner(file);
    int lvl=2;

    intsemtreeNode root = new intsemtreeNode(10);

    intsemtreeNode c1d1 = new intsemtreeNode(11); //food
    intsemtreeNode c2d1 = new intsemtreeNode(12); //activity

    intsemtreeNode c1d2 = new intsemtreeNode(1); //McDon
    intsemtreeNode c2d2 = new intsemtreeNode(2); //TacoBell
    intsemtreeNode c3d2 = new intsemtreeNode(3); //DQ
    intsemtreeNode c4d2 = new intsemtreeNode(4); //park
    intsemtreeNode c5d2 = new intsemtreeNode(5); //trail


    root.addChild(c1d1);
    root.addChild(c2d1);

    c1d1.addChild(c1d2);
    c1d1.addChild(c2d2);
    c1d1.addChild(c3d2);

    c2d1.addChild(c4d2);
    c2d1.addChild(c5d2);

    weightFunc(lvl);


    //possible string tree.
    //for now implemented with int tree representing strings
    //Creating static constant meaning tree
    /*
    semtreeNode root = new semtreeNode("root");

    semtreeNode c1d1 = new semtreeNode("home");
    semtreeNode c2d1 = new semtreeNode("Food");
    semtreeNode c3d1 = new semtreeNode("Outdoor activity");
    semtreeNode c4d1 = new semtreeNode("School");
    semtreeNode c5d1 = new semtreeNode("store");

    semtreeNode c1d2 = new semtreeNode("McDon");
    semtreeNode c2d2 = new semtreeNode("DQ");
    semtreeNode c3d2 = new semtreeNode("TacoBell");
    semtreeNode c4d2 = new semtreeNode("Park");
    semtreeNode c5d2 = new semtreeNode("Trail");
    semtreeNode c6d2 = new semtreeNode("RollaHigh");
    semtreeNode c7d2 = new semtreeNode("MST");
    semtreeNode c8d2 = new semtreeNode("walmart");
    semtreeNode c9d2 = new semtreeNode("Kmart");
    semtreeNode c10d2 = new semtreeNode("foodstores");

    semtreeNode c1d3 = new semtreeNode("aldi");
    semtreeNode c2d3 = new semtreeNode("kroger");

    root.addChild(c1d1);
    root.addChild(c2d1);
    root.addChild(c3d1);
    root.addChild(c4d1);
    root.addChild(c5d1);

    c2d1.addChild(c1d2);
    c2d1.addChild(c2d2);
    c2d1.addChild(c3d2);
    c3d1.addChild(c4d2);
    c3d1.addChild(c5d2);
    c4d1.addChild(c6d2);
    c4d1.addChild(c7d2);
    c5d1.addChild(c8d2);
    c5d1.addChild(c9d2);
    c5d1.addChild(c10d2);

    c10d2.addChild(c1d3);
    c10d2.addChild(c2d3);
    */

    //READING IN DATA

    while(input.hasNextInt()) {
      int linelen = input.nextInt();
      int[] p1 = new int[linelen];
      for(int i = 0; i < linelen; i++) {
        p1[i] = input.nextInt();
      }
      int linelen2 = input.nextInt();
      double[] t1 = new double[linelen2];
      for(int i = 0; i < linelen2; i++) {
        t1[i] = input.nextDouble();
      }
      int linelen3 = input.nextInt();
      int[] p2 = new int[linelen3];
      for(int i = 0; i < linelen3; i++) {
        p2[i] = input.nextInt();
      }
      int linelen4 = input.nextInt();
      double[] t2 = new double[linelen4];
      for(int i = 0; i < linelen4; i++) {
        t2[i] = input.nextDouble();
      }

    //outputting 2 different test sequences
      System.out.println("person1 locations&staytimes");
    for(int i = 0; i < p1.length; i++) {
      System.out.print(p1[i] + " ");
    }
    System.out.println();

    for(int i = 0; i < t1.length; i++) {
      System.out.print(t1[i] + " ");
    }
    System.out.println();

    System.out.println("person2 location&staytimes");

    for(int i = 0; i < p2.length; i++) {
      System.out.print(p2[i] + " ");
    }
    System.out.println();

    for(int i = 0; i < t2.length; i++) {
      System.out.print(t2[i] + " ");
    }
    System.out.println();

    int[] shiftp1 = new int[p1.length];
    int[] shiftp2 = new int[p2.length];





    //list of matches (nodes for graphs)
    //for each layer of semantic meaning

    long stime2 = System.nanoTime();

    for (int m=0; m < lvl; m++) {
        System.out.println("LEVEL " + (lvl-m));
        if(m==1) {
          p1 = levelShift(p1);
          p2 = levelShift(p2);

          System.out.println("shiftedPerson1:");

          for (int i=0; i < shiftp1.length; i++) {
            System.out.println(p1[i]);
          }

          System.out.println("shiftedPerson2:");

          for (int i=0; i < shiftp2.length; i++) {
            System.out.println(p2[i]);
          }

        }



        ArrayList<int[]> matches = new ArrayList<int[]>();

        //checks for matches between them
        //adds matches to list i=index of match in p1
        //                     j=index of match in p2
        //third zero in current sets the node as white

        for (int i=0; i < p1.length; i++) {
          for (int j=0; j < p2.length; j++) {
            if(p1[i] == p2[j]) {
              int[] current = {0,0,0};
              current[0] = i;
              current[1] = j;
              matches.add(current);
            }
          }
        }

        //put in decreasing lexigraphical order
        Collections.reverse(matches);


        //printing out list of matches for testing
        System.out.println("number of matches: " + matches.size());
        System.out.println("Indexes of matches");
        for (int i=0; i < matches.size(); i++) {
          System.out.print(matches.get(i)[0] + " ");
          System.out.println(matches.get(i)[1]);
        }

        //create adjacency matrix to store graph
        if(matches.size() == 0) {
          int[] fakedata = {0,0,0};
          matches.add(fakedata);
        }
        int[][] commonLoc = new int[matches.size()][matches.size()];


        //builds graph and blacks out nodes
        //

        for (int l = 1; l < matches.size(); l++) {
          for (int t=l-1; t > -1; t--) {
            if (matches.get(l)[2] == 0) {
              if (precTest(matches, l, t, t1, t2)) {
                if(commonLoc[l][t] != -1) {
                    commonLoc[l][t] = 1;
                }
                for (int i=0; i < matches.size(); i++) {
                  if(commonLoc[t][i] == 1) {
                    matches.get(i)[2] = -1;
                  }
                }
              }
            }
          }
        }


        //prints out the adjacency matrix

        System.out.println("//////////////////");
        System.out.print("   ");
        for (int i=0; i < matches.size(); i++)
          System.out.print((i+1) + " ");
        System.out.println();
        for (int i=0; i < matches.size(); i++) {
          System.out.print((i+1) + " " + (i < 9 ? " " : ""));
          for (int j=0; j < matches.size() ;j++ ) {
            System.out.print(commonLoc[i][j] + " ");
          }
          System.out.println();
        }

        //finding max match
        maximumMatch(commonLoc, commonLoc[0].length-1, commonLoc[0].length-1);
        System.out.println();

        }


        if(!input.hasNextInt()) {
          long endtime2 = System.nanoTime();
          System.out.println("excluding IO: " + ((endtime2 - stime2)/1000000) );
        }
      }

    input.close();
    long endtime = System.nanoTime();

    System.out.println("entire program: " + ((endtime - starttime)/1000000));

  } catch (Exception ex) {
    ex.printStackTrace();
  }

  }

  public static void maximumMatch(int[][] commonLoc, int srow, int scol) {
    if(outDeg(commonLoc, srow)) {
      //System.out.print("start");
      System.out.print(srow+1);
      System.out.println("end");
      return;
    }
    for(int i=scol; i > -1; i--) {
      if(commonLoc[srow][i] == 1) {
        System.out.print((srow+1) + "(");
        maximumMatch(commonLoc, i, i /*can b i-1*/);
        //System.out.print(srow+1);
      }
    }

    return;
  }

  public static int weightFunc(int lvl) {
    return (int) Math.pow(2,(lvl-1));
  }

  //currently hard coded. Need to implement a search by value in tree

  public static int[] levelShift(int[] ogPers) {
    int[] shiftPers = new int[ogPers.length];

    for (int i=0; i < ogPers.length; i++) {
      if(ogPers[i] == 1 || ogPers[i] == 2 || ogPers[i] == 3) {
        shiftPers[i] = 11;
      }

      if(ogPers[i] == 4 || ogPers[i] == 5) {
        shiftPers[i] = 12;
      }
    }

    return shiftPers;
  }

  public static boolean outDeg(int[][] commonLoc, int srow) {
    int total = 0;
    for(int i=0; i<commonLoc[0].length; i++) {
      total += commonLoc[srow][i];
    }

    if(total==0)
      return true;

    return false;
  }

  public static boolean precTest(ArrayList<int[]> matches, int l, int t, double[] t1, double[] t2) {
    double total1 = 0;
    double total2 = 0;
    double travDif = 0;

    if(matches.get(l)[0] <= matches.get(t)[0] && matches.get(l)[1] < matches.get(t)[1]) {
      for(int i =(matches.get(l)[0] * 2) + 1; i < (matches.get(t)[0] * 2); i++) {
        total1 += t1[i];
      }
      for(int i =(matches.get(l)[1] * 2) + 1; i < (matches.get(t)[1] * 2); i++) {
        total2 += t2[i];
      }

      travDif = Math.abs(total1-total2)/(Math.max(total1, total2));
      //System.out.println("dif" + travDif);
      if( travDif <= MAX_DIF) {
        return true;
      }
    }
    return false;
  }
}
