package org.rosbris;

import java.util.HashMap;
import java.util.Map;

import org.rosbris.constants.ExternalCauses;
import org.rosbris.core.Const;
import org.rosbris.core.DataSet;
import org.rosbris.core.DataSet.DataEntry;
import org.rosbris.core.PopUtil;
import org.rosbris.core.Util;

/*
 * Число смертей от внешних причин за 1966-1990 гг., 
 * с разбивкой по причине смерти
 */
public class CalcA
{
    double totalDeaths = 0;
    double totalDeathsMale = 0;
    double totalDeathsFemale = 0;
    Map<Integer, Double> deathsByCauseTotal = new HashMap<>();
    Map<Integer, Double> deathsByCauseMale = new HashMap<>();
    Map<Integer, Double> deathsByCauseFemale = new HashMap<>();

    public void eval() throws Exception
    {
        DataSet dr = DataSetLoader.load_DRc5a_1959_1998_total();
        DataSet pop = DataSetLoader.load_Pop_1959_2014_total();

        for (DataEntry de : dr.entries())
            commonEval(de, pop, 1966, 1990);

        show();
    }

    private void commonEval(DataEntry dr, DataSet pop, int yy1, int yy2) throws Exception
    {
        int year = dr.asInt("Year");
        if (!(year >= yy1 && year <= yy2))
            return;

        if (dr.has("Reg") && !dr.asString("Reg").equals(Const.RegionWholeRF))
            return;

        String sex = dr.asString("Sex");

        double deaths = PopUtil.people(dr, pop);
        totalDeaths += deaths;
        if (sex.equals("M"))
            totalDeathsMale += deaths;
        else
            totalDeathsFemale += deaths;

        int cause = dr.asInt("Cause");
        if (ExternalCauses.isExternalCause(cause))
        {
            cause = ExternalCauses.map(cause);

            addByKey(deathsByCauseTotal, cause, deaths);
            if (sex.equals("M"))
                addByKey(deathsByCauseMale, cause, deaths);
            else
                addByKey(deathsByCauseFemale, cause, deaths);
        }
    }

    private <T> void addByKey(Map<T, Double> m, T key, double value)
    {
        Double old = m.get(key);
        if (old == null)
            old = 0.0;
        m.put(key, old + value);
    }

    private void show()
    {

        Util.out("===================================================================");
        Util.out(String.format("Total deaths (mil): %.3f", totalDeaths / 1_000_000));
        Util.out(String.format("Total deaths: %.0f", totalDeaths));
        Util.out("");
        Util.out(String.format("Total male deaths (mil): %.3f", totalDeathsMale / 1_000_000));
        Util.out(String.format("Total male deaths: %.0f", totalDeathsMale));
        Util.out("");
        Util.out(String.format("Total female deaths (mil): %.3f", totalDeathsFemale / 1_000_000));
        Util.out(String.format("Total female deaths: %.0f", totalDeathsFemale));
        Util.out("");

        show("Смерти обоих полов от внешних причин за 1966-1990, разбивка по причине", deathsByCauseTotal);
        show("Смерти мужчин от внешних причин за 1966-1990, разбивка по причине", deathsByCauseMale);
        show("Смерти женщин от внешних причин за 1966-1990, разбивка по причине", deathsByCauseFemale);
    }

    private void show(String title, Map<Integer, Double> m)
    {
        Util.out("===================================================================");
        Util.out(title);
        Util.out("");

        for (int code : m.keySet())
        {
            Util.out(String.format("%.0f;;%s", m.get(code), ExternalCauses.causeText(code)));
        }

        Util.out("");
    }
}
