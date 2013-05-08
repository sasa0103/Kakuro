/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Sarah
 */
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class moeglichkeiten {
	/**
	 * Hier stehen die Mengen drin, die für einen gewissen Wert in Frage kommen.
	 */
  static Map<Integer, Set<Set<Integer>>> mengenFuerSumme;

  public static void main(String... args) {
  	// Menge initialisieren
  	mengenFuerSumme = new HashMap<Integer, Set<Set<Integer>>>();
  	for(int i = 3; i <= 45; i++) {
  		mengenFuerSumme.put(i, new HashSet<Set<Integer>>());
  	}

  	// Alle Möglichen Zellenlängen
  	for(int i = 2; i <= 9; i++) {
  		insertFuerZellen(i);
  	}
  }

  /**
   * Fügt in mengenFuerSumme an die richtige Stelle die Mengen für entsprechende Summen ein.
   */ 
  static void insertFuerZellen(int anzahlZellen) {
    int[] kombinationen = new int[anzahlZellen];
    iteriere(kombinationen, 0);
  }

  /**
   * Alle Kombinationen für das Array werden einmal rekursiv durchgegangen.
   * Wenn pos < kombinationen.length - 1 ist, wird die Funktion rekursiv mit pos+1 aufgerufen,
   * sonst ist das Ende erreicht und jede Kombination wird nur einmal ausgeschrieben.
   */
  static void iteriere(int[] kombinationen, int pos) {
  	// rekursiv aufrufen
  	if(pos < kombinationen.length - 1) {
  		int start = pos == 0 ? 1 : kombinationen[pos - 1] + 1;
  		for(int i = start; i <= 9; i++) {
        kombinationen[pos] = i;
  			iteriere(kombinationen, pos+1);
  		}
  	}
  	// abbruch
  	if(pos == kombinationen.length - 1) {
      for(int i = kombinationen[kombinationen.length - 2] + 1; i <= 9; i++) {
        kombinationen[pos] = i;
        insertArray(kombinationen);
      }
    }
  }

  /**
   * Fügt das Array kombination richtig in mengenFuerSumme ein
   */
  static void insertArray(int[] kombination) {
    int val = 0;
    Set<Integer> theSet = new HashSet<Integer>();
    for(int i = 0; i < kombination.length; i++) {
      val += kombination[i];
      theSet.add(kombination[i]);
    }
    mengenFuerSumme.get(val).add(theSet);
  }
}
