package com.deltarail.schedule.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by RAllam on 06/05/2016.
 */

public class DatesUtilityTest {
    @Test
    public void testIsValidPeriod() {
        String record = "BSNC130011512131512140000001 PXX1B611941122180008 DMUV   125      B S          P\n" +
                "LO1B611941115121316050800000012180008XC 2128 NABRDEEN 2128 21284         TB\n" +
                "LI1B611941115121316050800000012180008XC 2128 NABRDFJN           2129 00000000\n" +
                "LI1B611941115121316050800000012180008XC 2128 NCRGISTH           2130H00000000\n" +
                "LI1B611941115121316050800000012180008XC 2128 NNWHL              2138H00000000\n" +
                "LI1B611941115121316050800000012180008XC 2128 NSHVN    2144 2145H     21442145         T\n" +
                "LI1B611941115121316050800000012180008XC 2128 NCAARMNT           2151H00000000\n" +
                "LI1B611941115121316050800000012180008XC 2128 NLRNCKRK           2157 00000000\n" +
                "LI1B611941115121316050800000012180008XC 2128 NCRAIGO            2201 00000000\n" +
                "LI1B611941115121316050800000012180008XC 2128 NMONTRSE 2204H2206      220522061        T";
        Assert.assertFalse(DatesUtility.isValidPeriod(record));
    }
}
