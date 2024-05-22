package tty.ttw.components;

import java.awt.*;

public final class Pop {

  // defaults
  public int limit_y           = 25;
  public float font_size       = 10f;
  public Color color           = Color.BLACK;
  public int dt                = 5;
  public float alpha           = 1f;
  public float dalpha          = 0.005f;
  public int dy                = 4;
  public int current_life_time = 0;


  public String id;
  public Type type;
  public String msg;
  public int mlifetime;
  int x;
  int y;
  int aw;
  int ah;

  public Pop(
    String msg,
    int x,
    int y,
    int aw,
    int ah,
    Type type,
    int mlifetime,
    String id
  ) {
    this.msg       = msg;
    this.type      = type;
    this.mlifetime = mlifetime;
    this.id        = id;
    this.x         = x;
    this.y         = y;
    this.aw        = aw;
    this.ah        = ah;
    this.dt        = dt;
  }

  @Override
  public String toString () {
    return String.format("Pop(%s, %d, %d, %d, %d, %s, %d)", msg, x, y, aw, ah, type.toString(), mlifetime);
  }

}
