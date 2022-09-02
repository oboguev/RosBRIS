package org.rosbris;

import org.rosbris.core.Util;

public class MainRosBRIS
{
    public static void main(String[] args)
    {
        try
        {
            // new CalcA().eval();
            new CalcB().eval();
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
