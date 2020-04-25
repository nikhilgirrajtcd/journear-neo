package com.journear.app.core.interfaces;

import com.journear.app.core.entities.NearbyDevice;

public interface JnPeerDiscoveryListener {

    void onPeerDiscovered(NearbyDevice obj);
}
