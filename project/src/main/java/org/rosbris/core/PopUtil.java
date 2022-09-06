package org.rosbris.core;

import org.rosbris.constants.SettlementType;
import org.rosbris.core.DataSet.DataEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopUtil
{
    private static Logger logger = LoggerFactory.getLogger(PopUtil.class);

    private static int[] age_groups = { 0, 1, 5,
            10, 15,
            20, 25,
            30, 35,
            40, 45,
            50, 55,
            60, 65,
            70, 75,
            80, 85
    };

    public static int[] getAgeGroups()
    {
        return age_groups;
    }

    public static double people(DataEntry dr, DataSet pop) throws Exception
    {
        DataEntry de = findPopulationDataEntry(dr, pop);

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

    public static double people(DataEntry dr, DataSet pop, int ag) throws Exception
    {
        DataEntry de = findPopulationDataEntry(dr, pop);

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

        return (rate * groupPopulation) / 1_000_000;
    }

    private static DataEntry findPopulationDataEntry(DataEntry dr, DataSet pop) throws Exception
    {
        String year = dr.asString("Year");
        String sex = dr.asString("Sex");
        String reg = dr.asStringOptional("Reg");

        String group = SettlementType.TOTAL.code();

        for (DataEntry de : pop.entries())
        {
            if (!de.asString("Year").equals(year))
                continue;
            if (!de.asString("Sex").equals(sex))
                continue;
            if (reg != null && !de.asString("Reg").equals(reg))
                continue;

            if (de.has("Group") && !de.asString("Group").equals(group))
                continue;

            return de;
        }

        logger.error("Cannot find matching population record, year: {}, sex: {}, region: {}", year, sex, reg);
        throw new Exception("Cannot find matching population record");
    }
}
