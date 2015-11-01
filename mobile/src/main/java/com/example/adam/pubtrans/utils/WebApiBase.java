package com.example.adam.pubtrans.utils;

import android.util.Log;

import com.example.adam.pubtrans.interfaces.WebApiResponse;
import com.example.adam.pubtrans.secrets.PTVAPIDetails;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Adam on 31/10/2015.
 */
public class WebApiBase {
    public static final String TAG = "WebApiBase";

    protected static void newCall(final String uri, final WebApiResponse callback) {
        OkHttpClient mClient = new OkHttpClient();
        mClient.setReadTimeout(5, TimeUnit.MINUTES);
        Request request = buildTTAPIURL(PTVAPIDetails.BASE_URL,
                PTVAPIDetails.API_KEY, uri, PTVAPIDetails.USER_ID, callback);
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                //Cache and try again on network resume.
                callback.failure("Reponse invalid", null);
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.i(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                try {
                    Object object = new JSONTokener(response.body().string()).nextValue();
                    Log.i(TAG, object.toString());
                    callback.success(object);

                } catch (JSONException e) {
                    callback.failure(e.toString(), null);
                }


            }
        });
    }

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
    public static Request buildTTAPIURL(final String baseURL, final String privateKey, final String uri,
                                       final int developerId, final WebApiResponse callback) {
        try {

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

            Request request = new Request.Builder()
                    .url(url.toString())
                    .build();
            return request;
        }
        catch (NoSuchAlgorithmException e) {
            callback.failure(e.toString(), null);
        }
        catch (InvalidKeyException e) {
            callback.failure(e.toString(), null);
        }
        return null;
    }

}
