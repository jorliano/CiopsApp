package br.com.jortec.ciopsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import br.com.jortec.ciopsapp.dominio.Emergencia;
import br.com.jortec.ciopsapp.extra.VerificaConecao;
import br.com.jortec.ciopsapp.network.HttpConnection;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private LocationManager locationManager;
    private String localizacao;
    private String longitudeStr = "0";
    private String latitudeStr = "0";
    private Bitmap imagem;
    String tipo = "Ambulancia";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleMap mMiniMap; // Might be null if Google Play services APK is not available.

    private Drawer.Result navegadorDrawer;
    private AccountHeader.Result accountHeader;
    int itemDrawerSelected;

    //private ArrayList<Uri> listaImagens;
    private ArrayList<Bitmap> listaBitmap;
    Uri uri;
    int CAPITURA_IMAGEM = 1;

    Emergencia em;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //listaImagens = new ArrayList<Uri>();
        listaBitmap = new ArrayList<Bitmap>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CiopsApp");
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) this.findViewById(R.id.fab);

        // MAPA
        //Vai pra configuração de gps
        if (!VerificaConecao.verifyGps(this)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1);
        } else {
            setUpMapIfNeeded();
        }


        // NAVIGATION DRAWER
        // HEADER
        accountHeader = new AccountHeader()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(true)
                .withHeaderBackground(R.drawable.tema_drawer)
                        //.withHeaderBackground(android.R.drawable.screen_background_dark)
                .build();

        // BODY
        navegadorDrawer = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.START)
                .withSavedInstance(savedInstanceState)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fotos).withIcon(android.R.drawable.ic_menu_camera),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_enviar).withIcon(android.R.drawable.ic_menu_send),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(android.R.drawable.ic_menu_preferences),
                        new SwitchDrawerItem().withName(R.string.drawer_item_notificacao).withChecked(true).withIcon(android.R.drawable.ic_menu_agenda)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {


                        itemDrawerSelected = i;

                        if (i == 0) { // FOTOS
                            Intent intent2 = new Intent(MainActivity.this, ImagensActivity.class);
                            intent2.putExtra("imagens", listaBitmap);
                            startActivity(intent2);
                        } else if (i == 1) { // ENVIAR

                            enviarDadosSever();
                        }


                    }
                })
                .build();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chamar a função da camera
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!VerificaConecao.verifyGps(this)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1);
        } else {
            setUpMapIfNeeded();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                imagem = (Bitmap) bundle.get("data");
                //saveBitmap(img);
                listaBitmap.add(imagem);
            }
        }
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
                imprimirMensagem("Os dados foram enviados ");
            } else {
                imprimirMensagem(result);
            }

        }

    }

    private String post() {
        return HttpConnection.getSetDataWeb("http://ciopsapp.ddns.net:8090/RESTfulExample/rest/ClienteService/enviar", "enviar", new Gson().toJson(em));
    }

    private void imprimirMensagem(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMiniMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.miniMapa))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        //Gps
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng latLng = null;
        if (location != null) {
            latitudeStr = String.valueOf(location.getLatitude());
            longitudeStr = String.valueOf(location.getLongitude());
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            latLng = new LatLng(0, 0);
        }


       // mMap.addMarker(new MarkerOptions().position(latLng).title("Minha Localização"));
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

        mMiniMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMiniMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        mMiniMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mMiniMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMiniMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }

            }
        });

    }

    public void enviarDadosSever() {
        final RadioGroup rb = new RadioGroup(this);
        RadioButton rb1 = new RadioButton(this);
        rb1.setText("Ambulancia");

        RadioButton rb2 = new RadioButton(this);
        rb2.setText("Bombeiro");

        RadioButton rb3 = new RadioButton(this);
        rb3.setText("Policia");

        rb.addView(rb1);
        rb.addView(rb2);
        rb.addView(rb3);
        rb.check(rb1.getId());


        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("Opções");
        materialDialog.setView(rb);
        materialDialog.setBackgroundResource(R.color.material_dialogo);
        materialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  tipo = "Ambulancia";
                    int checked = rb.getCheckedRadioButtonId();
                    if(checked == 2)
                        tipo = "Bombeiro";
                    if(checked == 3)
                        tipo = "Policia";

                if (VerificaConecao.verifyConnection(MainActivity.this)) {
                    Bundle b = getIntent().getExtras();
                    em = new Emergencia();

                    if (b.get("codigo") != null) {
                        em.setId((Integer) b.get("codigo"));
                    }


                    for (int i = 0; i < listaBitmap.size(); i++) {
                        //Decodificar imagem
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        listaBitmap.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitMapData = stream.toByteArray();

                        if (i == 0)
                            em.setFoto(bitMapData);
                        else if (i == 1)
                            em.setFoto2(bitMapData);
                        else if (i == 2)
                            em.setFoto3(bitMapData);
                    }
                    //String encondeImagem = Base64.encodeToString(bitMapData, Base64.DEFAULT);


                    em.setLatitude(latitudeStr);
                    em.setLongitude(longitudeStr);
                    em.setTipo(tipo);

                    new HttpAsyncPOST().execute();
                    listaBitmap = new ArrayList<Bitmap>();
                    materialDialog.dismiss();
                } else {
                    imprimirMensagem("Verifique sua conexão com a internet");

                }
            }
        });

        materialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });

        materialDialog.show();


    }

    //localizacao = "https://www.google.com.br/maps/place/" + latitudeStr + "," + longitudeStr;

    /*  public void saveBitmap(Bitmap pBitmap) {

        try {
            File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String nomeImagem = diretorio.getPath() + "/" + System.currentTimeMillis() + ".jpg";
            // uri = Uri.fromFile(new File(nomeImagem));

            File ifile = new File(nomeImagem);
            //File ifile= new File(Environment.getExternalStorageDirectory() + "/imgsApp/", "imagem1.png");
            FileOutputStream outStream = new FileOutputStream(ifile);
            pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.close();

            //uri = Uri.fromFile(ifile);
            //listaImagens.add(uri);

            Log.i("Caminho Uri", String.valueOf(uri));

        } catch (Exception e) {
            Log.e("Could not save", e.toString());
        }
    }*/
}
