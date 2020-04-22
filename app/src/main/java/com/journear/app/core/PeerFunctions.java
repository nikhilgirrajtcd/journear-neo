package com.journear.app.core;

import com.journear.app.core.entities.NearbyDevice;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;

public class PeerFunctions {

    public static LinkedList<NearbyDevice> getNearbyDevices() {

        throw new UnsupportedOperationException("implementation pending");
    }

    public static String getBroadcastString(NearbyDevice ndOwnJourneyPlan) {
        if (ndOwnJourneyPlan == null) {
            // TODO: Remove NULL Put the current user here, create a service locator
            return getBroadcastString(NearbyDevice.getDummy());
        }
        return StringUtils.joinWith("|",
                ndOwnJourneyPlan.getUser() == null ? "Dummy" : ndOwnJourneyPlan.getUser().name,
                ndOwnJourneyPlan.getSource2() == null ? -1 : ndOwnJourneyPlan.getSource2().id,
                ndOwnJourneyPlan.getDestination2() == null ? -1 : ndOwnJourneyPlan.getDestination2().id,
                ndOwnJourneyPlan.getTravelTime().toString());
    }
}
