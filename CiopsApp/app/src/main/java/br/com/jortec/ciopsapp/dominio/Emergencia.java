package br.com.jortec.ciopsapp.dominio;

import android.graphics.Bitmap;

/**
 * Created by Jorliano on 05/11/2015.
 */
public class Emergencia {
    private int id;
    private String tipo;
    private String logintude;
    private String latitude;
    private byte[] foto;
    private byte[] foto2;
    private byte[] foto3;


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLogintude() {
        return logintude;
    }

    public void setLogintude(String logintude) {
        this.logintude = logintude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public byte[] getFoto2() {
        return foto2;
    }

    public void setFoto2(byte[] foto2) {
        this.foto2 = foto2;
    }

    public byte[] getFoto3() {
        return foto3;
    }

    public void setFoto3(byte[] foto3) {
        this.foto3 = foto3;
    }
}


