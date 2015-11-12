package br.com.jortec.ciopsapp.network;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import br.com.jortec.ciopsapp.dominio.Cliente;
import br.com.jortec.ciopsapp.dominio.Emergencia;


/**
 * Created by Jorliano on 04/11/2015.
 */

public class NetworkConnection {
    private static NetworkConnection instance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    Cliente cliente;
    Emergencia emergencia;


    public NetworkConnection(Context c) {
        mContext = c;
        mRequestQueue = getRequestQueue();
    }


    public static NetworkConnection getInstance(Context c) {
        if (instance == null) {
            instance = new NetworkConnection(c.getApplicationContext());
        }
        return (instance);
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return (mRequestQueue);
    }


    public <T> void addRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


    public void execute(final Transaction transaction, String tag) {
        //Recebe os dados passa pelo doBefore
        WrapObjNetwork obj = transaction.doBefore();
        Gson g = new Gson();
        HashMap<String, String> par = new HashMap<String, String>();

        if (obj == null) {
            return;
        }

        String url = "http://192.168.0.104:8080/RESTfulExample/rest/ClienteService/"+obj.getMetodo();
        if (obj.getMetodo().equals("enviar")) {
            emergencia = obj.getEmergencia();
            par.put(obj.getMetodo(), g.toJson(emergencia));
        } else {
            cliente = obj.getCliente();
            par.put(obj.getMetodo(), g.toJson(cliente));
        }

        Log.i("LOG", "--> chamando metodo request");
        CustomRequest request = new CustomRequest(Request.Method.POST, url, par,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("LOG", " ---> " + response);
                        transaction.doAfter(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        transaction.doAfter(null);
                    }
                });

        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequestQueue(request);
    }



}
