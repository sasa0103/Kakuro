/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Lefix
 */

// Quelle ;) http://www.informatik-student.de/2006/12/10/deep-copy-von-java-objekten/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class deepCopy {

  public static Object clone(Object copyObject) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(copyObject);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      Object deepCopy = ois.readObject();
      return deepCopy;
    } catch (IOException e) {
      e.printStackTrace();
    } catch(ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}