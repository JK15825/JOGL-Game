import com.jogamp.opengl.*;
import com.jogamp.opengl.fixedfunc.*;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.nio.FloatBuffer;
import java.awt.Point;
import java.awt.Robot;
import java.io.*;
import com.jogamp.opengl.util.texture.*;


public class Screen implements GLEventListener
{
    private GLU glu;
    private final int width = 1024;
    private final int height = 768;
    Game game;
    private Robot rob = null;
    private long lastTime;
    JFrame frame;
    private int texture;
    private int texture2;
    private int texture3;
    public Screen()
    {
        frame = new JFrame("Screen");
        lastTime = System.currentTimeMillis();

        try
        {
            rob = new Robot();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities cap = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(cap);
        canvas.addGLEventListener(this);
        canvas.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseMoved(MouseEvent e)
            {
                if(e.getX() > frame.getWidth() * 2/3)
                {
                    rob.mouseMove(frame.getLocation().x + frame.getWidth()/2, frame.getLocation().y + frame.getHeight()/2);                    
                }
                if(System.currentTimeMillis() - 50 > lastTime)
                {
                    lastTime = System.currentTimeMillis();
                    
                    float horzMovement = e.getX() - frame.getWidth()/2;
                    float vertMovement = e.getY() - frame.getHeight()/2;
                    float mSense = 50.0f;
                    
                    game.setRotation(vertMovement/mSense, horzMovement/mSense);
                    rob.mouseMove(frame.getLocation().x + frame.getWidth()/2, frame.getLocation().y + frame.getHeight()/2);
                }
            }
        });
       
        canvas.setFocusable(true);
        FPSAnimator animate = new FPSAnimator(canvas,30);
        
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.add(canvas);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        bind();
        
        frame.setFocusable(true);
        frame.setVisible(true);
        frame.setCursor(frame.getToolkit().createCustomCursor(
            new ImageIcon("cross.png").getImage(), new Point(0, 0),
            "img"));
            
        animate.start();
        
        rob.mouseMove(frame.getLocation().x + frame.getWidth()/2, frame.getLocation().y + frame.getHeight()/2);
        rob.mousePress(InputEvent.BUTTON1_MASK);
        rob.delay(1000);
        rob.mouseMove(frame.getLocation().x - frame.getWidth()/2, frame.getLocation().y + frame.getHeight()/2);        
        rob.mousePress(InputEvent.BUTTON1_MASK);
        rob.mouseMove(0,frame.getLocation().y + frame.getHeight()/2);
        rob.mousePress(InputEvent.BUTTON1_MASK);
        rob.mouseMove(frame.getLocation().x + frame.getWidth()/2, frame.getLocation().y + frame.getHeight()/2);
        rob.mousePress(InputEvent.BUTTON1_MASK);
    }
    public static void main(String args[])
    {
        new Screen();
    }
    public void init(GLAutoDrawable drawable)
    {
        game = new Game();
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glClearDepth(1f);
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        //gl.glEnable(GL.GL_CULL_FACE);

        float[] specular = new float[]{.1f,.1f,.1f,1f};
        float[] shininess = new float[]{50f};
        float[] lightpos = new float[]{0f,0f,0f,4f};
        FloatBuffer s2 = FloatBuffer.wrap(specular);
        FloatBuffer sh2 = FloatBuffer.wrap(shininess);
        FloatBuffer l2 = FloatBuffer.wrap(lightpos);
        
        gl.glMaterialfv(GL.GL_BACK, GL2.GL_SPOT_DIRECTION,s2);
        gl.glMaterialfv(GL.GL_BACK, GL2.GL_SHININESS, sh2);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, l2);
        
        //gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glShadeModel(GL2.GL_SMOOTH);
        
        gl.glColorMaterial(GL2.GL_FRONT,GL2.GL_DIFFUSE);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        try
        {
            File in = new File("white.png");
            Texture t = TextureIO.newTexture(in,true);
            texture = t.getTextureObject(gl);

            in = new File("black.png");
            t = TextureIO.newTexture(in,true);
            texture2 = t.getTextureObject(gl);

            in = new File("gray.png");
            t = TextureIO.newTexture(in,true);
            texture3 = t.getTextureObject(gl);

        }catch(Exception e){}

        game.loadTextures(texture,texture2,texture3);

    }
    
    public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4)
    {
        GL2 gl = drawable.getGL().getGL2();
        final float aspect = (float) width / (float) height;
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        final float fh = 0.5f;
        final float fw = fh * aspect;
        gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 1000.0f);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    private void render(GLAutoDrawable drawable)
    {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        game.draw(gl,glu);
        
        gl.glFlush();
    }
    public void display(GLAutoDrawable drawable)
    {
        game.tick();
        render(drawable);
    }
    public void dispose(GLAutoDrawable drawable)
    {
        
    }
    private void bind()
    {
        
        JRootPane root = frame.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"exit");
        root.getActionMap().put("exit", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.dispose();
                System.exit(0);
            }
        });
        
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed A"),"right");
        root.getActionMap().put("right", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setStrafel(true);
            }
        });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"),"rright");
        root.getActionMap().put("rright", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setStrafel(false);
            }
        });
        
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed D"),"left");
        root.getActionMap().put("left", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setStrafer(true);
            }
        });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"),"rleft");
        root.getActionMap().put("rleft", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setStrafer(false);
            }
        });
        
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed W"),"fow");
        root.getActionMap().put("fow", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setFord(true);
            }
        });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"),"rfow");
        root.getActionMap().put("rfow", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setFord(false);
            }
        });
        
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed S"),"back");
        root.getActionMap().put("back", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setBack(true);
            }
        });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"),"rback");
        root.getActionMap().put("rback", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setBack(false);
            }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed R"),"up");
        root.getActionMap().put("up", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setUp(true);
            }
        });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released R"),"rup");
        root.getActionMap().put("rup", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setUp(false);
            }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed F"),"down");
        root.getActionMap().put("down", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setDown(true);
            }
        });
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released F"),"rdown");
        root.getActionMap().put("rdown", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.setDown(false);
            }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed 0"),"shift");
        root.getActionMap().put("shift", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.forceAdjust();
            }
        });
    }   
}
