package tty.ttw.components;

import java.lang.*;
import tty.ttw.components.*;

public class Loop implements Runnable {

  public TPanel panel;

  public Loop(TPanel panel) {
    this.panel = panel;
  }

  @Override
  public void run() {
    System.out.println("@run");

    long lastLoopTime       = System.nanoTime();
    final int TARGET_FPS    = 60;
    final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    long lastFpsTime        = 0;

    while(true){
      long now          = System.nanoTime();
      long updateLength = now - lastLoopTime;
      lastLoopTime      = now;
      double delta      = updateLength / ((double)OPTIMAL_TIME);

      lastFpsTime += updateLength;
      if(lastFpsTime >= 1000000000){
        lastFpsTime = 0;
      }

      //panel.updateGame(delta);
      panel.repaint();

      try{
        Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
      } catch(Exception e){
        System.out.println("@e, run[] "+e);
      }
    }
  }
}
