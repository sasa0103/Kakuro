
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet; // Sarah ich habe LinkedHashset genommen weil Hashset wird sortiert. Z.B. wird (3,0) zu (0,3)
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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
    static HashSet<Integer>[][] einMatrix;
    static HashSet<Integer>[][] horizont;
    static HashSet<Integer>[][] vertikal;
    static int groesse;

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
      einMatrix = (HashSet<Integer>[][])Array.newInstance(LinkedHashSet.class, groesse, groesse);
      for(int j = 0; j < groesse; j++) {
          for(int k = 0; k < groesse; k++) {
              einMatrix[j][k] = new LinkedHashSet<Integer>();
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
    public static void main(String[] args) {
        //String s = "(7,(0,0,(0,9),0,(0,14),(0,12),(0,23),0,(9,3),9,(17,15),6,4,7,(3,0),3,(22,0),9,1,8,4,0,0,(10,0),6,4,(1,6),1,0,0,0,(11,0),3,6,2,0,(0,2),(0,1),0,(0,7),(9,5),9,(3,0),2,1,(12,0),7,5,0))";
        String s = "(4, (0,(0,7),(0,3),0,(6,0),4,2,0,3,2,1,0,2,1,0,0))";
        s = s.replaceAll(" ","");
        einlesen(s);
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
              System.out.print(einMatrix[i][j] + ",");
              if(j==3) System.out.println(einMatrix[i][j] + ",");
            }
        }
        
    }
    
    
    public static void horizontal(HashSet<Integer>[][] h){
        
      horizont = (HashSet<Integer>[][])Array.newInstance(HashSet.class, groesse, groesse);
      for(int j = 0; j < groesse; j++) {
          for(int k = 0; k < groesse; k++) {
              einMatrix[j][k] = new HashSet<Integer>();
          }
      }
      horizont = h.clone();
      
      for(int i=0; i<groesse; i++){
          for(int j=0; j<groesse; j++){
              if(einMatrix[i][j].size() == 2){
                  System.out.println("doppelt: " + i+j+"inhalt:" + einMatrix[i][j]);
              }
          }
      }
      
    }
    
    /*
    public static void solve() {
        HashSet<HashSet<Integer>>[][] hori,verti;
        temp = (HashSet<HashSet<Integer>>[][])Array.newInstance(HashSet.class, 3, 3);
        for(int i=0;i<3;i++) {
            for(int k=0;k<3;k++) {
                temp[i][k] = new HashSet<HashSet<Integer>>();
            }
        }
    }*/
    
}
