import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.Scanner;
import java.lang.Math;

public class simCompareNP {

  final static double MAX_DIF = 0.2;

  public static void main(String[] args) {

    try {
    File file = new File("people.txt");
    Scanner input = new Scanner(file);


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

    //2 different test sequences

    //for(int i = 0; i < p1.length; i++) {
      //System.out.print(p1[i] + " ");
    //}
    //System.out.println();

    //for(int i = 0; i < t1.length; i++) {
      //System.out.print(t1[i] + " ");
    //}
    //System.out.println();

    //for(int i = 0; i < p2.length; i++) {
      //System.out.print(p2[i] + " ");
    //}
    //System.out.println();

    //for(int i = 0; i < t2.length; i++) {
      //System.out.print(t2[i] + " ");
    //}
    //System.out.println();


    //list of matches (nodes for graphs)
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
          //System.out.print(current[0] + " ");
          //System.out.println(current[1]);
          matches.add(current);
        }
      }
    }

    //put in decreasing lexigraphical order
    Collections.reverse(matches);


    //printing out list of matches for testing
    //System.out.println(matches.size());
    //for (int i=0; i < matches.size(); i++) {
      //System.out.print(matches.get(i)[0] + " ");
      //System.out.println(matches.get(i)[1]);
    //}

    //create adjacency matrix to store graph
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

    //System.out.println("//////////////////");
    //System.out.print("   ");
    //for (int i=0; i < matches.size(); i++)
      //System.out.print((i+1) + " ");
    //System.out.println();
    //for (int i=0; i < matches.size(); i++) {
      //System.out.print((i+1) + " " + (i < 9 ? " " : ""));
      //for (int j=0; j < matches.size() ;j++ ) {
        //System.out.print(commonLoc[i][j] + " ");
      //}
      //System.out.println();
    //}

    //finding max match
    maximumMatch(commonLoc, commonLoc[0].length-1, commonLoc[0].length-1);
    //System.out.println();
    }
    input.close();
  } catch (Exception ex) {
    ex.printStackTrace();
  }

  }

  public static void maximumMatch(int[][] commonLoc, int srow, int scol) {
    if(outDeg(commonLoc, srow)) {
      ////System.out.print("start");
      System.out.print(srow+1);
      System.out.println("end");
      return;
    }
    for(int i=scol; i > -1; i--) {
      if(commonLoc[srow][i] == 1) {
        System.out.print((srow+1) + "(");
        maximumMatch(commonLoc, i, i /*can b i-1*/);
        ////System.out.print(srow+1);
      }
    }

    return;
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
      ////System.out.println("dif" + travDif);
      if( travDif <= MAX_DIF) {
        return true;
      }
    }
    return false;
  }
}
