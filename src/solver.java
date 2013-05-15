
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
import java.util.Collection;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sarah
 */
public class solver {

    static LinkedList<Integer>[][] einMatrix; // einMatrix beinhaltet das eingelesene Kakuro
    
    static int groesse;
    
    static Map<Integer, Set<Set<Integer>>> mengenFuerSumme; // beinhaltet die Moeglichkeiten
    
    static LinkedList<Set<Integer>>[][] horizontal; // beinhaltet alle Sets von Integer, die in der Horizontalen moeglich sind
    static Set<Integer>[][] horizontalTemp; // beinhaltet alle Integers (nur einmal) in einer grossen Menge pro Feld
    
    
    static LinkedList<Set<Integer>>[][] vertical;
    static Set<Integer>[][] verticalTemp;            

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
        //String s = "(7, (0, (0, 5), 0, 0, (0, 28), 0, (0, 11), (2, 0), 2, 0, (8, 6), 8, (9, 0), 9, (3, 0), 3, (5, 0), 1, 4, (2, 0), 2, 0, 0, (14, 8), 5, 9, 0, 0, 0, (8, 0), 8, (7, 0), 7, 0, 0, 0, 0, 0, (0, 7), 0, 0, (0, 5), 0, 0, (7, 0), 7, 0, (5, 0), 5))";
        
        //String s = "(4, (0,(0,7),(0,3),0,(6,0),4,2,0,3,2,1,0,2,1,0,0))";
        String s = "(3,(0,(0,15),(0,8),(10,0),7,3,(13,0),8,5))";
        s = s.replaceAll(" ","");        
        
        einlesen(s);
        for(int i=0; i<groesse; i++){
            for(int j=0; j<groesse; j++){
              if(j==groesse-1) System.out.println(einMatrix[i][j] + ",");
              else System.out.print(einMatrix[i][j] + ",");
            }
        }
        
        mengenFuerSumme = moeglichkeiten.getSumme();
                
        horizontal = (LinkedList<Set<Integer>>[][])Array.newInstance(LinkedList.class, groesse, groesse);
        vertical = (LinkedList<Set<Integer>>[][])Array.newInstance(LinkedList.class, groesse, groesse);
        horizontalTemp = (Set<Integer>[][])Array.newInstance(Set.class,groesse,groesse);
        verticalTemp = (Set<Integer>[][])Array.newInstance(Set.class,groesse,groesse);
        
        for(int j = 0; j < groesse; j++) {
            for(int k = 0; k < groesse; k++) {
                horizontal[j][k] = new LinkedList<Set<Integer>>();
                vertical[j][k] = new LinkedList<Set<Integer>>();
                
                horizontalTemp[j][k] = new LinkedHashSet<Integer>();
                verticalTemp[j][k] = new LinkedHashSet<Integer>();
            }
        }
        
        
       calc(); // berechnet horizontal/vertical
        
       calcTemp(); //berechnet horizontalTemp/verticalTemp

               
        System.out.println("------");
        
        for(int i=0;i<groesse;i++) {
            for(int k=0;k<groesse;k++) {
                
                if(k==groesse-1) System.out.println(horizontalTemp[i][k] + ",");            
                else System.out.print(horizontalTemp[i][k]+ "-|-");
            }           
        }
       
       ds();
       
       printAll();
    }
    
    public static void ds() { //ds=durchschnitt        
        for(int i=0;i<groesse;i++) {
            for(int j=0;j<groesse;j++) {
                if(horizontalTemp[i][j].size()==0) {
                    horizontalTemp[i][j].addAll(verticalTemp[i][j]);
                }
                
                if(verticalTemp[i][j].size()==0) {
                    verticalTemp[i][j].addAll(horizontalTemp[i][j]);
                }
                
                horizontalTemp[i][j].retainAll(verticalTemp[i][j]);
            }
        }
    }
    
    
    // Horizontal,HorizontalTemp,Vertical, VerticalTemp
    public static void printAll() {
        System.out.println("------");
        
        for(int i=0;i<groesse;i++) {
            for(int k=0;k<groesse;k++) {
                
                if(k==groesse-1) System.out.println(horizontal[i][k] + ",");            
                else System.out.print(horizontal[i][k]+ "-|-");
            }           
        }
        
        System.out.println("------");
        
        for(int i=0;i<groesse;i++) {
            for(int k=0;k<groesse;k++) {
                
                if(k==groesse-1) System.out.println(horizontalTemp[i][k] + ",");            
                else System.out.print(horizontalTemp[i][k]+ "-|-");
            }           
        }

        
        System.out.println("------");
        
        for(int i=0;i<groesse;i++) {
            for(int k=0;k<groesse;k++) {
                
                if(k==groesse-1) System.out.println(vertical[i][k] + ",");            
                else System.out.print(vertical[i][k] + "-|-");
            }

        }
                 
        System.out.println("------");
        
        for(int i=0;i<groesse;i++) {
            for(int k=0;k<groesse;k++) {
                
                if(k==groesse-1) System.out.println(verticalTemp[i][k] + ",");            
                else System.out.print(verticalTemp[i][k]+ "-|-");
            }           
        }
    }
    
    public static void calcTemp() {
        for(int i=0;i<groesse;i++) {
            
            for(int j=0;j<groesse;j++) {            
                
                // Vereinigt alle Mengen Von Integers in EINER Menge (LinkedHashSet) von Integers
                int k = horizontal[i][j].size();              
                if(k>0) horizontalTemp[i][j].addAll((Collection<? extends Integer>) deepCopy.clone(horizontal[i][j].getFirst()));
                
                for(int l=1;l<k;l++) {
                    horizontalTemp[i][j].addAll((Collection<? extends Integer>) deepCopy.clone(horizontal[i][j].get(l)));
                    
                }
                
                

                int m = vertical[i][j].size();
                if(m>0) verticalTemp[i][j].addAll((Collection<? extends Integer>) deepCopy.clone(vertical[i][j].getFirst()));

                for(int n=1;n<m;n++) {
                    verticalTemp[i][j].addAll((Collection<? extends Integer>) deepCopy.clone(horizontal[i][j].get(n)));
                }
                
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
                
                if(size==2) { // "size==2: Feld das berchnet werden muss            
                    
                    // Es werden alle Felder nach rechts abgesucht, bis wir auf ein Feld stossen, was (siehe goRight)
                    // a) es nicht mehr gibt, weil wir uns ausserhalb vom Feld befinden
                    // b) Wir auf ein neues Feld der Form (x,y) stossen.
                    // c) Wir auf ein schwarzes Feld stossen (0)
                    if(el.getFirst()!=0 && !checkHorizontal[i][j]) {
                        
                        int horizont = goRight(i,j+1); // Summe von Feldern
                        
                        for(int k=j+1;k<j+1+horizont;k++) {
                            
                            checkHorizontal[i][k] = true;
                            Set<Set<Integer>> resHor = mengenFuerSumme.get(el.getFirst()); //Fragen die Zahl ab...
                                                        
                            for(Set<Integer> itr: resHor) {
                                if(itr.size()==horizont) { // ...aber nehmen nur Sets von Integern, deren Anzahl mit der Summe von Feldern uebereinstimmt.
                                    horizontal[i][k].add(itr);
                                }
                            }
                        }
                    }
                    
                    // Analog zu oben nur in der Vertikalen 
                    if(el.getLast()!=0 && !checkVertical[i][j]) {
                        
                        int verti = goDown(i+1,j); // Anzahl an Feldern
                        
                        for(int k=i+1;k<i+1+verti;k++) {
                            
                            checkVertical[k][j] = true;
                            Set<Set<Integer>> resVer = mengenFuerSumme.get(el.getLast());
                            
                            for(Set<Integer> itr: resVer) { //for(Integer i : s)

                                if(itr.size()==verti) {
                                    vertical[k][j].add(itr);
                                }
                            }
                        }
                    }
                    
                    checkHorizontal[i][j] = true;
                    checkVertical[i][j] = true;
                    
                }
                
                if(size==1 && einMatrix[i][j].getFirst()==0) { // "Schwarzer block"
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
