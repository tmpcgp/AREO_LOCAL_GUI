package tty.ttw;

import java.lang.Math;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.*;
import tty.ttw.components.*;
import java.io.*;


public final class Utils {
  private final static String values = "Ñ@#W$9876543210?!abc;:+=-,._          ";

  private static int map_range(int value, int low1, int high1, int low2, int high2) {
    return low2 + (high2 - low2) * (value - low1) / (high1 - low1);
  }

  public static char compute_from_grayscale_value(int value) {
    final int idx = (int) Math.floor(map_range(value, 0, 255, 0, values.length()-1));
    return values.charAt(idx);
  }


  public static char[][] loadImage2Matrix(int bx, int by, BufferedImage image) {

    final long curr = System.nanoTime();
    char[][] colors = new char[
      image.getWidth() <= bx ? bx : image.getWidth()
    ][
      image.getHeight() <= by ? by : image.getHeight()
    ];

    for (int x = 0; x < bx; x++) {
      for (int y = 0; y < by; y++) {
        final int argb = image.getRGB(x, y);

        int b = (argb)     &0xFF;
        int g = (argb>>8)  &0xFF;
        int r = (argb>>16) &0xFF;

        colors[x][y] = compute_from_grayscale_value((b + g + r)/3);
      }
    }

    final long end = System.nanoTime();
    System.out.println("@time for parsing image "+((end - curr)/1_000_000)+" ms.");

    return colors;
  }

  public static int compute_str_with_font(String str, Graphics g) {
    final FontMetrics fm           = g.getFontMetrics();
    final Rectangle2D stringBounds = fm.getStringBounds(str, g);

    return (int)stringBounds.getWidth();
  }

  public static int compute_str_with_font_h(String str, Graphics g) {
    final FontMetrics fm           = g.getFontMetrics();
    final Rectangle2D stringBounds = fm.getStringBounds(str, g);

    return (int)stringBounds.getHeight();
  }

  public static char[][] compute_matrix_from_file(String fn) {
    try {
      InputStream is  = Utils.class.getClassLoader().getResourceAsStream(fn);
      Scanner scanner = new Scanner(is);

      int line_count           = 0;
      int max_size_t           = 0;
      String tmp               = "";
      ArrayList<String> buffer = new ArrayList<>();
      
      while (scanner.hasNextLine()) {
        tmp = scanner.nextLine();
        if (max_size_t < tmp.length()) {
          max_size_t = tmp.length();
        }
        buffer.add(tmp);
        line_count ++;
      }
      
      System.out.println("@maxsize >> "+max_size_t);
      System.out.println("@line_count >> "+line_count);

      char[][] ret = new char[max_size_t][line_count];
      char[] tmpp;

      for (int i = 0; i < buffer.size(); i++) {
        ret[i] = buffer.get(i).toCharArray();
      }

      return ret;
    } catch (Exception e) {
      System.out.println("@compute_matrix_from_file >> "+e);
    }

    return null;
  }

  public static void enq(Pop[] arr, Pop element) {

    boolean dupe = false;
    for (Pop e : arr) {
      if (e != null) {
        if(e.id.equals(element.id)) {
          dupe = true;
          break;
        }
      } else {
        continue;
      }
    }

    for (int i = arr.length-1; i >= 0; i--) {
      System.out.println("@element >> "+element);
      if (arr[i] == null && !dupe) {
        arr[i] = element;
        break;
      } else {
        continue;
      }
    }

    System.out.println("@arr >> "+Arrays.toString(arr));
  }

  public static Pop dq(Pop[] arr) {
    for (int i = arr.length-1; i >= 0; i--) {
      if (arr[i] != null) {
        Pop ret = arr[i];
        arr[i]  = null;

        return ret;
      } else {
        continue;
      }
    }

    return null;
  }

  public static float clamp(float value, float max, float min) {
    return Math.max(min, Math.min(max, value));
  }
}
