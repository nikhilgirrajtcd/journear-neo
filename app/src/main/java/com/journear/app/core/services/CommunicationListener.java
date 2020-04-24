package com.journear.app.core.services;

import com.journear.app.core.entities.NearbyDevice;

public interface CommunicationListener {
    void onResponse(JnMessage message);
    void onExpire(JnMessage expiredMessage, NearbyDevice nearbyDevice);
}
