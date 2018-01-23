package edu.uci.github.dig_dug;

/**
 * Created by fxuyi on 5/13/2017.
 */

public class land {
    private int index;
    public int x;
    public int y;
    protected boolean status;
    protected boolean stone;
    protected boolean channel;

    public land(int x, int y) {
        this.x = x;
        this.y = y;
        this.status = false;
        this.stone = false;
        this.channel=false;
        this.index = 9;
    }

    //Set land to soil ground
    public void set(boolean status, int index){
        this.status = status;
        this.index = index;
        this.channel = false;
    }

    public void setStone(boolean stone){
        this.stone=stone;
        this.status=!stone;
    }

    public void setChannel(){
        this.channel=true;
        this.index=9;
    }
    public int getIndex(){
        return index;
    }
}