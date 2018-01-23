package edu.uci.github.dig_dug;

import java.lang.Thread;
/**
 * Created by fxuyi on 5/17/2017.
 */

//AI Monsters control thread
public class MonsterThread extends Thread {
    GameView gameView;
    private monster Slime[];
    private dragon Smaug;
    private int sleeptime=1000;

    public MonsterThread(GameView cv){ gameView = cv; }

    //Get location relationship between a single monster and the hero
    public int getDirection(int x,int y){
        int ret=0;
        int Hero_X=gameView.worker.x;
        int Hero_Y=gameView.worker.y;
        if (x==Hero_X){
            if (y==Hero_Y){ret = 0;}  //Meet
            if (y<Hero_Y) {ret = 1;}   //Up
            if (y>Hero_Y) {ret = 5;}   //Down
        }
        else if (x<Hero_X){
            if (y==Hero_Y){ret = 7;}   //left
            if (y>Hero_Y){ret = 6;}    //down-left
            if (y<Hero_Y) {ret = 8;}   //up-left
        }
        else if (x>Hero_X){
                if (y==Hero_Y){ret = 3;}   //right
                if (y>Hero_Y){ret = 4;}    //down-right
                if (y<Hero_Y) {ret = 2;}   //up-right
        }
        return ret;
    }

    //Try to move one monster to one possible direction
    public boolean tryMove(int try_x,int try_y,monster oneMonster){
        if (gameView.checkChannel(oneMonster.x+try_x,oneMonster.y+try_y)){
            oneMonster.move(try_x,try_y);
            return true;
        }
        else{return false;}
    }

    //Find the best order to check path
    public void findOrder(int direction, monster oneMonster){
        switch (direction){
            case 1:    //down-left-right-up
            case 2:{
                if (!tryMove(0,1,oneMonster)){
                    if (!tryMove(-1,0,oneMonster)){
                        if (!tryMove(1,0,oneMonster)){
                            tryMove(0,-1,oneMonster);
                        }
                    }
                }
                break;
            }
            case 3:    //left-up-down-right
            case 4:{
                if (!tryMove(-1,0,oneMonster)){
                    if (!tryMove(0,-1,oneMonster)){
                        if (!tryMove(0,1,oneMonster)){
                            tryMove(1,0,oneMonster);
                        }
                    }
                }
                break;
            }
            case 5:
            case 6:{    //up-right-left-down
                if (!tryMove(0,-1,oneMonster)){
                    if (!tryMove(1,0,oneMonster)){
                        if (!tryMove(-1,0,oneMonster)){
                            tryMove(0,1,oneMonster);
                        }
                    }
                }
                break;
            }
            case 7:   //right-down-up-left
            case 8:{
                if (!tryMove(1,0,oneMonster)){
                    if (!tryMove(0,1,oneMonster)){
                        if (!tryMove(0,-1,oneMonster)){
                            tryMove(-1,0,oneMonster);
                        }
                    }
                }
                break;
            }
            default:{break;}
        }
    }

    public void ghostMove(int direction, monster oneMonster){
        int move_x=0;
        int move_y=0;
        switch(direction){
            case 1:{move_x=0;move_y=1;break;}
            case 2:{move_x=-1;move_y=1;break;}
            case 3:{move_x=-1;move_y=0;break;}
            case 4:{move_x=-1;move_y=-1;break;}
            case 5:{move_x=0;move_y=-1;break;}
            case 6:{move_x=1;move_y=-1;break;}
            case 7:{move_x=1;move_y=0;break;}
            case 8:{move_x=1;move_y=1;break;}
        }
        oneMonster.move(move_x,move_y);
        if (gameView.checkChannel(oneMonster.x,oneMonster.y)){
            oneMonster.MonsterMode();
            sleeptime=1000;
        }
    }

    public void run(){
        //Global Object shortcut
        Slime = new monster[3];
        Slime[0] = gameView.slime[0];
        Slime[1] = gameView.slime[1];
        Slime[2] = gameView.slime[2];
        Smaug = gameView.smaug;
        while(!gameView.getGameOver()) {
            //Monster Control
            for (int i=0;i<3;i++){
                if (Slime[i].status){
                    int direction=getDirection(Slime[i].x,Slime[i].y);
                    if (direction==0){  //Meet, kill Hero
                        gameView.worker.kill();
                        return; //Kill Thread, no more Monster movement needed
                    }
                    //Ghost Mode
                    if (Slime[i].counter>=(8+4*i)){
                        Slime[i].GhostMode();
                        sleeptime=1400; //Slow down when turn to ghost
                    }
                    //Move
                    if (Slime[i].ghost){
                        ghostMove(direction,Slime[i]);
                    }
                    else {
                        findOrder(direction,Slime[i]);
                        Slime[i].counter++;
                    }
                }
            }
            //Dragon Control
            if (Smaug.status){
                int direction=getDirection(Smaug.x,Smaug.y);
                if (direction==0){
                    gameView.worker.kill();
                    return;
                }
                //Reset Effect Display
                if (Smaug.fireCounter==-8){
                    gameView.resetEffect(10);
                    Smaug.fireCounter=0;
                }
                //Ghost Mode
                if (Smaug.counter>=20){
                    Smaug.GhostMode();
                    sleeptime=1400; //Slow down when turn to ghost
                }
                //Dragon Fire
                if (Smaug.fireCounter>=25){
                    if (gameView.Decray(direction)){
                        gameView.worker.kill();
                        return;
                    }
                    Smaug.fireCounter=-9;
                }
                else if (Smaug.ghost){  //Move
                    ghostMove(direction,Smaug);
                }
                else {
                    findOrder(direction,Smaug);
                    Smaug.counter++;
                    Smaug.fireCounter++;
                }
            }

            //Delay
            try{
                sleep(sleeptime);
            }
            catch(InterruptedException e) {
                System.out.println("Exception occured");
            }
        }//End of While
    }
}
