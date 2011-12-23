package ru.hh.school.stdlib;

import java.util.*;

public class Substitutor3000 {
    HashMap<String, String> map = new HashMap<String, String>();
  public synchronized void put(String key, String value) {

      map.put(key, value);
  }

  public synchronized String get(String key) {
    if (this.map.containsKey(key)) {
        Parser p = new Parser();
        String newValue = p.parse(map.get(key), map);
        //System.out.println("+ "+key +"\n"+newValue);
        return newValue;
    }
      else {
        return "";
    }
  }
}
