package edu.uci.github.dig_dug;

/**
 * Created by fxuyi on 5/15/2017.
 */

public class hero {
    protected int index;
    protected int x;
    protected int y;
    protected boolean status;
    protected int face_x;
    protected int face_y;

    public hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.index = 8;
        this.status=true;
        this.face_x=0;
        this.face_y=0;
    }
    public void kill(){
        this.status=false;
    }

    public void turn(int direction){
        switch (direction){
            case 0:{
                this.index=13;
                break;
            }
            case 1:{
                this.index=15;
                break;
            }
            case 2:{
                this.index=16;
                break;
            }
            case 3:{
                this.index=14;
                break;
            }
            default:{
                this.index=8;
                break;
            }
        }
    }

    public void move(int x_move, int y_move){
        final int MaxColumn=11;
        final int MaxRow=13;
        this.face_x=x_move;
        this.face_y=y_move;

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
}
