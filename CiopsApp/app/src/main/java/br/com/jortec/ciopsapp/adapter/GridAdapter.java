package br.com.jortec.ciopsapp.adapter;

/**
 * Created by Jorliano on 01/11/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.util.List;

import br.com.jortec.ciopsapp.R;

/**
 * Created by Jorliano on 11/10/2015.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {
    private List<Bitmap> lista;
    private LayoutInflater inflater;



    public GridAdapter(Context c, List l){
        lista = l;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // View v = inflater.inflate(R.layout.item_moto, viewGroup, false);
        View v = inflater.inflate(R.layout.item_recycler, viewGroup, false);
        MyViewHolder mvh= new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
       try {
           Bitmap fotoAlterada = Bitmap.createScaledBitmap(lista.get(position), 360 , 470, true);
           viewHolder.imagem.setImageBitmap(fotoAlterada);
       }catch (Exception e){
           viewHolder.imagem.setImageBitmap(lista.get(position));
       }

    }
    @Override
    public int getItemCount() {
        return lista.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagem;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagem = (ImageView) itemView.findViewById(R.id.imagem);



        }

    }
}




/*public class GridAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Uri> lista;

    public GridAdapter(Context ctx, ArrayList<Uri> lista){
        this.ctx = ctx;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Criar componente imagemView
        ImageView im = new ImageView(ctx);
        im.setImageURI(lista.get(position));
        im.setAdjustViewBounds(true);
        return im;
    }
}*/
