package br.com.jortec.ciopsapp.network;

import br.com.jortec.ciopsapp.dominio.Cliente;
import br.com.jortec.ciopsapp.dominio.Emergencia;

/**
 * Created by Jorliano on 07/11/2015.
 */
public class WrapObjNetwork {

    private Cliente cliente;
    private Emergencia emergencia;
    private String metodo;

    public  WrapObjNetwork(Cliente cliente,String metodo){
        this.setCliente(cliente);
        this.setMetodo(metodo);
    }

    public  WrapObjNetwork(Emergencia emergencia,String metodo){
        this.setEmergencia(emergencia);
        this.setMetodo(metodo);

    }


    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Emergencia getEmergencia() {
        return emergencia;
    }

    public void setEmergencia(Emergencia emergencia) {
        this.emergencia = emergencia;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }


}
