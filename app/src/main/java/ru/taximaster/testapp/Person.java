package ru.taximaster.testapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {
    public final String photoUrl;
    private final LatLng mPosition;

    public Person(LatLng position, String photoUrl) {
        mPosition = position;
        this.photoUrl = photoUrl;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

}

