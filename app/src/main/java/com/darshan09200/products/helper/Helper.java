package com.darshan09200.products.helper;

import android.text.Html;
import android.text.Spanned;

import com.google.android.gms.maps.model.LatLng;

public class Helper {
    public static String getStaticMapUrl(LatLng latLng, String apiKey) {
        // https://shorturl.at/lmA69 -> https://firebasestorage.googleapis.com/v0/b/persistence-darshan09200.appspot.com/o/marker.png?alt=media
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latLng.latitude + "," + latLng.longitude + "&zoom=15&size=600x400&markers=icon:https://shorturl.at/lmA69%7C" + latLng.latitude + "," + latLng.longitude + "&key=" + apiKey;
    }

    public static Spanned formatPrice(double price) {
        String priceAsString = String.valueOf(price);
        int priceDecimalIndex = priceAsString.indexOf(".");

        String priceInteger = priceAsString.substring(0, priceDecimalIndex);
        String priceDecimal = priceAsString.substring(priceDecimalIndex);

        return Html.fromHtml("$ " + priceInteger + "<sup><small>" + priceDecimal + "</small></sup>", Html.FROM_HTML_MODE_LEGACY);
    }
}
