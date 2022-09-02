package org.rosbris.constants;

public enum SettlementType
{
    TOTAL("T"),
    URBAN("U"),
    RURAL("R");

    private String code;

    SettlementType(String code)
    {
        this.code = code;
    }

    public String code()
    {
        return code;
    }
}