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
 * Погодовое число смертей от внешних причин за 1959-1990 гг., 
 * как доля в общей смертности
 */
public class CalcB
{
    // private static final int StartYear = 1966;
    private static final int StartYear = 1959;

    static class YearData implements Comparable
    {
        int year;
        double total_male = 0;
        double total_female = 0;
        double external_male = 0;
        double external_female = 0;

        @Override
        public int compareTo(Object o)
        {
            return this.year - ((YearData) o).year;
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

        YearData yd = yds.get(year);
        if (yd == null)
        {
            yd = new YearData();
            yd.year = year;
            yds.put(year, yd);
        }

        String sex = dr.asString("Sex");

        double deaths = PopUtil.people(dr, pop);

        if (sex.equals("M"))
            yd.total_male += deaths;
        else
            yd.total_female += deaths;

        int cause = dr.asInt("Cause");
        if (ExternalCauses.isExternalCause(cause))
        {
            if (sex.equals("M"))
                yd.external_male += deaths;
            else
                yd.external_female += deaths;

        }
    }

    private void show()
    {
        List<YearData> list = new ArrayList<>(yds.values());
        Collections.sort(list);

        Util.out("===================================================================");
        Util.out(String.format("Годовая доля смертей от внешних причин, %d-1990, оба пола", StartYear));
        Util.out("");
        for (YearData yd : list)
        {
            double pct = 100 * (yd.external_male + yd.external_female) / (yd.total_male + yd.total_female);
            Util.out(String.format("%d;%f", yd.year, pct));
        }
        Util.out("");

        Util.out("===================================================================");
        Util.out(String.format("Годовая доля смертей от внешних причин, %d-1990, мужчины", StartYear));
        Util.out("");
        for (YearData yd : list)
        {
            double pct = 100 * (yd.external_male) / (yd.total_male);
            Util.out(String.format("%d;%f", yd.year, pct));
        }
        Util.out("");

        Util.out("===================================================================");
        Util.out(String.format("Годовая доля смертей от внешних причин, %d-1990, женщины", StartYear));
        Util.out("");
        for (YearData yd : list)
        {
            double pct = 100 * (yd.external_female) / (yd.total_female);
            Util.out(String.format("%d;%f", yd.year, pct));
        }
        Util.out("");
    }
}
