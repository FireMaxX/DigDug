package edu.uci.github.dig_dug;

/**
 * Created by fxuyi on 5/16/2017.
 */

public class dragon extends monster {
    private int index;
    protected int fireCounter;
    public dragon(int x, int y, boolean dragon){
        super(x,y,dragon);
        this.index=6;
        this.fireCounter=0;
    }
    public void MonsterMode(){
        this.ghost=false;
        this.index=6;
        this.counter=0;
    }

    public void GhostMode(){
        this.ghost=true;
        index=4;
    }

    public int getIndex(){
        return index;
    }
    public void setIndex(int index) { this.index=index; }
}
