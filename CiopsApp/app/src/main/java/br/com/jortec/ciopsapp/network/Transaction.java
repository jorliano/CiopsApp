package br.com.jortec.ciopsapp.network;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.jortec.ciopsapp.dominio.Cliente;

/**
 * Created by Jorliano on 07/11/2015.
 */
public interface Transaction {
    WrapObjNetwork doBefore();
    void doAfter(JSONArray jsonObject);
}