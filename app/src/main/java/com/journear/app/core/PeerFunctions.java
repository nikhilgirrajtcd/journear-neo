package com.journear.app.core;

import android.util.Log;

import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.UserSkimmed;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.Random;

public class PeerFunctions {

    private static final String LOGTAG = "PeerFunctions";

    public static LinkedList<NearbyDevice> getNearbyDevices() {

        throw new UnsupportedOperationException("implementation pending");
    }

    public static String getBroadcastString(NearbyDevice ndOwnJourneyPlan) {
        if (ndOwnJourneyPlan == null) {
            // TODO: Remove NULL Put the current user here, create a service locator
            return getBroadcastString(NearbyDevice.getDummy());
        }
        return StringUtils.joinWith("|",
                ndOwnJourneyPlan.getOwner() == null ? "Dummy" : ndOwnJourneyPlan.getOwner().name, //0
                ndOwnJourneyPlan.getSource2() == null ? -1 : ndOwnJourneyPlan.getSource2().id, //1
                ndOwnJourneyPlan.getDestination2() == null ? -1 : ndOwnJourneyPlan.getDestination2().id, //2
                ndOwnJourneyPlan.getTravelTime().toString(), //3
                LocalFunctions.getGenderIndex(ndOwnJourneyPlan.getOwner().getGender()), //4
                ndOwnJourneyPlan.getPreferSameGender() ? 1 : 0, //5
                ndOwnJourneyPlan.getModeOfJourney(), //6
                ndOwnJourneyPlan.getOwner() == null ? (new Random()).nextInt() : ndOwnJourneyPlan.getOwner().getUserId(), // 7
                ndOwnJourneyPlan.getTravelPlanId() // 8
        );
    }

    public static NearbyDevice parseBroadcastString(String s) {
        String[] all = StringUtils.split(s, '|');
        Log.d(LOGTAG, "parsingBroadcastString: " + s);
        NearbyDevice nd = new NearbyDevice();
        UserSkimmed u = new UserSkimmed();
        u.setName(all[0]);
        u.setGender(LocalFunctions.getGenderString(Integer.parseInt(all[5])));
        nd.setSource2(all[1]);
        nd.setDestination2(all[2]);
        nd.setTravelTime(all[3]);
        nd.setPreferSameGender(all[4].equals("1"));
        nd.setModeOfJourney(all[6]);
        u.setUserId(all[7]);
        nd.setTravelPlanId(all[8]);
        nd.setOwner(u);
        return nd;
    }
}
