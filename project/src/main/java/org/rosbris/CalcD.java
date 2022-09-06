package org.rosbris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rosbris.constants.ExternalCauses;
import org.rosbris.core.Const;
import org.rosbris.core.DataSet;
import org.rosbris.core.DataSet.DataEntry;
import org.rosbris.core.PopUtil;
import org.rosbris.core.Util;

/*
 * Распределение смерти от внешних причин по возрасту
 */
public class CalcD
{
    private static final int StartYear = 1966;

    static class AgeData implements Comparable<AgeData>
    {
        int age;
        double deaths = 0;

        @Override
        public int compareTo(AgeData o)
        {
            return this.age - o.age;
        }
    }

    private Map<Integer, AgeData> xds = new HashMap<>();

    public void eval(String sex) throws Exception
    {
        DataSet dr59 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1959-1988.txt"));
        DataSet pop59 = DataSet.load(Util.dirFile(Const.DataDir, "PopDc5a1959-1988.txt"));

        DataSet dr89 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1989-1998.txt"));
        DataSet pop89 = DataSet.load(Util.dirFile(Const.DataDir, "PopD5a1989-2014.txt"));

        for (DataEntry dr : dr59.entries())
            commonEval(dr, pop59, StartYear, 1988, sex);

        for (DataEntry dr : dr89.entries())
            commonEval(dr, pop89, 1989, 1990, sex);

        show(sex);
    }

    private void commonEval(DataEntry dr, DataSet pop, int yy1, int yy2, String sex) throws Exception
    {
        int year = dr.asInt("Year");
        if (!(year >= yy1 && year <= yy2))
            return;

        if (dr.has("Reg") && !dr.asString("Reg").equals(Const.RegionWholeRF))
            return;

        int cause = dr.asInt("Cause");
        if (!ExternalCauses.isExternalCause(cause))
            return;

        String rsex = dr.asString("Sex");
        if (!rsex.equals(sex))
            return;

        for (int ag : PopUtil.getAgeGroups())
        {
            AgeData xd = xds.get(ag);
            if (xd == null)
            {
                xd = new AgeData();
                xd.age = ag;
                xds.put(ag, xd);
            }

            double deaths = PopUtil.people(dr, pop, ag);
            xd.deaths += deaths;
        }
    }

    private void show(String sex)
    {
        List<AgeData> list = new ArrayList<>(xds.values());
        Collections.sort(list);

        Util.out("===================================================================");
        Util.out(String.format("Количество смертей от внешних причин по возрастам, %d-1990, пол=%s", StartYear, sex));
        Util.out("");
        for (AgeData xd : list)
        {
            Util.out(String.format("%d;%.0f", xd.age, xd.deaths));
        }
        Util.out("");
    }
}
