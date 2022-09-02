package org.rosbris;

import org.rosbris.DataSet.DataEntry;
import org.rosbris.constants.SettlementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopUtil
{
    private static Logger logger = LoggerFactory.getLogger(PopUtil.class);

    private static int age_groups[] = { 0, 1, 5,
            10, 15,
            20, 25,
            30, 35,
            40, 45,
            50, 55,
            60, 65,
            70, 75,
            80, 85
    };

    public static double people(DataEntry dr, DataSet pop) throws Exception
    {
        String year = dr.asString("Year");
        String sex = dr.asString("Sex");
        String reg = dr.asStringOptional("Reg");

        String group = SettlementType.TOTAL.code();

        DataEntry de = null;

        for (DataEntry dex : pop.entries())
        {
            if (!dex.asString("Year").equals(year))
                continue;
            if (!dex.asString("Sex").equals(sex))
                continue;
            if (reg != null && !dex.asString("Reg").equals(reg))
                continue;

            if (dex.has("Group") && !dex.asString("Group").equals(group))
                continue;

            de = dex;
            break;
        }

        if (de == null)
        {
            logger.error("Cannot find matching population record, year: {}, sex: {}, region: {}", year, sex, reg);
            throw new Exception("Cannot find matching population record");
        }

        double total = 0;

        for (int ag : age_groups)
        {
            double rate = dr.asDouble("Drac" + ag);
            int groupPopulation;
            if (de.has("Pdc" + ag))
            {
                groupPopulation = de.asInt("Pdc" + ag);
            }
            else
            {
                groupPopulation = de.asInt("PopD5a" + ag);
            }

            total += (rate * groupPopulation) / 1_000_000;
        }

        return total;
    }
}
