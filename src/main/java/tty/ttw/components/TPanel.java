package tty.ttw.components;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import tty.ttw.components.*;
import java.lang.*;
import java.util.*;
import tty.ttw.Utils;

public class TPanel extends JPanel {

  public static char[][][] frames = new char[2][][];
  private static String str = "";
  public static Font font;
  public static int ys =  -50;
  public static boolean single_pop_up = false;
  public final float dalpha      = 0.004f;
  public static float alpha      = 0f;
  public static boolean reversed = false;
  public static AppState state   = AppState.LOADING;

  // counters for the animations
  public static int cy    = 0;
  public static int cx    = 0;
  public static int ck    = 0;
  public static float cw  = 0f;
  public static int steps = 75;

  // to set up
  public static Color color1 = Color.RED;
  public static Color color2 = Color.BLUE;


  private static int usize = 5;
  // @Incomplete allow for multiple pop's
  public static Pop[] q = new Pop[usize];
  public static Pop pop;

  public int W;
  public int H;
  
  public TPanel(/*BufferedImage image,*/ int W, int H) {
    setBorder(BorderFactory.createLineBorder(Color.black));

    // initiating...
    // paintComponent is a sort of game loop.
    this.W = W;
    this.H = H;

    Thread game = new Thread(new Loop(this));
    game.start();

    try {
      InputStream stream = TPanel.class.getClassLoader().getResourceAsStream("PressStart2P-Regular.ttf");
      System.out.println("@inputStream >> "+stream);
      font               = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);

      System.out.println("@font >> "+font);

    } catch (FontFormatException | IOException e) {
      System.out.println("@e @TPanel constructor "+e);
    }

    // now we need a way to incorporate this frame into the back_buffer;
    // let's use some coordinates
    final int sx = 10;
    final int sy = 45;

    frames[0] = Utils.compute_matrix_from_file("ascii.txt");
    frames[1] = Utils.compute_matrix_from_file("tag.txt");

    this.setFocusable(true);
    this.addKeyListener(new CustomKL(this));
  }


  // MAIN LOOP
  public void paintComponent(Graphics g) {
    super.paintComponent(g);       

    g.setColor(new Color(178, 220, 237));
    g.fillRect(0, 0, W, H);
    g.setColor(Color.BLACK);
    g.setFont(font);


    final int dx = 20;
    final int dy = 20;


    if (pop != null) {
      PaintUtils.display_pop_up(pop, g);
      // if the pop.y is reaching the limit_y
      if (pop.y < pop.limit_y) {
        pop.y += pop.dy;
      } else {
        // check the lifetime of the stuff.
        if (pop.mlifetime <= pop.current_life_time) {
          pop.current_life_time += pop.dt;
        } else {
          // draw it but, fainting
          if (pop.alpha <= 0.1f) {
            // get the next pop
            pop = Utils.dq(q);
          } else {
            pop.alpha -= pop.dalpha;
          }
        }
      }
    } else {
      // rendered in the next frame
      pop = Utils.dq(q);
    }

    // printing that we're initialiting main process
    g.setColor(Color.WHITE);
    switch (state) {
      case LOADING: {

        alpha = Utils.clamp(alpha, 1f, 0f);

        if (alpha <= 0.1f) {
          reversed = false;
        }
        if (alpha >= 0.99f) {
          reversed = true;
        }

        if (reversed) {
          alpha += -1 * dalpha;
        } else {
          alpha += dalpha;
        }

        g.setColor(new Color(0f, 0f, 0f, alpha));
        g.setFont(g.getFont().deriveFont(7f));

        // drawing the bot mascot frame
        // find out a way to center out this very well
        for (int i=0; i < frames[0].length; i++) {
          for (int j=0; j < frames[0][i].length; j++) {
            char value = frames[0][i][j];
            g.drawString(String.valueOf(value), j*6 + (int)W/3 - 20, i*10 + (int)H/10);
          }
        }

        // drawing the title of the project
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(10f));
        for (int i=0; i < frames[1].length; i++) {
          for (int j=0; j < frames[1][i].length; j++) {
            char value = frames[1][i][j];
            g.drawString(String.valueOf(value), j*10 + (int)W/4, i*10 + (int)H-200);
          }
        }

        g.setFont(g.getFont().deriveFont(15f));

        if (cx <= 200){
          str = "Tap <Enter/> To Start The Main Process";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 225);

          str = "Tap <M/> To Toggle Menu";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 250);

        } if (cx > 200 && cx <= 500) {
          str = "Tap <Enter/> To Start The Main Process.";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 225);

          str = "Tap <M/> To Toggle Menu.";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 250);

        } if (cx > 500 && cx <= 700){
          str = "Tap <Enter/> To Start The Main Process..";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 225);

          str = "Tap <M/> To Toggle Menu..";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 250);

        } if (cx > 700){
          str = "Tap <Enter/> To Start The Main Process...";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 225);

          str = "Tap <M/> To Toggle Menu...";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2 + 250);

          cx = 0;
        }

        cx+=5;
        break;
      }

      case OPENING: {
        g.setFont(g.getFont().deriveFont(50f));

        str = "AREOBOT";
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2-100);

        if (cy >= 200) {
          g.setColor(Color.GREEN);
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2) + dx, (int)H/2-100 + dy);
        } if (cy >= 500) {
          g.setColor(Color.RED);
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2) + (2*dx), (int)H/2-100 + (2*dy));
        } if (cy >= 700) {
          g.setColor(Color.GRAY);
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2) + (3*dx), (int)H/2-100 + (3*dy));
        } if (cy >= 700) {
          g.setColor(Color.BLUE);
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2) + (4*dx), (int)H/2-100 + (4*dy));
        } if (cy >= 1000){
          cy = 0;
        }

        g.setFont(g.getFont().deriveFont(20f));
        g.setColor(Color.WHITE);

        if (ck <= 200){
          str = "Initiating Xatkit Process";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2+100);
        } if (ck > 200 && ck <= 500) {
          str = "Initiating Xatkit Process.";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2+100);
        } if (ck > 500 && ck <= 700){
          str = "Initiating Xatkit Process..";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2+100);
        } if (ck > 700) {
          str = "Initiating Xatkit Process...";
          g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2+100);
          ck = 0;
        }

        cy += 5;
        ck += 5;
        break;
      }

      case CLOSING: {
        alpha = Utils.clamp(alpha, 1f, 0f);

        if (alpha <= 0.1f) {
          reversed = false;
        }
        if (alpha >= 0.99f) {
          reversed = true;
        }

        if (reversed) {
          alpha += -1 * dalpha;
        } else {
          alpha += dalpha;
        }

        g.setColor(new Color(0f, 0f, 0f, alpha));
        g.setFont(g.getFont().deriveFont(7f));

        // drawing the bot mascot frame
        // find out a way to center out this very well
        for (int i=0; i < frames[0].length; i++) {
          for (int j=0; j < frames[0][i].length; j++) {
            char value = frames[0][i][j];
            g.drawString(String.valueOf(value), j*6 + (int)W/3 - 20, i*10 + (int)H/10);
          }
        }

        str = "Thanks!";
        
        if (cw > steps) {
          cw = 0;
        }

        System.out.println("@cw >> "+cw);

        float ratio = (float) cw / (float) steps;
        int red   = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
        int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
        int blue  = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));

        g.setColor(new Color(red, green, blue));
        g.setFont(g.getFont().deriveFont(17f));
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), (int)H/2+150);

        if (false) {
          System.exit(0);
        }

        cw += 0.05;
        break;
      }


      case MENU: {
        str = "<Menu/>";

        int ssy = 100;

        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), ssy);
        g.setFont(g.getFont().deriveFont(15f));

        final int ddy = Utils.compute_str_with_font_h(str, g)*2;
        final int ddx = 50;
        final int cx  = (int)Utils.compute_str_with_font("</Toggle Menu           :: M", g)/2;

        ssy += 100;
        ssy += ddy;
        str = "</Toggle Menu           :: M";
        g.drawString(str, (int)W/2 - cx, ssy);

        ssy += ddy;
        str = "</Toggle Statistics     :: S";
        g.drawString(str, (int)W/2 - cx, ssy);

        ssy += ddy;
        str = "</Toggle Exit           :: ESCAPE";
        g.drawString(str, (int)W/2 - cx, ssy);

        ssy += ddy;
        str = "</Toggle Restart Server :: R";
        g.drawString(str, (int)W/2 - cx, ssy);

        ssy += ddy;
        str = "</Toggle Start Server   :: ENTER";
        g.drawString(str, (int)W/2 - cx, ssy);

        break;
      }

      case STATISTICS: {
        str = "<Statistics/>";

        int ssy = 100;

        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), ssy);
        g.setFont(g.getFont().deriveFont(15f));

        final int ddy = Utils.compute_str_with_font_h(str, g)*2;
        final int ddx = 50;

        ssy += 100;
        ssy += ddy;
        str = "</Number Request               :: ()";
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), ssy);

        ssy += ddy;
        str = "</Number Of States             :: ()";
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), ssy);

        ssy += ddy;
        str = "</Number Of Intents            :: ()";
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), ssy);

        ssy += ddy;
        str = "</Number Of Training Sentences :: ()";
        g.drawString(str, ((int)W/2 - (int)Utils.compute_str_with_font(str, g)/2), ssy);

        break;
      }


      default: {
        System.out.println("@else state is not recognized.");
        break;
      }
    }
  }
}
