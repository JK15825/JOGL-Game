import com.jogamp.opengl.*;
import java.util.*;
public class Deca implements Poly
{
    private int x;
    private int y;
    private int z;
    ArrayList<double[]> points;
    ArrayList<int[]> faces;
    ArrayList<double[]> colors;
    ArrayList<int[]> faceSet;
    public Deca(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = y;
        loadPoints();
        loadFaces();
        loadColors();
    }
    private void loadPoints()
    {
        points = new ArrayList<>();
        double side = 1;
        ArrayList<double[]> p = new ArrayList<>();
        double gR = (1 + Math.sqrt(5))/2;
        double h = side * ((Math.pow(gR,3)) / (2 * Math.sqrt(Math.pow(gR,2) + 1)));
        
        double longSide = 1.1 * (side * Math.pow(gR,2)/Math.sqrt(Math.pow(gR,2 + 1)));
        
        double fromCenter = ((1/Math.sqrt(Math.pow(gR,2) + 1)) * side)/2;
        
        double x;
        double y;
        double angle = 72;
        int farEdge = 5;
        for (int i = 0; i < 5; i++)
        {
            x = (side * .86) * Math.sin(Math.toRadians(i * angle));
            y = (side * .86) * Math.cos(Math.toRadians(i * angle));
            p.add(new double[]{x,y,h});

        }
        for (int i = 0; i < 5; i++)
        {
            x = longSide * Math.sin(Math.toRadians(i * angle));
            y = longSide * Math.cos(Math.toRadians(i * angle));
            p.add(new double[]{x,y,fromCenter});

        }
        for (int i = 0; i < 5; i++)
        {
            x = longSide * Math.sin(Math.toRadians((i * angle) + 180));
            y = longSide * Math.cos(Math.toRadians((i * angle) + 180));
            p.add(new double[]{x,y,-fromCenter});

        }
        for (int i = 0; i < 5; i++)
        {
            x = (side * .86) * Math.sin(Math.toRadians((i * angle) + 180));
            y = (side * .86) * Math.cos(Math.toRadians((i * angle) + 180));
            p.add(new double[]{x,y,-h});

        } 
        p.add(new double[]{0,0,h});
        p.add(new double[]{0,0,-h});
        p.add(new double[]{0,0,fromCenter});
        p.add(new double[]{0,0,-fromCenter});
        points = p;
    }
    private void loadFaces()
    {
        faces = new ArrayList<>();
        
        for(int k = 0; k < 5;k++)
        {   
            if(k + 1 == 5)
                faces.add(new int[]{k,20,0});
            else
                faces.add(new int[]{k,20,k + 1});
        }
        for(int k = 5; k < 10;k++)
        {   
            if(k + 1 == 10)
                faces.add(new int[]{k,22,5});
            else
                faces.add(new int[]{k,22,k + 1});
        }
        for(int k = 10; k < 15;k++)
        {
            if(k + 1 == 15)
                faces.add(new int[]{k,23,10});
            else
                faces.add(new int[]{k,23,k + 1});
        }
        for(int k = 15; k < 20;k++)
        {
            if(k + 1 == 20)
                faces.add(new int[]{k,21,15});
            else
                faces.add(new int[]{k,21,k + 1});
        }
        /*bottom row of faces*/
        faces.add(new int[]{0,1,5});
        faces.add(new int[]{5,6,1});
        
        faces.add(new int[]{1,2,6});
        faces.add(new int[]{6,7,2});
        
        faces.add(new int[]{2,3,7});
        faces.add(new int[]{7,8,3});
        
        faces.add(new int[]{3,4,8});
        faces.add(new int[]{8,9,4});
        
        faces.add(new int[]{4,0,9});
        faces.add(new int[]{9,5,0});/*
        
        /*bottom middle row of faces*/
        faces.add(new int[]{5,6,13});
        faces.add(new int[]{6,7,14});
        faces.add(new int[]{7,8,10});
        faces.add(new int[]{8,9,11});
        faces.add(new int[]{9,5,12});/*34*/
        
        /*top middle row of faces*/
        faces.add(new int[]{10,11,8});
        faces.add(new int[]{11,12,9});
        faces.add(new int[]{12,13,5});
        faces.add(new int[]{13,14,6});
        faces.add(new int[]{14,10,7});/*39*/
        
        /*top row of faces*/
        faces.add(new int[]{15,16,10});
        faces.add(new int[]{10,11,16});
        
        faces.add(new int[]{16,17,11});
        faces.add(new int[]{11,12,17});
        
        faces.add(new int[]{17,18,12});
        faces.add(new int[]{12,13,18});
        
        faces.add(new int[]{18,19,13});
        faces.add(new int[]{13,14,19});
        
        faces.add(new int[]{19,15,14});
        faces.add(new int[]{14,10,15});/*49*/
        
        /*group of faces*/
        faceSet = new ArrayList<int[]>();
        /*top and bottom*/
        faceSet.add(new int[]{0,1,2,3,4});
        faceSet.add(new int[]{15,16,17,18,19});
        /*bottom faces*/
        faceSet.add(new int[]{20,21,30});
        faceSet.add(new int[]{22,23,31});
        faceSet.add(new int[]{24,25,32});
        faceSet.add(new int[]{26,27,33});
        faceSet.add(new int[]{28,29,34});
        /*top faces*/
        faceSet.add(new int[]{40,41,35});
        faceSet.add(new int[]{42,43,36});
        faceSet.add(new int[]{44,45,37});
        faceSet.add(new int[]{46,47,38});
        faceSet.add(new int[]{48,49,39});
        
        
    }
    private void loadColors()
    {
        colors = new ArrayList<>();
        for(int k = 0; k < faces.size(); k++)
        {
            colors.add(new double[]{Math.random(), Math.random(), Math.random()});
        }
    }
    public float getX(){return (float)x;}
    public float getY(){return (float)x;}
    public float getZ(){return (float)x;}
    public float getSizeX()
    {
        return 0.0f;
    }
    public float getSizeY()
    {
        return 0.0f;
    }
    public float getSizeZ()
    {
        return 0.0f;
    }
    public void draw(GL2 gl)
    {
        gl.glPushMatrix();
        gl.glRotatef(x,0,0,0);
        gl.glTranslatef(this.x,this.y,z);        
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(4,0,0);
        int otherCount = 0;
        int cur = 0;
        for(int[] faceList : faceSet)
        {
            gl.glColor3d(colors.get(cur)[0],colors.get(cur)[1],colors.get(cur)[2]);
            for(int curFace : faceList)
            {
                int[] verts = faces.get(curFace);
                for(int point : verts)
                {
                    gl.glVertex3d(points.get(point)[0],points.get(point)[1],points.get(point)[2]);
                }
            }
            cur++;
        }

        gl.glEnd();
        gl.glPopMatrix();
    }
}
