package Utilities;

import android.util.SparseArray;

/*
    A SparseArray class to use spots ID.
    This should be refactored to use something more dynamic

 */

public class Magic_Spots_ID {

    private static SparseArray<String> citySpots = new SparseArray<String>();

    public Magic_Spots_ID() {

    }

    public static void initPlaces() {


        citySpots.put(1, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=4219&units=eu"); // ASHDOD
        citySpots.put(2, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3811&units=eu"); //ASHKELON
        citySpots.put(3, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3987&units=eu"); // HAIFA
        citySpots.put(4, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3662&units=eu"); // BAT YAM
        citySpots.put(5, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3783&units=eu"); //BEIT YANAY
        citySpots.put(6, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3983&units=eu"); //CASEAREA
        citySpots.put(7, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3660&units=eu"); //DOLPHINARIUM TLV
        citySpots.put(8, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3980&units=eu"); //HERZELIYA
        citySpots.put(9, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3980&units=eu"); //HAIFA HAZUK
        citySpots.put(10, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3658&units=eu"); //HILTON TLV
        citySpots.put(11, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3663&units=eu"); //HOF MARAVI
        citySpots.put(12, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3984&units=eu"); //MAAGAN MICHAEL
        citySpots.put(13, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3979&units=eu"); //MARINA HERZELIYA
        citySpots.put(14, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3975&units=eu"); //PALMACHIM
        citySpots.put(15, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3976&units=eu"); //RISHON LEZION
        citySpots.put(16, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3982&units=eu"); //SDOT YAM
        citySpots.put(17, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3986&units=eu"); // SIDNA ALI HERZEIYA
        citySpots.put(18, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3978&units=eu"); //TEL BARUCH
        citySpots.put(19, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3661&units=eu"); //TOPSY
        citySpots.put(20, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3977&units=eu"); //ZIKIM
        citySpots.put(21, "http://magicseaweed.com/api/c7N1GSdfpUsn0iB8pMiUEWoiolj1gVbL/forecast/?spot_id=3981&units=eu"); // ZVULUN HERZELIYA
    }

    public static String getCitySpots(int key) {

        return citySpots.get(key);

    }


}
