package br.com.jortec.ciopsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.jortec.ciopsapp.adapter.GridAdapter;

public class ImagensActivity extends AppCompatActivity {
 private Toolbar toolbar;
 private ArrayList<Bitmap> lista;
 private RecyclerView recyclerView;
 private int tamhoGrid = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagens);

        toolbar = (Toolbar) findViewById(R.id.toolbar_imagens);
        toolbar.setTitle("Imagens Tiradas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null){
            tamhoGrid = 3;
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,tamhoGrid,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

       /* StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        llm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(llm);*/

        Bundle bd = this.getIntent().getExtras();
        if(bd.containsKey("imagens")){
            lista = (ArrayList<Bitmap>) bd.get("imagens");
            if(lista.isEmpty()) {
                // Log.i("Caminho da imagem", String.valueOf(lista.get(0)));
                Toast.makeText(this, "Nenhuma foto foi tirada", Toast.LENGTH_SHORT).show();
            }

            GridAdapter adapter = new GridAdapter(this,lista);
            recyclerView.setAdapter(adapter);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imagens, menu);
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

}

