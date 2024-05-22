package tty.ttw.components;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import tty.ttw.*;
import java.lang.*;
import java.util.*;
import tty.ttw.Utils;


public final class PaintUtils {

  private static final int LIMIT = 200;

  public static void display_pop_up(Pop pop, Graphics g) {
    System.out.println("@display_pop_up POP >> "+pop);

    // re-computing x and y
    final int w = (int)Utils.compute_str_with_font(pop.msg, g);
    final int x = (int)App.W/2-(int)w/2;
    final int h = Utils.compute_str_with_font_h(pop.msg, g);

    g.setFont(g.getFont().deriveFont(pop.font_size));

    // drawing the pop_up container
    switch (pop.type) {
      case WARNING: {
        g.setColor(new Color(245f/255f, 196f/255f, 132f/255f, pop.alpha));
        break;
      }
      case OK: {
        g.setColor(new Color(140f/255f, 255f/255f, 144f/255f, pop.alpha));
        break;
      }
      case ERROR: {
        g.setColor(new Color(247f/255f, 92f/255f, 110f/255f, pop.alpha));
        break;
      }
    }

    g.fillRoundRect(x, pop.y, w, h*2, pop.aw, pop.ah);
    g.setColor(new Color(pop.color.getRed(), pop.color.getGreen(), pop.color.getBlue(), pop.alpha));
    g.drawString(pop.msg, (int)Utils.compute_str_with_font(pop.msg, g), pop.y + (int)h);
  }
}
