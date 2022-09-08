package org.rosbris;

import org.rosbris.constants.SettlementType;
import org.rosbris.core.Const;
import org.rosbris.core.DataSet;
import org.rosbris.core.Util;

public class DataSetLoader
{
    public static DataSet load_DRc5a_1959_1998_total() throws Exception
    {
        DataSet dr59 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1959-1988.txt"));
        DataSet dr89 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1989-1998.txt")).selectEq("Group", SettlementType.TOTAL.code());
        return new DataSet().append(dr59).append(dr89);
    }

    public static DataSet load_Pop_1959_2014_total() throws Exception
    {
        DataSet pop59 = DataSet.load(Util.dirFile(Const.DataDir, "PopDc5a1959-1988.txt"));
        DataSet pop89 = DataSet.load(Util.dirFile(Const.DataDir, "PopD5a1989-2014.txt")).selectEq("Group", SettlementType.TOTAL.code());
        return new DataSet().append(pop59).append(pop89);
    }
}
