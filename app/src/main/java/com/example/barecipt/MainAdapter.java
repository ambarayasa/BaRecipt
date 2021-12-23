package com.example.barecipt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    public List<ReciptHandler> resepHandlerList;
    private Context context;
    private RecyclerView recyclerView;
    private ImageButton btnDelete, btnEdit;
    private SharedPreferences preferences;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView namaResep;
        TextView pilihanResep, lamaMasakanResep, statusLamaMasakanResep, jenisResep, bahanResep, langkahResep;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaResep = itemView.findViewById(R.id.resep_nama_txt);
            pilihanResep = itemView.findViewById(R.id.resep_pilihan_txt);
            lamaMasakanResep = itemView.findViewById(R.id.resep_lama_masakan_txt);
            statusLamaMasakanResep = itemView.findViewById(R.id.resep_status_lama_masakan_txt);
            jenisResep = itemView.findViewById(R.id.resep_jenis_txt);
            bahanResep = itemView.findViewById(R.id.resep_bahan_txt);
            langkahResep = itemView.findViewById(R.id.resep_langkah_txt);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            preferences = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        }
    }

    public MainAdapter(List<ReciptHandler> resepHandlerList, Context context, RecyclerView recyclerView) {
        this.resepHandlerList = resepHandlerList;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recipt_row, parent, false);
        MainAdapter.ViewHolder viewHolder = new MainAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        ReciptHandler resepHandler = resepHandlerList.get(position);
        holder.namaResep.setText(String.valueOf(resepHandler.getNamaResep()));
        holder.pilihanResep.setText(String.valueOf(resepHandler.getPilihan()));
        holder.lamaMasakanResep.setText(String.valueOf(resepHandler.getLamaMemasak()));
        holder.statusLamaMasakanResep.setText(String.valueOf(resepHandler.getStatusLamaMemasak()));
        holder.jenisResep.setText(String.valueOf(resepHandler.getJenis()));
        holder.bahanResep.setText(String.valueOf(resepHandler.getBahan()));
        holder.langkahResep.setText(String.valueOf(resepHandler.getLangkah()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer itemId = Integer.valueOf(resepHandler.getId());
                String itemName = String.valueOf(resepHandler.getNamaResep());
                String itemPilihan = String.valueOf(resepHandler.getPilihan());
                String itemLamaMasakan = String.valueOf(resepHandler.getLamaMemasak());
                String itemStatusLamaMasakan = String.valueOf(resepHandler.getStatusLamaMemasak());
                String itemJenisMasakan = String.valueOf(resepHandler.getJenis());
                String itemBahanMasakan = String.valueOf(resepHandler.getBahan());
                String itemLangkahMasakan = String.valueOf(resepHandler.getLangkah());
                Intent detailIntent = new Intent(holder.itemView.getContext(), ShowRecipt.class);
                detailIntent.putExtra("id", itemId);
                detailIntent.putExtra("nama_resep", itemName);
                detailIntent.putExtra("pilihan", itemPilihan);
                detailIntent.putExtra("lama_masakan", itemLamaMasakan);
                detailIntent.putExtra("status_lama_masakan", itemStatusLamaMasakan);
                detailIntent.putExtra("jenis", itemJenisMasakan);
                detailIntent.putExtra("bahan", itemBahanMasakan);
                detailIntent.putExtra("langkah", itemLangkahMasakan);
                holder.itemView.getContext().startActivity(detailIntent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Integer itemId = Integer.valueOf(resepHandler.getId());
                String itemName = String.valueOf(resepHandler.getNamaResep());
                String itemPilihan = String.valueOf(resepHandler.getPilihan());
                String itemLamaMasakan = String.valueOf(resepHandler.getLamaMemasak());
                String itemStatusLamaMasakan = String.valueOf(resepHandler.getStatusLamaMemasak());
                String itemJenisMasakan = String.valueOf(resepHandler.getJenis());
                String itemBahanMasakan = String.valueOf(resepHandler.getBahan());
                String itemLangkahMasakan = String.valueOf(resepHandler.getLangkah());
                Intent detailIntent = new Intent(holder.itemView.getContext(), EditRecipt.class);
                detailIntent.putExtra("position", holder.getAdapterPosition());
                detailIntent.putExtra("id", itemId);
                detailIntent.putExtra("nama_resep", itemName);
                detailIntent.putExtra("pilihan", itemPilihan);
                detailIntent.putExtra("lama_masakan", itemLamaMasakan);
                detailIntent.putExtra("status_lama_masakan", itemStatusLamaMasakan);
                detailIntent.putExtra("jenis", itemJenisMasakan);
                detailIntent.putExtra("bahan", itemBahanMasakan);
                detailIntent.putExtra("langkah", itemLangkahMasakan);
                holder.itemView.getContext().startActivity(detailIntent);
            }
        });
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return resepHandlerList.size();
    }
}
