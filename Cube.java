import java.util.*;
import com.jogamp.opengl.*;



public class Cube //implements Poly
{
    ArrayList<double[]> points;
    ArrayList<int[]> faces;
    ArrayList<double[]> colors;
    ArrayList<int[]> faceSet;
    float x;
    float y;
    float z;
    int texture;

    float rX;
    float rY;


    public Cube(float x, float y, float z, int texture)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        rX = 0;
        rY = 0;

        this.texture = texture;
        loadPoints();
        loadFaces();
        loadColors();


    }
    public Cube(float x, float y, float z, float size)
    {
         this.x = x;
        this.y = y;
        this.z = z;
        rX = 0;
        rY = 0;
        loadPoints(size);
        loadFaces();
        loadColors();
        
    }
    public Cube(float x, float y, float z, float length, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        rX = 0;
        rY = 0;
        loadPoints(length, width, height);
        loadFaces();
        loadColors();
        
    }
    private void loadPoints()
    {
        points = new ArrayList<>();
        points.add(new double[]{-1,1,1});
        points.add(new double[]{1,1,1});
        points.add(new double[]{1,1,-1});
        points.add(new double[]{-1,1,-1});
        
        points.add(new double[]{-1,-1,1});
        points.add(new double[]{1,-1,1});
        points.add(new double[]{1,-1,-1});
        points.add(new double[]{-1,-1,-1});
    }
    private void loadPoints(double s)
    {
        points = new ArrayList<>();
        points.add(new double[]{-s,s,s});
        points.add(new double[]{s,s,s});
        points.add(new double[]{s,s,-s});
        points.add(new double[]{-s,s,-s});
        
        points.add(new double[]{-s,-s,s});
        points.add(new double[]{s,-s,s});
        points.add(new double[]{s,-s,-s});
        points.add(new double[]{-s,-s,-s});
    }
    private void loadPoints(double l, double w, double h)
    {
        points = new ArrayList<>();
        points.add(new double[]{-l,h,w});
        points.add(new double[]{l,h,w});
        points.add(new double[]{l,h,-w});
        points.add(new double[]{-l,h,-w});
        
        points.add(new double[]{-l,-h,w});
        points.add(new double[]{l,-h,w});
        points.add(new double[]{l,-h,-w});
        points.add(new double[]{-l,-h,-w});
    }
    private void loadFaces()
    {
        faces = new ArrayList<>();
        faces.add(new int[]{4,0,5});
        faces.add(new int[]{5,0,1});
        
        faces.add(new int[]{5,1,6});
        faces.add(new int[]{6,1,2});
        
        faces.add(new int[]{6,2,7});
        faces.add(new int[]{7,2,3});
        
        faces.add(new int[]{7,3,4});
        faces.add(new int[]{4,3,0});
        
        faces.add(new int[]{0,1,2});
        faces.add(new int[]{0,2,3});
        
        faces.add(new int[]{4,5,6});
        faces.add(new int[]{4,6,7});
        
        faceSet = new ArrayList<>();
        faceSet.add(new int[]{0,1});
        faceSet.add(new int[]{2,3});
        faceSet.add(new int[]{4,5});
        faceSet.add(new int[]{6,7});
        faceSet.add(new int[]{8,9});
        faceSet.add(new int[]{10,11});
    }
    private void loadColors()
    {
        colors = new ArrayList<>();
        for(int k = 0; k < faces.size(); k++)
        {
            colors.add(new double[]{Math.random(), Math.random(), Math.random()});
        }
    }
    public float getX(){return x;}
    public float getY(){return y;}
    public float getZ(){return z;}
    public float getSizeX()
    {
        float x = (float)points.get(1)[0] - (float)points.get(0)[0];
        float y = (float)points.get(1)[1] - (float)points.get(0)[1];
        float z = (float)points.get(1)[2] - (float)points.get(0)[2];
        return (float)(Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2)))/2;
    }
    public float getSizeY()
    {
        float x = (float)points.get(4)[0] - (float)points.get(0)[0];
        float y = (float)points.get(4)[1] - (float)points.get(0)[1];
        float z = (float)points.get(4)[2] - (float)points.get(0)[2];
        return (float)(Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2)))/2;
    }
    public void adjXYZ(float x, float y, float z)
    {
        this.x+=x;
        this.y+=y;
        this.z+=z;
    }
    public float getSizeZ()
    {
        float x = (float)points.get(3)[0] - (float)points.get(0)[0];
        float y = (float)points.get(3)[1] - (float)points.get(0)[1];
        float z = (float)points.get(3)[2] - (float)points.get(0)[2];
        return (float)(Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2)))/2;
    }
    public void draw(GL2 gl)
    {
        gl.glPushMatrix();
        gl.glBegin(GL.GL_TRIANGLES);
        //gl.glColor3f(4,0,0);
        for(int[] faceList : faceSet)
        {
            for(int curFace : faceList)
            {
                int[] verts = faces.get(curFace);
                gl.glTexCoord2d(0f,0f);
                gl.glVertex3d(points.get(verts[0])[0],points.get(verts[0])[1],points.get(verts[0])[2]);                
                gl.glTexCoord2d(1f,0f);
                gl.glVertex3d(points.get(verts[1])[0],points.get(verts[1])[1],points.get(verts[1])[2]);
                gl.glTexCoord2d(0f,1f);
                gl.glVertex3d(points.get(verts[2])[0],points.get(verts[2])[1],points.get(verts[2])[2]);
            }
        }
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glPopMatrix();
    }
        
    public void draw(GL2 gl, float cX, float cY, float cZ)
    {
        if(Math.abs(cX - x) <= 60 && Math.abs(cZ - z) <= 60 && Math.abs(cY - y) <= 16)
        {
            
            
            gl.glPushMatrix();
            gl.glTranslatef(x,y,z);
            gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);

            gl.glColor3f(1f,1f,1f);

            gl.glBegin(GL.GL_TRIANGLES);
            for(int[] faceList : faceSet)
            {
                for(int curFace : faceList)
                {
                    int[] verts = faces.get(curFace);
                    gl.glTexCoord2d(0f,0f);
                    gl.glVertex3d(points.get(verts[0])[0],points.get(verts[0])[1],points.get(verts[0])[2]);
                    gl.glTexCoord2d(1f,0f);
                    gl.glVertex3d(points.get(verts[1])[0],points.get(verts[1])[1],points.get(verts[1])[2]);
                    gl.glTexCoord2d(0f,1f);
                    gl.glVertex3d(points.get(verts[2])[0],points.get(verts[2])[1],points.get(verts[2])[2]);
                }
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();

            gl.glPopMatrix();
        }
        
    }
    public String toString()
    {
        return "X: " + x + " Y: "+ y + " Z: " + z;
    }
}
