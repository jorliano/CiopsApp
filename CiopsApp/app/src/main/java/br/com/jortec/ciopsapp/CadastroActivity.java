package br.com.jortec.ciopsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import br.com.jortec.ciopsapp.dominio.Cliente;
import br.com.jortec.ciopsapp.extra.VerificaConecao;
import br.com.jortec.ciopsapp.network.HttpConnection;
import br.com.jortec.ciopsapp.network.NetworkConnection;
import br.com.jortec.ciopsapp.network.Transaction;
import br.com.jortec.ciopsapp.network.WrapObjNetwork;

public class CadastroActivity extends AppCompatActivity {
    private EditText nome;
    private EditText email;
    private EditText telefone;
    private EditText cidade;
    private EditText login;
    private EditText senha;
    private Spinner spnSexo;
    private Button btCadastrar;
    private Toolbar toolbar;

    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        toolbar = (Toolbar) findViewById(R.id.toolbar_cadastro);
        toolbar.setTitle("Cadastro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nome = (EditText) findViewById(R.id.edtNome);
        email = (EditText) findViewById(R.id.edtEmail);
        telefone = (EditText) findViewById(R.id.edtTelefone);
        cidade = (EditText) findViewById(R.id.edtCidade);
        login = (EditText) findViewById(R.id.edtLogin);
        senha = (EditText) findViewById(R.id.edtSenha);
        // spnSexo = (Spinner) findViewById(R.id.spinnerSexo);
        // String[] campos = new String[]{"Masculino", "Feminino"};
        // spnSexo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, campos));

        btCadastrar = (Button) findViewById(R.id.btCadastrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VerificaConecao.verifyConnection(v.getContext())) {
                    cliente = new Cliente();
                    cliente.setNome(nome.getText().toString());
                    cliente.setEmail(email.getText().toString());
                    cliente.setTelefone(telefone.getText().toString());
                    cliente.setCidade(cidade.getText().toString());
                    cliente.setLogin(login.getText().toString());
                    cliente.setSenha(senha.getText().toString());

                    if(!cliente.getNome().equals("") && !cliente.getLogin().equals("") && !cliente.getSenha().equals("") ){
                        new HttpAsyncPOST().execute();
                    }else{
                        imprimirMensagem("Preencha os campos vazios");
                    }


                    //startActivity(new Intent(CadastroActivity.this,MainActivity.class));

                } else {
                    Toast.makeText(v.getContext(), "Verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    private class HttpAsyncPOST extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return post();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Script", "ANSWER: " + result);
            if (result.length() < 10) {
                startActivity(new Intent(CadastroActivity.this, MainActivity.class).putExtra("codigo", Integer.parseInt(result)));
            } else {
                imprimirMensagem(result);
            }

        }

    }

    private String post() {
        return HttpConnection.getSetDataWeb("http://ciopsapp.ddns.net:8090/RESTfulExample/rest/ClienteService/cadastrar", "cadastrar", new Gson().toJson(cliente));
    }

    ;

    private void imprimirMensagem(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }


}
