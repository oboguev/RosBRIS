package org.rosbris;

import org.rosbris.DataSet.DataEntry;

public class CalcA
{
    public void eval() throws Exception
    {
        DataSet dr59 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1959-1988.txt"));
        DataSet pop59 = DataSet.load(Util.dirFile(Const.DataDir, "PopDc5a1959-1988.txt"));

        DataSet dr89 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1989-1998.txt"));
        DataSet pop89 = DataSet.load(Util.dirFile(Const.DataDir, "PopD5a1989-2014.txt"));

        for (DataEntry dr : dr59.entries())
        {
            int year = dr.asInt("Year");
            if (!(year >= 1966 && year <= 1988))
                continue;

            PopUtil.people(dr, pop59);
        }

        for (DataEntry dr : dr89.entries())
        {
            int year = dr.asInt("Year");
            if (!(year >= 1989 && year <= 1990))
                continue;

            if (!dr.asString("Reg").equals(Const.RegionWholeRF))
                continue;

            PopUtil.people(dr, pop89);
        }

    }
}
