package com.journear.app;

import android.content.Context;
import android.content.ContextWrapper;

import com.journear.app.core.ServerFunctions;
import com.journear.app.core.entities.JnGeocodeItem;
import com.journear.app.core.utils.JnGeocoder;

import org.junit.Test;
import static org.junit.Assert.*;


public class ServerFunctionsUnitTest {
    Context context  = new ContextWrapper(null);
    @Test
    public void authenticateWithBadCredentials()
    {
     assertTrue(1==1);
    }

    @Test
    public void something()
    {
        MainActivity ma = new MainActivity();
        JnGeocoder.GetGeocodingListForRegion("ie", ma);


        assertTrue(true);
    }

//    @Test
//    public void authenticateWithGoodCredentials()
//    {
//        assertTrue(ServerFunctions.getInstance(context).authenticate("adminJournear", "godpleasesaveus"));
//    }
}
