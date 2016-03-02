import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;
import java.util.ArrayList;
import java.nio.FloatBuffer;
public class Player
{
    float cameraAzimuth = .5f, cameraSpeed = 0.0f, cameraElevation = 0.0f;
    float cameraCoordsPosx = 0.0f, cameraCoordsPosy = 0.0f, cameraCoordsPosz = -20.0f;
    float cameraUpx = 0.0f, cameraUpy = 1.0f, cameraUpz = 0.0f;
    float rX, rY;
    Cube temp;
    Cube temp2;
    public Player(float x, float y, float z)
    {
        cameraCoordsPosx = x;
        cameraCoordsPosy = y;
        cameraCoordsPosz = z;

        temp = new Cube(x - .25f,y - 4,z,.5f,.25f,.25f);
        temp2 = new Cube(x + .25f,y - 4,z,.5f,.25f,.25f);
        //temp = new Cube(x,y-4f,z);
        //temp2 = new Cube(x,y-4,z);
    }
    public void forceAdjust(float x, float y, float z)
    {
        cameraCoordsPosx = x;
        cameraCoordsPosy = y;
        cameraCoordsPosz = z;
    }
    public void moveCamera()
    {
        float[] tmp = polarToCartesian(cameraAzimuth, cameraSpeed, cameraElevation);
    
        cameraCoordsPosx += tmp[0];
        cameraCoordsPosy += tmp[1];
        cameraCoordsPosz += tmp[2];
    }

    public void aimCamera(GL2 gl, GLU glu)
    {
        gl.glLoadIdentity();
        float[] tmp = polarToCartesian(cameraAzimuth, 10.0f, cameraElevation);
        float[] camUp = polarToCartesian(cameraAzimuth, 100.0f, cameraElevation + 90);
    
        cameraUpx = camUp[0];
        cameraUpy = camUp[1];
        cameraUpz = camUp[2];
        
        gl.glRotatef(rX,1,0,0);
        gl.glRotatef(rY,0,1,0);
        
        glu.gluLookAt(cameraCoordsPosx, cameraCoordsPosy, cameraCoordsPosz,
            cameraCoordsPosx + tmp[0], cameraCoordsPosy + tmp[1],
            cameraCoordsPosz + tmp[2], cameraUpx, cameraUpy, cameraUpz);

        float[] lightpos = new float[]{cameraCoordsPosx,cameraCoordsPosy,cameraCoordsPosz,1f};
        FloatBuffer l2 = FloatBuffer.wrap(lightpos);

        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_POSITION,l2);

        temp.draw(gl);
        temp2.draw(gl);
    }

    private float[] polarToCartesian (float azimuth, float length, float altitude)
    {
        float[] result = new float[3];
        float x, y, z;    
    
        float theta = (float)Math.toRadians(90 - azimuth);
        float tantheta = (float) Math.tan(theta);
        float radian_alt = (float)Math.toRadians(altitude);
        float cospsi = (float) Math.cos(radian_alt);
    
        x = (float) Math.sqrt((length * length) / (tantheta * tantheta + 1));
        z = tantheta * x;
        x = -x;
        if ((azimuth >= 180.0 && azimuth <= 360.0) || azimuth == 0.0f) 
        {
            x = -x;
            z = -z;
        }
        y = (float) (Math.sqrt(z * z + x * x) * Math.sin(radian_alt));
        if (length < 0) 
        {
            x = -x;
            z = -z;
            y = -y;
        }
        x = x * cospsi;
        z = z * cospsi;
        result[0] = x;
        result[1] = y;
        result[2] = z;        
        return result;
    }
    public void moveCamera(boolean f,boolean b,boolean l,boolean r, boolean u, boolean d, ArrayList<Cube> cubes)
    {
        float cmxc = 0f;
        float cmyc = 0f;
        float cmzc = 0f;
        if(f)
        {
            float pitchFactor = (float)Math.cos(Math.toRadians(rX));
            cmxc += ( .5 * (float)Math.sin(Math.toRadians(rY)) * -1.0f ) * pitchFactor;
            
            float yawFactor = (float)Math.cos(Math.toRadians(rX));
            cmzc += ( .5 * (float)Math.cos(Math.toRadians(rY))) * yawFactor;
        }
        if(b)
        {
            float pitchFactor = (float) Math.cos(Math.toRadians(rX));
            cmxc += (.5 * (float) Math.sin(Math.toRadians(rY))) * pitchFactor;

            float yawFactor = (float) Math.cos(Math.toRadians(rX));
            cmzc += (.5 * (float) Math.cos(Math.toRadians(rY)) * -1.0f) * yawFactor;
        }

        if(r)
        {
            float yRot = (float)Math.toRadians(rY);
            
            cmxc += -.5 * (float)Math.cos(yRot);
            cmzc += -.5 * (float)Math.sin(yRot);
        }
        if(l)
        {
            float yRot = (float)Math.toRadians(rY);
            
            cmxc += .5 * (float)Math.cos(yRot);
            cmzc += .5 * (float)Math.sin(yRot);
        }

        if(u)
        {
            cmyc +=.5f;
        }
        if(d)
        {
            cmyc -=.5f;
        }
        cameraCoordsPosx += cmxc;
        cameraCoordsPosy += cmyc;
        cameraCoordsPosz += cmzc;

        temp.adjXYZ(cmxc,cmyc,cmzc);
        temp2.adjXYZ(cmxc,cmyc,cmzc);

        if(collision(cubes,temp,temp2))
        {
            cameraCoordsPosx -= cmxc;
            cameraCoordsPosy -= cmyc;
            cameraCoordsPosz -= cmzc;

            temp.adjXYZ(-cmxc,-cmyc,-cmzc);
            temp2.adjXYZ(-cmxc,-cmyc,-cmzc);
        }
    }
    public boolean collision(ArrayList<Cube> cube, Cube temp, Cube temp2)
    {
        for(Cube c : cube)
        {
            if(Math.abs(temp.getX() - c.getX()) < temp.getSizeX() + c.getSizeX())
            {
                if(Math.abs(temp.getY() - c.getY()) < temp.getSizeY() + c.getSizeY())
                {
                    if(Math.abs(temp.getZ() - c.getZ()) < temp.getSizeZ() + c.getSizeZ())
                    {
                        return true;
                    }
                }
            }
            if(Math.abs(temp2.getX() - c.getX()) < temp2.getSizeX() + c.getSizeX())
            {
                if(Math.abs(temp2.getY() - c.getY()) < temp2.getSizeY() + c.getSizeY())
                {
                    if(Math.abs(temp2.getZ() - c.getZ()) < temp2.getSizeZ() + c.getSizeZ())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public float x()
    {
        return cameraCoordsPosx;
    }
    public float y()
    {
        return cameraCoordsPosy;
    }
    public float z()
    {
        return cameraCoordsPosz;
    }
    public void setUpXY(float x, float y)
    {
        rX+=x;
        rY+=y;
        if(rX < -70.0f)
            rX = -70.0f;
        if(rX > 70.0f)
            rX = 70.f;
        if(rY < -180.0f)
            rY += 360.0f;
        if(rY > 180.0f)
            rY -= 360.0f;
    }
}
