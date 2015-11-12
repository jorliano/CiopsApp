package br.com.jortec.ciopsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import br.com.jortec.ciopsapp.dominio.Cliente;
import br.com.jortec.ciopsapp.extra.VerificaConecao;
import br.com.jortec.ciopsapp.network.HttpConnection;
import br.com.jortec.ciopsapp.network.NetworkConnection;
import br.com.jortec.ciopsapp.network.Transaction;
import br.com.jortec.ciopsapp.network.WrapObjNetwork;

public class LoginActivity extends AppCompatActivity {
    private EditText edtLogin;
    private EditText edtSenha;
    private Button btLogar;
    private Button btCadastro;
    Cliente c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin = (EditText) findViewById(R.id.edtLogarLogin);
        edtSenha = (EditText) findViewById(R.id.edtLogarSenha);
        btLogar = (Button) findViewById(R.id.btLogar);
        btCadastro = (Button) findViewById(R.id.btCadastro);

         btLogar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //NetworkConnection.getInstance(v.getContext()).execute(LoginActivity.this,"tag");
                 if (VerificaConecao.verifyConnection(v.getContext())) {
                     c = new Cliente();
                     c.setLogin(edtLogin.getText().toString());
                     c.setSenha(edtSenha.getText().toString());

                    // Chama a ação que se comunica com o webservice
                    // new  HttpAsyncPOST().execute();
                     startActivity(new Intent(LoginActivity.this,MainActivity.class));
                     
                 } else {
                     Toast.makeText(v.getContext(), "Verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
                 }
                 ;
             }
         });

        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CadastroActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HttpAsyncPOST extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return post();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Script", "ANSWER: " + result);
            if(result.length() < 10 && result != null){
                startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("codigo", Integer.parseInt(result)));
            }else{
                imprimirMensagem(result);
            }

        }

    }
    private String post(){

        return  HttpConnection.getSetDataWeb("http://192.168.0.104:8080/RESTfulExample/rest/ClienteService", "logar", new Gson().toJson(c));
    };

    private void imprimirMensagem(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }


}
