package edu.uci.github.dig_dug;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import java.util.Random;

import static java.lang.Integer.max;

/**
 * Created by fxuyi on 5/10/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    protected Paint paint;
    private Bitmap icons[];
    private Bitmap buttons[];
    protected final int AmountofColumn=11;
    protected final int AmountofRow=17;
    protected land blocks[][];               //lower Layer Object Array
    protected layer effect[][];              //Upper Layer Object Array
    protected hero worker;                   //Hero Object
    protected int direction=5;              //Control Hero Movement
    protected boolean Touch_UP_Marker=false;     //Use to detect if onTouch event Action_Up is happen to prevent auto continuous moving
    protected monster slime[];              //Monster Object
    protected dragon smaug;                 //Dragon Object
    private boolean GameOver=false;        //GameLoop Control
    private String EndMessage="UnKnown";  //End Game Message
    protected boolean Exiting=false;        //Close App Locker

    //Main Thread, all object initializations
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        Random ran = new Random();

        // Initialize Object
        icons = new Bitmap[17];
        buttons = new Bitmap[5];
        blocks = new land[AmountofColumn][AmountofRow];
        effect = new layer[AmountofColumn][AmountofRow];
        worker = new hero(5,7);
        slime = new monster[3];
        slime[0] = new monster(2,4,false);
        slime[1] = new monster(8,3,false);
        slime[2] = new monster(9,10,false);
        smaug = new dragon(3,9,true);
        //Set LandScape/Effect Objects
        for (int i=0;i<AmountofColumn;i++){
            for (int j=0;j<AmountofRow;j++) {
                blocks[i][j]=new land(i,j);
                effect[i][j]=new layer(i,j);
            }
        }
        //Set Soil Object
        for (int i=0;i<AmountofColumn;i++){
            blocks[i][1].set(true,0);
            blocks[i][2].set(true,0);
            blocks[i][3].set(true,0);
            blocks[i][4].set(true,1);
            blocks[i][5].set(true,1);
            blocks[i][6].set(true,1);
            blocks[i][7].set(true,2);
            blocks[i][8].set(true,2);
            blocks[i][9].set(true,2);
            blocks[i][10].set(true,3);
            blocks[i][11].set(true,3);
            blocks[i][12].set(true,3);

        }
        //Set channel
        for (int i=0;i<3;i++){
            blocks[2][i+3].setChannel();    //For slime 0
            blocks[2+i][9].setChannel();    //For dragon
            blocks[7+i][3].setChannel();    //For slime 1
            blocks[9][9+i].setChannel();    //For slime 2
        }
        blocks[5][7].set(true,9);   //For worker
        //Set Stones
        blocks[3][2].setStone(true);
        effect[3][2].setStone();
        blocks[2][10].setStone(true);
        effect[2][10].setStone();
        blocks[7][8].setStone(true);
        effect[7][8].setStone();
    }
    //return GameOver Mark
    public boolean getGameOver(){
        return GameOver;
    }

    //Check if move to next block is legal for hero
    public boolean checkLandStatus(int x, int y){
        if ((x<AmountofColumn)&&(x>=0)&&(y<AmountofRow)&&(y>=0)){
            return blocks[x][y].status;
        }
        else {
            return false;
        }
    }
    //Check if move to next block is legal for monsters in normal status
    public boolean checkChannel(int x, int y){
        if ((x<AmountofColumn)&&(x>=0)&&(y<AmountofRow)&&(y>=0)){
            return blocks[x][y].channel;
        }
        else {
            return false;
        }
    }

    //Dragon set Fire in current direction and Check if hero is hurt, return True if kill happen
    public boolean Decray(int monster_direction){
        int dra_x=smaug.x;
        int dra_y=smaug.y;
        int min=0;
        int max=0;
        boolean result=false;

        switch (monster_direction){
            case 1:case 2:case 3:
            case 4:{    //Left Fire
                min=dra_x-4;
                if (min<0){min=0;}
                for (int i=dra_x-1;i>=min;i--){
                    effect[i][dra_y].setFire();
                    if ((worker.y==dra_y)&&(worker.x==i)){
                        result=true;
                    }
                }
                break;
            }
            case 5:case 6:case 7:
            case 8:{    //Right Fire
                max=dra_x+5;
                if (max>AmountofColumn){max=AmountofColumn;}
                for (int i=dra_x+1;i<max;i++){
                    effect[i][dra_y].setFire();
                    if ((worker.y==dra_y)&&(worker.x==i)) {
                        result=true;
                    }
                }
                break;
            }
        }
        return result;
    }
    //Hero use laser attack the next 3 blocks he faces
    public boolean laserGun(){
        if (worker.face_y==0){
            if (worker.face_x==1){
                for (int i=1;i<4;i++){
                    if ((worker.x+i<AmountofColumn)&&(checkChannel(worker.x+i,worker.y))){
                        if (killMonseter(worker.x+i,worker.y)){
                            return true;
                        }
                        effect[worker.x+i][worker.y].setLaser();
                    }
                    else{
                        break;
                    }
                }
            }
            else if (worker.face_x==-1){
                for (int i=1;i<4;i++){
                    if ((worker.x-i>=0)&&(checkChannel(worker.x-i,worker.y))){
                        if (killMonseter(worker.x-i,worker.y)){
                            return true;
                        }
                        effect[worker.x-i][worker.y].setLaser();
                    }
                    else{
                        break;
                    }
                }
            }
        }
        else if (worker.face_x==0){
            if (worker.face_y==1){
                for (int j=1;j<4;j++){
                    if ((worker.y+j<AmountofRow)&&(checkChannel(worker.x,worker.y+j))){
                        if (killMonseter(worker.x,worker.y+j)){
                            return true;
                        }
                        effect[worker.x][worker.y+j].setLaser();
                    }
                    else{
                        break;
                    }
                }
            }
            else if (worker.face_y==-1){
                for (int j=1;j<4;j++){
                    if ((worker.y-j<AmountofRow)&&(checkChannel(worker.x,worker.y-j))){
                        if (killMonseter(worker.x,worker.y-j)){
                            return true;
                        }
                        effect[worker.x][worker.y-j].setLaser();
                    }
                    else{
                        break;
                    }
                }
            }
        }
        return false;   //No kill
    }

    //kill a monster appear in (x,y) on Map
    public boolean killMonseter(int x, int y){
        if ((smaug.status)&&(smaug.x==x)&&(smaug.y==y)){
            smaug.status=false;
            effect[smaug.x][smaug.y].setDeath();
            return true;
        }

        for (int i=0;i<3;i++){
            if ((slime[i].status)&&(slime[i].x==x)&&(slime[i].y==y)){
                slime[i].status=false;
                effect[slime[i].x][slime[i].y].setDeath();
                return true;
            }
        }
        return false;
    }

    //Stop Displaying Fire/Laser effect
    public void resetEffect(int index){
        for (int i=0;i<AmountofColumn;i++){
            for (int j=0;j<AmountofRow;j++) {
                if (effect[i][j].getIndex()==index) {
                    effect[i][j].reSet();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int TouchX;
        int TouchY;
        int IndexX;
        int IndexY;
        int width = getWidth();
        int height = getHeight();
        int rowHeight = height / AmountofRow;
        int columnWidth = width / AmountofColumn;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {  //Code from TA, Thanks
            System.out.println("ACTION_DOWN");
            Touch_UP_Marker=false;
            TouchX = (int) event.getX();
            TouchY = (int) event.getY();
            IndexX = TouchX / rowHeight;
            IndexY = TouchY / columnWidth;
            //Get Move Direction
            if ((IndexX==7)||(IndexX==8)){
                if ((IndexY==13)||(IndexY==14)){
                    direction=0;    //up
                }
                else if ((IndexY==15)||(IndexY==16)) {
                    direction=1;    //down
                }
            } else if ((IndexY==14)||(IndexY==15)){
                if ((IndexX==5)||(IndexX==6)){
                    direction=2;    //left
                }
                else if ((IndexX==9)||(IndexX==10)) {
                    direction=3;    //right
                }
                else if ((IndexX==0)||(IndexX==1)||(IndexX==2)||(IndexX==3)) {
                    direction=4;    //z
                }
            } else if ((IndexY==13)||(IndexX==16)){
                if ((IndexX==0)||(IndexX==1)||(IndexX==2)||(IndexX==3)) {
                    direction=4;    //z
                }
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            System.out.println("ACTION_UP");
            Touch_UP_Marker=true;
            //Close App 2s after click on the screen if already GameOver
            if (GameOver){
                EndGameThread endGameThread= new EndGameThread(this);
                endGameThread.start();
                while(!Exiting){}   //Wait for a while before close app
                System.exit(0);
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);
        Rect rect = new Rect();
        int width = getWidth();
        int height = getHeight();

        int rowHeight = height / AmountofRow;
        int columnWidth = width / AmountofColumn;

        int totalAmountofMonster=4;

        //GameOver Check
        if (GameOver){
            Paint p = new Paint(Color.RED);
            p.setTextSize(100);  // Set text size
            p.setColor(Color.RED);
            p.setTextAlign(Paint.Align.CENTER);
            p.setFakeBoldText(true);
            canvas.drawText(EndMessage, width/2,height/2,p);
            return;
        }

        //Display Lower Layer Map
        for (int i=0;i<AmountofColumn;i++){
            for (int j=0;j<AmountofRow;j++) {
                rect.set(i * columnWidth, j * rowHeight, (i + 1) * columnWidth, (j + 1) * rowHeight);
                canvas.drawBitmap(icons[blocks[i][j].getIndex()], null, rect, null);
            }
        }
        //Display buttons; z
        rect.set(0 * columnWidth, 13 * rowHeight, (0 + 4) * columnWidth, (13 + 4) * rowHeight);
        canvas.drawBitmap(buttons[4], null, rect, null);
            //up
        rect.set(7 * columnWidth, 13 * rowHeight, (7 + 2) * columnWidth, (13 + 2) * rowHeight);
        canvas.drawBitmap(buttons[0], null, rect, null);
            //down
        rect.set(7 * columnWidth, 15 * rowHeight, (7 + 2) * columnWidth, (15 + 2) * rowHeight);
        canvas.drawBitmap(buttons[1], null, rect, null);
            //left
        rect.set(5 * columnWidth, 14 * rowHeight, (5 + 2) * columnWidth, (14 + 2) * rowHeight);
        canvas.drawBitmap(buttons[2], null, rect, null);
            //right
        rect.set(9 * columnWidth, 14 * rowHeight, (9 + 2) * columnWidth, (14 + 2) * rowHeight);
        canvas.drawBitmap(buttons[3], null, rect, null);
        //Display monsters
        if (smaug.status){
            rect.set(smaug.x * columnWidth, smaug.y * rowHeight, (smaug.x + 1) * columnWidth, (smaug.y + 1) * rowHeight);
            canvas.drawBitmap(icons[smaug.getIndex()], null, rect, null);
        }
        else{totalAmountofMonster--;}
        for (int i=0;i<3;i++){
            if (slime[i].status){
                rect.set(slime[i].x * columnWidth, slime[i].y * rowHeight, (slime[i].x + 1) * columnWidth, (slime[i].y + 1) * rowHeight);
                canvas.drawBitmap(icons[slime[i].getIndex()], null, rect, null);
            }
            else{totalAmountofMonster--;}
        }
        if (totalAmountofMonster<=0){
            EndMessage = "You Win!";
            GameOver=true;
        }
        //Display hero
        if (worker.status){
            rect.set(worker.x * columnWidth, worker.y * rowHeight, (worker.x + 1) * columnWidth, (worker.y + 1) * rowHeight);
            canvas.drawBitmap(icons[worker.index], null, rect, null);
        }
        else{
            EndMessage = "Game Over!";
            this.GameOver=true;
        }
        //Display Upper Layer Effect
        for (int i=0;i<AmountofColumn;i++) {
            for (int j = 0; j < AmountofRow; j++) {
                if (effect[i][j].status) {   //Display Upper Layer
                    rect.set(i * columnWidth, j * rowHeight, (i + 1) * columnWidth, (j + 1) * rowHeight);
                    canvas.drawBitmap(icons[effect[i][j].getIndex()], null, rect, null);
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ClockThread clockThread = new ClockThread(this);
        HeroThread heroThread = new HeroThread(this);
        MonsterThread monsterThread= new MonsterThread(this);
        StoneThread stoneThread=new StoneThread(this);
        //LandScape(Lower Layer)
        icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.up);
        icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.mid);
        icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.midd);
        icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.down);
        icons[9] = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        //Objects
        icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.mos);
        icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.drag);
        //Hero
        icons[8] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        icons[13] = BitmapFactory.decodeResource(getResources(), R.drawable.hero1);
        icons[14] = BitmapFactory.decodeResource(getResources(), R.drawable.hero3);
        icons[15] = BitmapFactory.decodeResource(getResources(), R.drawable.hero5);
        icons[16] = BitmapFactory.decodeResource(getResources(), R.drawable.hero7);
        //Upper Layer Effect Objects
        icons[7] = BitmapFactory.decodeResource(getResources(), R.drawable.stone);
        icons[10] = BitmapFactory.decodeResource(getResources(), R.drawable.fire);
        icons[11] = BitmapFactory.decodeResource(getResources(), R.drawable.laser);
        icons[12] = BitmapFactory.decodeResource(getResources(), R.drawable.skel);
        //Buttons
        buttons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.goup);
        buttons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.godown);
        buttons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        buttons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        buttons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.attack);
        clockThread.start();
        heroThread.start();
        monsterThread.start();
        stoneThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }
}

