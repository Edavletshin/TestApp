package ru.taximaster.testapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Photo implements ClusterItem {
    public final String photoUrl;
    private final LatLng mPosition;

    public Photo(LatLng position, String photoUrl) {
        mPosition = position;
        this.photoUrl = photoUrl;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

}

