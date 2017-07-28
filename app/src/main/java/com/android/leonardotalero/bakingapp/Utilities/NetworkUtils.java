package com.android.leonardotalero.bakingapp.Utilities;

/**
 * Created by leonardotalero on 7/7/17.
 */

import android.content.Context;
import android.net.Uri;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import com.android.leonardotalero.bakingapp.BuildConfig;




/*
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {
    //"http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]";



    final static String API_KEY= (BuildConfig.THE_MOVIE_DB_API_TOKEN);
    final static String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    //final static  String BASE_URL_IMAGE="http://image.tmdb.org/t/p/w342/";

    final static String PARAM_API= "api_key";
    final static String PARAM_LANGUAGE="language";
    final static String PARAM_LANGUAGE_default="en-US";
    final static String PARAM_PAGE="page";
    final static String PARAM_REGION="region";
    final static String PARAM_QUERY="query";


    public static URL buildUrl(String searchQuery) {

        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                //.appendQueryParameter(PARAM_API, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



    public static URL buildUrlImage(String urlImage) {

        Uri builtUri = Uri.parse(urlImage)
                .buildUpon()
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }




    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(30000);
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }



}