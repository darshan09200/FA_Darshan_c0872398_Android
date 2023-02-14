package com.darshan09200.products.helper;

import android.text.Html;
import android.text.Spanned;

import com.google.android.gms.maps.model.LatLng;

public class Helper {
    public static String getStaticMapUrl(LatLng latLng, String apiKey) {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latLng.latitude + "," + latLng.longitude + "&zoom=15&size=600x400&markers=label:Product%7C" + latLng.latitude + "," + latLng.longitude + "&key=" + apiKey;
    }

    public static Spanned formatPrice(double price) {
        String priceAsString = String.valueOf(price);
        int priceDecimalIndex = priceAsString.indexOf(".");

        String priceInteger = priceAsString.substring(0, priceDecimalIndex);
        String priceDecimal = priceAsString.substring(priceDecimalIndex);

        return Html.fromHtml("$ " + priceInteger + "<sup><small>" + priceDecimal + "</small></sup>", Html.FROM_HTML_MODE_LEGACY);
    }
}
