package edu.uci.github.dig_dug;

/**
 * Created by fxuyi on 5/20/2017.
 */

public class layer {
    private int index;
    public int x;
    public int y;
    protected boolean status;

    public layer(int x, int y) {
        this.x = x;
        this.y = y;
        this.status = false;
        this.index = 9;
    }
    public void setStone(){
        this.status=true;
        this.index=7;
    }
    public void setLaser(){
        this.status=true;
        this.index=11;
    }
    public void setFire(){
        this.status=true;
        this.index=10;
    }
    public void setDeath(){
        this.status=true;
        this.index=12;
    }
    public void reSet(){
        this.status=false;
    }
    public int getIndex(){
        return index;
    }
}
