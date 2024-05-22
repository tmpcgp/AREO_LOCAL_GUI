package tty.ttw.components;

import tty.ttw.components.*;
import java.awt.event.*;
import tty.ttw.*;

public class CustomKL implements KeyListener{

  public TPanel panel;

  public CustomKL(TPanel panel) {
    this.panel = panel;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();

    switch (code) {
      case KeyEvent.VK_ENTER: {
        TPanel.state = AppState.OPENING;
        break;
      }
      // for closing the local client
      case KeyEvent.VK_ESCAPE: {
        System.out.println("@ESCAPE >> "+true);
        Pop p = new Pop(
          "Exiting Application.",
          5,
          -20,
          5,
          5,
          Type.OK,
          100,
          "Exit"
        );

        Utils.enq(TPanel.q, p);

        TPanel.state = AppState.CLOSING;
        break;
      }
      case KeyEvent.VK_M: {
        if (TPanel.state == AppState.CLOSING) {
          Pop p = new Pop(
            "Can't Toggle Menu" /* msg */,
            5   /* starting x */, 
            -20 /* starting y */,
            5   /* arcwidth   */,
            5   /* archeight  */,
            Type.ERROR /* type of popUP */,
            100 /* max lifetime after death */,
            "Menu/exit"
          );

          Utils.enq(TPanel.q, p);
        } else {
          System.out.println("@M >> "+true);
          TPanel.state = AppState.MENU;
        }
        break;
      }
      case KeyEvent.VK_Q: {
        if (TPanel.state == AppState.CLOSING) {
          Pop p = new Pop(
            "Already Shutting Down The Server." /* msg */,
            5   /* starting x */, 
            -20 /* starting y */,
            5   /* arcwidth   */,
            5   /* archeight  */,
            Type.OK /* type of popUP */,
            100 /* max lifetime after death */,
            "Already Shut"
          );

          Utils.enq(TPanel.q, p);
        }

        TPanel.state = AppState.CLOSING;
        break;
      }

      // @Incomplete adding things.
      case KeyEvent.VK_S: {
        TPanel.state = AppState.STATISTICS;
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

}
