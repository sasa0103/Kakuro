
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet; // Sarah ich habe LinkedHashset genommen weil Hashset wird sortiert. Z.B. wird (3,0) zu (0,3)
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sarah
 */
public class solver {

    static Map<Integer, Integer> eingabe;
    static LinkedList<Integer>[][] einMatrix;
    //static HashSet<Integer>[][] horizont;
    //static HashSet<Integer>[][] vertikal;
    static int groesse;
    
    static Map<Integer, Set<Set<Integer>>> mengenFuerSumme;
    
    static HashSet<Set<Integer>>[][] horizontal;
    static HashSet<Set<Integer>>[][] vertical;
            

/**
 * Diese Methode liest das Kakuro als String ein und speichert es als HashSet Matrix (einMatrix)
 */      
   public static void einlesen(String s)
   {
      StringTokenizer st = new StringTokenizer(s, ",");
      String temp;
      boolean ende = false;
      int i = 0;
      groesse = new Integer(st.nextToken().replaceFirst("\\(", ""));
      
      
      // Matrix wird mit der richtigen Größe initialisiert
      //einMatrix = (HashSet<Integer>[][])Array.newInstance(LinkedHashSet.class, groesse, groesse);
      einMatrix = (LinkedList<Integer>[][])Array.newInstance(LinkedList.class,groesse,groesse); 
      for(int j = 0; j < groesse; j++) {
          for(int k = 0; k < groesse; k++) {
              einMatrix[j][k] = new LinkedList<Integer>();
          }
      }
      
      /**
       * Das erste Feld des Kakuro wird gesondert behandelt, da dort eine Klammer auf geht. 
       * Diese Klammer wird entfernt und der Wert als Integer in der Matrix gespeichert.      
       */
      String readToken = st.nextToken().replaceFirst("\\(", "");
      Integer readValue = new Integer(readToken);
      einMatrix[i/groesse][i%groesse].add(readValue);
      i++;
      
      // Der String wird durchgegangen, die Klammern enfernt und in die Matrix geschrieben.
      while (st.hasMoreTokens() && ende == false) {
          temp = st.nextToken();
          if(temp.contains("(")){
             einMatrix[i/groesse][i%groesse].add(new Integer(temp.replaceFirst("\\(", "")));  
             einMatrix[i/groesse][i%groesse].add(new Integer(st.nextToken().replaceFirst("\\)", "")));
             i++;
          }
          else{
            if(temp.contains(")")){
              einMatrix[i/groesse][i%groesse].add(new Integer(temp.replaceAll("\\)", "")));
              ende = true;
            }
            else{
                einMatrix[i/groesse][i%groesse].add(new Integer(temp));
                i++;
            }
          }         
      }
   }

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { //  throws IOException {
        //String s = "(7,(0,0,(0,9),0,(0,14),(0,12),(0,23),0,(9,3),9,(17,15),6,4,7,(3,0),3,(22,0),9,1,8,4,0,0,(10,0),6,4,(1,6),1,0,0,0,(11,0),3,6,2,0,(0,2),(0,1),0,(0,7),(9,5),9,(3,0),2,1,(12,0),7,5,0))";
        String s = "(7, (0, (0, 5), 0, 0, (0, 28), 0, (0, 11), (2, 0), 2, 0, (8, 6), 8, (9, 0), 9, (3, 0), 3, (5, 0), 1, 4, (2, 0), 2, 0, 0, (14, 8), 5, 9, 0, 0, 0, (8, 0), 8, (7, 0), 7, 0, 0, 0, 0, 0, (0, 7), 0, 0, (0, 5), 0, 0, (7, 0), 7, 0, (5, 0), 5))";
        
        //String s = "(4, (0,(0,7),(0,3),0,(6,0),4,2,0,3,2,1,0,2,1,0,0))";
        s = s.replaceAll(" ","");
        einlesen(s);
        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
              System.out.print(einMatrix[i][j] + ",");
              if(j==6) System.out.println(einMatrix[i][j] + ",");
            }
        }
        
        mengenFuerSumme = moeglichkeiten.getSumme();
        
        System.out.println(mengenFuerSumme);
        
        horizontal = (HashSet<Set<Integer>>[][])Array.newInstance(HashSet.class, groesse, groesse);
        vertical = (HashSet<Set<Integer>>[][])Array.newInstance(HashSet.class, groesse, groesse);
        
        for(int j = 0; j < groesse; j++) {
            for(int k = 0; k < groesse; k++) {
                horizontal[j][k] = new HashSet<Set<Integer>>();
                vertical[j][k] = new HashSet<Set<Integer>>();
            }
        }
        
        calc();
 
        System.out.println("------");
        
        for(int i=0;i<groesse;i++) {
            for(int k=0;k<groesse;k++) {
                System.out.println(horizontal[i][k]);
            }
        }
        
        System.out.println("------");
        
        for(int i=0;i<groesse;i++) {
            for(int k=0;k<groesse;k++) {
                System.out.println(vertical[i][k]);
            }
        }
        
    }
    
    
    public static void calc() {

        boolean[][] checkHorizontal = new boolean[groesse][groesse];
        boolean[][] checkVertical = new boolean[groesse][groesse];
                
        for(int i=0;i<groesse;i++) {
            for(int j=0;j<groesse;j++) {
                LinkedList<Integer> el = einMatrix[i][j];
                int size = el.size();

                //System.out.println(el.getFirst());
                
                if(size==2) {
                    int horizont = 0;
                    int verti = 0;
                    
                    if(el.getFirst()!=0 && !checkHorizontal[i][j]) {
                        horizont = goRight(i,j+1);
                        //System.out.println("H " + el.getFirst() + " " + j + " " + horizont);
                        for(int k=j+1;k<j+1+horizont;k++) {
                            //System.out.println("HW " +el.getFirst()+ " "+el.getLast() + " "+ j + " " + horizont);
                            checkHorizontal[i][k] = true;
                            
                            Set<Set<Integer>> resHor = mengenFuerSumme.get(el.getFirst());
                            
                            //System.out.print(resHor);
                            
                            for(Set<Integer> itr: resHor) { //for(Integer i : s)
                                if(itr.size()==horizont) {
                                    horizontal[i][k].add(itr);
                                    //System.out.println(itr);
                                }
                            }
                        }
                    }
                    
                     
                    if(el.getLast()!=0 && !checkVertical[i][j]) {
                        verti = goDown(i+1,j); // Anzahl an Feldern
                        //System.out.println("W " + el.getLast() + " " + i + " " + verti);
                        
                        for(int k=i+1;k<i+1+verti;k++) {
                            checkVertical[k][j] = true;
                            
                            Set<Set<Integer>> resVer = mengenFuerSumme.get(el.getLast());
                            
                            //System.out.println(resVer);
                            
                            for(Set<Integer> itr: resVer) { //for(Integer i : s)
                                //System.out.println(itr.size());
                                if(itr.size()==verti) {
                                    vertical[k][j].add(itr);
                                }
                            }
                        }
                    }
                    
                    checkHorizontal[i][j] = true;
                    checkVertical[i][j] = true;
                }
                
            }
        }
    }
    
    public static int goDown(int i,int j) {
        int count = 0;
        while(i<groesse && einMatrix[i][j].size()==1 && einMatrix[i][j].getFirst()!=0) {
            i++;
            count++;
        }
        return count;
    }
    
    public static int goRight(int i,int j) {
        int count=0;
        while(j<groesse && einMatrix[i][j].size()==1 && einMatrix[i][j].getFirst()!=0) {
            //System.out.println("goRight" +  einMatrix[i][j].size());
            j++;
            count++;
        }
        return count;
    }    
}
