package tty.ttw;

import javax.swing.*;
import tty.ttw.components.TPanel;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.lang.Math;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.io.*;



public class App {

  public static final int W = 2*500;
  public static final int H = 2*300;

  public static void main( String[] args ) throws IOException {

    JFrame jframe = new JFrame("Areobot Local Server [ALS]");
    jframe.setSize(W, H);

    TPanel canvas  = new TPanel(W, H);
    jframe.add(canvas);

    jframe.setFocusable(false);
    jframe.setLocationRelativeTo(null);
    jframe.setVisible(true);
    jframe.setResizable(false);
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
