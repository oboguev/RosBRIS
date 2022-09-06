package org.rosbris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rosbris.core.Const;
import org.rosbris.core.DataSet;
import org.rosbris.core.DataSet.DataEntry;
import org.rosbris.core.PopUtil;
import org.rosbris.core.Util;

/*
 * Погодовое число умерших от алкогольных отравлений
 * и от иных отравлений
 */
public class CalcC
{
    private static final int StartYear = 1959;

    static class YearData implements Comparable<YearData>
    {
        int year;
        double alcohol_poisoning_death = 0;
        double other_poisoning_death = 0;

        @Override
        public int compareTo(YearData o)
        {
            return this.year - o.year;
        }
    }

    private Map<Integer, YearData> yds = new HashMap<>();

    public void eval() throws Exception
    {
        DataSet dr59 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1959-1988.txt"));
        DataSet pop59 = DataSet.load(Util.dirFile(Const.DataDir, "PopDc5a1959-1988.txt"));

        DataSet dr89 = DataSet.load(Util.dirFile(Const.DataDir, "DRc5a1989-1998.txt"));
        DataSet pop89 = DataSet.load(Util.dirFile(Const.DataDir, "PopD5a1989-2014.txt"));

        for (DataEntry dr : dr59.entries())
            commonEval(dr, pop59, StartYear, 1988);

        for (DataEntry dr : dr89.entries())
            commonEval(dr, pop89, 1989, 1990);

        show();
    }

    private void commonEval(DataEntry dr, DataSet pop, int yy1, int yy2) throws Exception
    {
        int year = dr.asInt("Year");
        if (!(year >= yy1 && year <= yy2))
            return;

        if (dr.has("Reg") && !dr.asString("Reg").equals(Const.RegionWholeRF))
            return;

        int cause = dr.asInt("Cause");
        if (cause != 163 && cause != 164)
            return;

        YearData yd = yds.get(year);
        if (yd == null)
        {
            yd = new YearData();
            yd.year = year;
            yds.put(year, yd);
        }

        double deaths = PopUtil.people(dr, pop);

        if (cause == 163)
        {
            yd.alcohol_poisoning_death += deaths;
        }
        else if (cause == 164)
        {
            yd.other_poisoning_death += deaths;

        }
    }

    private void show()
    {
        List<YearData> list = new ArrayList<>(yds.values());
        Collections.sort(list);

        Util.out("===================================================================");
        Util.out(String.format("Годовое количество смертей от отравлений алкоголем и от других случайных отравлений, %d-1990, оба пола", StartYear));
        Util.out("");
        for (YearData yd : list)
        {
            Util.out(String.format("%d;%.0f;%.0f", yd.year, yd.alcohol_poisoning_death, yd.other_poisoning_death));
        }
        Util.out("");
    }
}
