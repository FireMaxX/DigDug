package edu.uci.github.dig_dug;

/**
 * Created by fxuyi on 5/16/2017.
 */

public class monster {
    private int index;
    protected int x;
    protected int y;
    protected boolean status;
    protected boolean ghost;
    protected int counter;

    public monster(int x, int y, boolean dragon){
        this.x=x;
        this.y=y;
        this.status=true;
        this.ghost=false;
        this.counter=0;
        if (dragon){
            this.index=6;
        }
        else{
            this.index=5;
        }
    }
    public void GhostMode(){
        this.ghost=true;
        index=4;
    }
    public void MonsterMode(){
        this.ghost=false;
        index=5;
        this.counter=0;
    }

    public void move(int x_move, int y_move){
        final int MaxColumn=11;
        final int MaxRow=13;

        if (x+x_move>=MaxColumn){
            this.x=MaxColumn-1;
        }
        else if (x+x_move<0){
            this.x=0;
        }
        else if (y+y_move>=MaxRow){
            this.y=MaxRow-1;
        }
        else if (y+y_move<1){
            this.y=1;
        }
        else {
            this.x = x+x_move;
            this.y = y+y_move;
        }
    }

    public int getIndex(){
        return index;
    }
    public void setIndex(int index) { this.index=index; }
}
