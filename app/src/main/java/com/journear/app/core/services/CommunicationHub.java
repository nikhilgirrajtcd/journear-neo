package com.journear.app.core.services;

import com.journear.app.core.entities.NearbyDevice;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class CommunicationHub {

//    private Map<String, String>

    public void publish(JourNearMessage message) {

    }

    // returns message Id
    public String sendMessage(NearbyDevice nearbyDevice, JourNearMessageSet message, CommunicationListener responseListener) {
return null;
    }

    private String createMessageId(NearbyDevice nearbyDevice, JourNearMessage lastMessage) {
        if(nearbyDevice == null)
            return  null;

        if(lastMessage == null)
        {
            return nearbyDevice.getId() + "::1";
        }
        else {
            int lastId = Integer.parseInt(StringUtils.split(lastMessage.getMessageId(), ":")[1]);
            return nearbyDevice.getId() + ":" + (lastId + 1);
        }
    }
}
