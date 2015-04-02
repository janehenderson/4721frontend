package com.example.frontendian.mappprototype;

/**
 * @ Authors Reece, James, Milton, Federico
 */

import com.google.android.gms.common.ConnectionResult;

public interface GeofenceRegistrationCallbacks {
    public void onApiClientConnected();

    public void onApiClientSuspended();

    public void onApiClientConnectionFailed(ConnectionResult connectionResult);

    public void onGeofencesRegisteredSuccessful();
}
