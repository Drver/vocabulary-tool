package org.drver.vocabulary.tool.util;

import java.util.List;
import java.util.Random;

public class Util {

  public static <T> void shuffle(List<T> list) {

    int size = list.size();
    Random random = new Random();

    for(int i = 0; i < size; i++) {
      int randomPos = random.nextInt(size);
      T temp = list.get(i);
      list.set(i, list.get(randomPos));
      list.set(randomPos, temp);
    }
  }

  public static void println(String str) {
    System.out.println(str);
  }
}
