package org.rosbris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSet
{
    private static Logger logger = LoggerFactory.getLogger(DataSet.class);

    public static class DataEntry
    {
        private Map<String, String> values = new HashMap<>();

        private DataEntry(String[] keys, String vals[])
        {
            for (int k = 0; k < vals.length; k++)
            {
                values.put(keys[k], vals[k]);
            }
        }

        public String asString(String key) throws Exception
        {
            if (!values.containsKey(key))
                throw new Exception("DataEntry has no value for key: " + key);
            return values.get(key);
        }

        public int asInt(String key) throws Exception
        {
            String s = asString(key);

            try
            {
                return Integer.valueOf(s);
            }
            catch (Exception ex)
            {
                throw new Exception("DataEntry value is not int, key: " + key + ", value: " + s, ex);
            }
        }

        public double asDouble(String key) throws Exception
        {
            String s = asString(key);

            try
            {
                return Double.valueOf(s);
            }
            catch (Exception ex)
            {
                throw new Exception("DataEntry value is not double, key: " + key + ", value: " + s, ex);
            }
        }
    }

    private List<DataEntry> values = new ArrayList<>();

    public static DataSet load(String path) throws Exception
    {
        DataSet ds = new DataSet();
        ds.loadFromFile(path);
        return ds;
    }

    private void loadFromFile(String path) throws Exception
    {
        String[] lines = Util.readFileAsString(path)
                .replace("\r", "")
                .split("\n");

        String[] keys = null;
        int n = 0;

        for (String line : lines)
        {
            n++;
            line = line.trim();
            if (line.length() == 0)
                continue;

            String[] vals = line.split(",");

            if (keys == null)
            {
                keys = vals;
                continue;
            }

            if (vals.length < keys.length)
            {
                logger.warn("Partial data in file {}, line {}, expected: {}, actual: {}",
                            path, n, keys.length, vals.length);
            }
            else if (vals.length != keys.length)
            {
                logger.error("Unexpected number of entries in file {}, line {}, expected: {}, actual: {}",
                             path, n, keys.length, vals.length);
                throw new Exception("Unexpected number of entries in file");
            }

            values.add(new DataEntry(keys, vals));
        }
    }
}
