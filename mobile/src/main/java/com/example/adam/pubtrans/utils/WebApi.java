package com.example.adam.pubtrans.utils;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.IWebApiResponse;
import com.example.adam.pubtrans.runnables.DisruptionsFetchThread;
import com.example.adam.pubtrans.runnables.GetBroadNextDeparturesFetchThread;
import com.example.adam.pubtrans.runnables.GetNearMeFetchThread;
import com.example.adam.pubtrans.runnables.GetStoppingPatternFetchThread;
import com.example.adam.pubtrans.runnables.StopsOnLineFetchThread;
import com.example.adam.pubtrans.secrets.PTVAPIDetails;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Adam on 30/05/2015.
 */
public class WebApi {
    /**
     * Method to demonstrate building of Timetable API URL
     *
     * @param baseURL - Timetable API base URL without slash at the end ( Example :http://timetableapi.ptv.vic.gov.au )
     * @param privateKey - Developer Key supplied by PTV (((Example :"9c132d31-6a30-4cac8d8b-8a1970834799")
     * @param uri - Request URI with parameters(Example :/v2/nearme/latitude/-37.82392/longitude/144.9462017
     * @param developerId- Developer ID supplied by PTV
     * @return Complete URL with signature
     * @throws Exception
     *
     */
    public static String buildTTAPIURL(final String baseURL, final String privateKey, final String uri,
                                final int developerId) throws Exception  {
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";
        StringBuffer uriWithDeveloperID = new StringBuffer().append(uri).append(uri.contains("?") ? "&" : "?")
                .append("devid=" + developerId);
        byte[] keyBytes = privateKey.getBytes();
        byte[] uriBytes = uriWithDeveloperID.toString().getBytes();
        Key signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] signatureBytes = mac.doFinal(uriBytes);
        StringBuffer signature = new StringBuffer(signatureBytes.length * 2);
        for (byte signatureByte : signatureBytes)
        {
            int intVal = signatureByte & 0xff;
            if (intVal < 0x10)
            {
                signature.append("0");
            }
            signature.append(Integer.toHexString(intVal));
        }
        StringBuffer url = new StringBuffer(baseURL).append(uri).append(uri.contains("?") ? "&" : "?")
                .append("devid=" + developerId).append("&signature=" + signature.toString().toUpperCase());
        return url.toString();
    }

    public static void getNearMe(LatLng myLocation, IWebApiResponse getNearMeInterface) throws Exception{
        String url = buildTTAPIURL(PTVAPIDetails.BASE_URL, PTVAPIDetails.API_KEY, "/v2/nearme/latitude/"+myLocation.latitude+"/longitude/" + myLocation.longitude, PTVAPIDetails.USER_ID);
        (new Thread(new GetNearMeFetchThread(url, getNearMeInterface))).start();
    }

    public static void getDisruptions(IWebApiResponse getDisruptionInterface) throws Exception{
        String url = buildTTAPIURL(PTVAPIDetails.BASE_URL, PTVAPIDetails.API_KEY, "/v2/disruptions/modes/general,metro-train,metro-tram,metro-bus,regional-train,regional-bus,regional-coach", PTVAPIDetails.USER_ID);
        (new Thread(new DisruptionsFetchThread(url, getDisruptionInterface))).start();
    }

    public static void getStopsOnALine(String transportType, int lineId, IWebApiResponse getDisruptionInterface) throws Exception{
        int mode = getModeId(transportType);
        String url = buildTTAPIURL(PTVAPIDetails.BASE_URL, PTVAPIDetails.API_KEY, "/v2/mode/" + Integer.toString(mode) + "/line/" + Integer.toString(lineId) +  "/stops-for-line", PTVAPIDetails.USER_ID);
        Log.e("WebApi", url);
        (new Thread(new StopsOnLineFetchThread(url, getDisruptionInterface))).start();
    }


    public static void getBroadNextDepatures(String transportType, int stopId, int limit, IWebApiResponse getBroadNextDeparturesInterface) throws Exception{
        int mode = getModeId(transportType);
        String url = buildTTAPIURL(PTVAPIDetails.BASE_URL, PTVAPIDetails.API_KEY, "/v2/mode/" + Integer.toString(mode) + "/stop/" + Integer.toString(stopId) + "/departures/by-destination/limit/" + Integer.toString(limit), PTVAPIDetails.USER_ID);
        (new Thread(new GetBroadNextDeparturesFetchThread(url, getBroadNextDeparturesInterface))).start();
    }

    public static void getStoppingPattern(String transportType, int runId, int stopId, IWebApiResponse getStoppingPatternInterface) throws Exception{
        int mode = getModeId(transportType);
        String url = buildTTAPIURL(PTVAPIDetails.BASE_URL, PTVAPIDetails.API_KEY, "/v2/mode/" + Integer.toString(mode) + "/run/" + Integer.toString(runId) + "/stop/" + Integer.toString(stopId) + "/stopping-pattern", PTVAPIDetails.USER_ID);
        (new Thread(new GetStoppingPatternFetchThread(url, getStoppingPatternInterface))).start();
    }

    public static int getModeId(String transportType) {
        int mode;
        switch(transportType) {
            case "train": mode = 0; break;
            case "tram": mode = 1; break;
            case "bus": mode = 2; break;
            case "nightrider": mode = 4; break;
            default: mode = 3; break;
        }
        return mode;
    }

}
