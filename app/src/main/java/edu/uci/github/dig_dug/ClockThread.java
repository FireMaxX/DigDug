package edu.uci.github.dig_dug;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.lang.Thread;

/**
 * Created by fxuyi on 5/10/2017.
 */

//redraw Canvas and check whether hero alive every 0.2s
public class ClockThread extends Thread {
    GameView gameView;
    public ClockThread(GameView cv){
        gameView = cv;
    }

    //reDraw Canvas according to the current icon status
    public void  reDraw() {
        SurfaceHolder holder = gameView.getHolder();
        ;
        Canvas canvas = holder.lockCanvas();

        if (canvas != null) {
            gameView.draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void check(){
        int hero_x=gameView.worker.x;
        int hero_y=gameView.worker.y;
        int MonsterAmount=gameView.slime.length;

        for (int i=0;i<MonsterAmount;i++){
            if ((gameView.slime[i].status)&&(gameView.slime[i].x==hero_x)&&(gameView.slime[i].y==hero_y)){
                gameView.worker.kill();   //Meet, Hero die
                return;
            }
        }
        if ((gameView.smaug.status)&&(gameView.smaug.x==hero_x)&&(gameView.smaug.y==hero_y)){
            gameView.worker.kill();   //Meet, Hero die
        }
    }

    public void run(){
        while(!gameView.getGameOver()) {
            reDraw();
            check();
            try{
                sleep(200);
            }
            catch(InterruptedException e) {
                System.out.println("Exception occured");
            }
        }
        //Last Call for Draw after GameOver to display EndGame Message
        reDraw();
        try{
            sleep(500);
        }
        catch(InterruptedException e) {
            System.out.println("Exception occured");
        }
        return; //Kill Thread, no more reDraw
    }
}
