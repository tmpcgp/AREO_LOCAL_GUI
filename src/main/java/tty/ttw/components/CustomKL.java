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
        if (TPanel.state == AppState.OPENING) {
          Pop p = new Pop(
            "Press <Escape/> To Kill Main Process." /* msg */,
            5   /* starting x */, 
            -20 /* starting y */,
            5   /* arcwidth   */,
            5   /* archeight  */,
            Type.ERROR /* type of popUP */,
            100 /* max lifetime after death */,
            "Kill Main" /* Giving description id */
          );

          Utils.enq(TPanel.q, p);

        } else if (TPanel.state == AppState.CLOSING) {
           Pop p = new Pop(
            "Can't Abort Kill Main Process." /* msg */,
            5   /* starting x */, 
            -20 /* starting y */,
            5   /* arcwidth   */,
            5   /* archeight  */,
            Type.ERROR /* type of popUP */,
            100 /* max lifetime after death */,
            "Abort Kill"
          );

          Utils.enq(TPanel.q, p);

        }
        else {
          TPanel.state = AppState.OPENING;
        }
        break;
      }
      case KeyEvent.VK_ESCAPE: {
        if (TPanel.state == AppState.CLOSING) {
           Pop p = new Pop(
            "Already Killing Main Process."/* msg */,
            5   /* starting x */, 
            -20 /* starting y */,
            5   /* arcwidth   */,
            5   /* archeight  */,
            Type.OK /* type of popUP */,
            100 /* max lifetime after death */,
            "Already Kill"
          );

          Utils.enq(TPanel.q, p);
        }

        TPanel.state = AppState.CLOSING;
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
