package com.example.olxapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.olxapp.R;
import com.example.olxapp.adapter.AdapterAnuncios;
import com.example.olxapp.helper.ConfiguracaoFirebase;
import com.example.olxapp.helper.RecyclerItemClickListener;
import com.example.olxapp.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AnunciosActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerAnunciosPublicos;
    private Button buttonRegiao,buttonCategoria;
    private AdapterAnuncios adapterAnuncios;
    private List<Anuncio> ListaAnuncios = new ArrayList<>();
    private DatabaseReference anunciosPublicosref = ConfiguracaoFirebase.getFirebase()
            .child("anuncios");
    private AlertDialog dialog;
    private String filtroEstado = "";
    private String filtroCategoria = "";
    private boolean filtrandoPorEstado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnunciosPublicos.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(ListaAnuncios,this);
        recyclerAnunciosPublicos.setAdapter(adapterAnuncios);

        recuperarAnuncios();

        recyclerAnunciosPublicos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerAnunciosPublicos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Anuncio anuncioSelecionado = ListaAnuncios.get(position);
                                Intent i = new Intent(AnunciosActivity.this,DetalhesProdutosActivity.class);
                                i.putExtra("anunciosSelecionado", anuncioSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    public void filtrarPorEstado(View view){
        AlertDialog.Builder dialogEtsado = new AlertDialog.Builder(this);
        dialogEtsado.setTitle("Selecione o estado desejado");

        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner,null);
        dialogEtsado.setView(viewSpinner);

        final Spinner spinnerEstado = viewSpinner.findViewById(R.id.spinnerEstado);
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,estados
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(arrayAdapter);

        dialogEtsado.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtroEstado = spinnerEstado.getSelectedItem().toString();
                recuperarAnunciosPorEstado();
                filtrandoPorEstado = true;
            }
        });
        dialogEtsado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = dialogEtsado.create();
        dialog.show();
    }

    public void filtrarPorCategoria(View view){
        if(filtrandoPorEstado){
            AlertDialog.Builder dialogEtsado = new AlertDialog.Builder(this);
            dialogEtsado.setTitle("Selecione a categoria desejado");

            View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner,null);
            dialogEtsado.setView(viewSpinner);

            final Spinner spinnerCategoria = viewSpinner.findViewById(R.id.spinnerCategoria);
            String[] estados = getResources().getStringArray(R.array.estados);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,android.R.layout.simple_spinner_item,estados
            );
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(arrayAdapter);

            dialogEtsado.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    filtroCategoria = spinnerCategoria.getSelectedItem().toString();
                    recuperarAnunciosPorCategoria();
                }
            });
            dialogEtsado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = dialogEtsado.create();
            dialog.show();
        }else {
            Toast.makeText(this,
                    "Escolha primeiro uma região", Toast.LENGTH_SHORT).show();
        }


    }

    public void recuperarAnunciosPorCategoria(){
        anunciosPublicosref = ConfiguracaoFirebase.getFirebase()
                .child("anuncios")
                .child(filtroEstado)
                .child(filtroCategoria);

        anunciosPublicosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListaAnuncios.clear();
                for(DataSnapshot anuncios:dataSnapshot.getChildren()){
                    Anuncio anuncio = anuncios.getValue(Anuncio.class);
                    ListaAnuncios.add(anuncio);

                }
                Collections.reverse(ListaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recuperarAnunciosPorEstado(){
        anunciosPublicosref = ConfiguracaoFirebase.getFirebase()
                .child("anuncios")
                .child(filtroEstado);

        anunciosPublicosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListaAnuncios.clear();
                for(DataSnapshot categorias:dataSnapshot.getChildren()){
                    for(DataSnapshot anuncios:categorias.getChildren()){
                        Anuncio anuncio = anuncios.getValue(Anuncio.class);
                        ListaAnuncios.add(anuncio);

                    }
                }
                Collections.reverse(ListaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recuperarAnuncios(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando anúncios")
                .setCancelable(false)
                .build();
        dialog.show();

        ListaAnuncios.clear();
        anunciosPublicosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot estados:dataSnapshot.getChildren()){
                    for(DataSnapshot categorias:estados.getChildren()){
                        for(DataSnapshot anuncios:categorias.getChildren()){
                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            ListaAnuncios.add(anuncio);

                        }
                    }
                }

                Collections.reverse(ListaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(autenticacao.getCurrentUser() == null){
            menu.setGroupVisible(R.id.group_deslogado,true);
        }else {
            menu.setGroupVisible(R.id.group_logado,true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_cadastrar:
                startActivity(new Intent(getApplicationContext(),CadastroActivity.class));
                break;
            case R.id.menu_sair:
                autenticacao.signOut();
                invalidateOptionsMenu();
                break;
            case R.id.menu_anuncios:
                startActivity(new Intent(getApplicationContext(),MeusAnunciosActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inicializarComponentes(){
        recyclerAnunciosPublicos = findViewById(R.id.recyclerAnunciosPublicos);
    }
}
