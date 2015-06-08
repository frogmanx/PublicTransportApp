package com.example.adam.pubtrans.utils;

import com.example.adam.pubtrans.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by Adam on 7/06/2015.
 */
public class ImageUtils {


    public static int  getTransportImageResource(String transportType) {
        switch(transportType) {
            case PTVConstants.TRAM_TYPE: return R.drawable.ic_directions_transit_black;
            case PTVConstants.BUS_TYPE: return R.drawable.ic_directions_bus_black;
            case PTVConstants.TRAIN_TYPE: return R.drawable.ic_directions_train_black;
            case PTVConstants.NIGHTRIDER_TYPE: return R.drawable.ic_directions_bus_black;

        }
        return R.mipmap.ic_launcher;
    }

    public static BitmapDescriptor getTransportPinDescriptor(String transportType) {

        switch(transportType) {
            case PTVConstants.TRAM_TYPE: return BitmapDescriptorFactory.fromResource(R.drawable.tram_pin);
            case PTVConstants.BUS_TYPE: return BitmapDescriptorFactory.fromResource(R.drawable.bus_pin);
            case PTVConstants.TRAIN_TYPE: return BitmapDescriptorFactory.fromResource(R.drawable.train_pin);
            case PTVConstants.NIGHTRIDER_TYPE: return BitmapDescriptorFactory.fromResource(R.drawable.bus_pin);

        }
        return BitmapDescriptorFactory.defaultMarker();
    }
}
