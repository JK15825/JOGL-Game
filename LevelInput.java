import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelInput
{
    public static ArrayList<Cube> loadLevel(int t, int t2, int t3)
    {
        Scanner in = null;
        try
        {
            in = new Scanner(new File("map.dat"));
        }catch(Exception e){}

        int amount = in.nextInt();
        ArrayList<Cube> toSend = new ArrayList<>();

        for(int k = 0; k < amount; k++)
        {
            int cur = in.nextInt();
            if(cur == 0)
                toSend.add(new Cube(in.nextFloat(),in.nextFloat(),in.nextFloat(),t));
            else if(cur == 1)
                toSend.add(new Cube(in.nextFloat(),in.nextFloat(),in.nextFloat(),t2));
            else
                toSend.add(new Cube(in.nextFloat(),in.nextFloat(),in.nextFloat(),t3));
        }
        System.out.println(toSend);
        return toSend;
    }
}
