import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;

import java.util.*;


public class Game
{
    private boolean r = false;
    private boolean l = false;
    private boolean f = false;
    private boolean b = false;
    private boolean u = false;
    private boolean d = false;

    private float rX;
    private float rY;

    private ArrayList<Cube> c;
   
    private Player player;

    private int blue;
    private int red;
    private int purple;
    public Game()
    {
        player= new Player(0f,8f,0f);
        rX = 0;
        rY = 0;
        c = new ArrayList<>();
    }
    public void loadTextures(int t, int t2, int t3)
    {
        blue = t;
        red = t2;
        purple = t3;

        c = LevelInput.loadLevel(t,t2,t3);
    }
    public void tick()
    {
        player.setUpXY(rX,rY);
        player.moveCamera(f,b,l,r,u,d,c);
    }
    public void draw(GL2 gl,GLU glu)
    {
        player.aimCamera(gl,glu);
        player.moveCamera();
        
        for(Cube p :c)
        {
            p.draw(gl,player.x(), player.y(),player.z());
        }

    }
    public void setRotation(float x, float y)
    {
        rX = x;
        rY = y;
    }
    public void setFord(boolean t)
    {
        f = t;
    }
    public void setBack(boolean t)
    {
        b = t;
    }
    public void setStrafel(boolean t)
    {
        l = t;
    }
    public void setStrafer(boolean t)
    {
        r = t;
    }
    public void setUp(boolean t)
    {
        u = t;
    }
    public void setDown(boolean t)
    {
        d = t;
    }
    public void forceAdjust()
    {
        player.forceAdjust(-4f,4f,-4f);
    }
}
