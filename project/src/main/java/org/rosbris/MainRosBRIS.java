package org.rosbris;

public class MainRosBRIS
{
    public static void main(String[] args)
    {
        try
        {
            new CalcA().eval();
            Util.out("=== Completed === ");
        }
        catch (Exception ex)
        {
            System.out.flush();
            System.err.println("*** Exception: ");
            ex.printStackTrace();
        }
    }
}
