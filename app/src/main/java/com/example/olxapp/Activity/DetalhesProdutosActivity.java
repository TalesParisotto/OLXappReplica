package com.example.olxapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olxapp.R;
import com.example.olxapp.model.Anuncio;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CarouselViewPagerScroller;
import com.synnapps.carouselview.ImageListener;

public class DetalhesProdutosActivity extends AppCompatActivity {

    private CarouselView carouselView;
    private TextView titulo;
    private TextView descricao;
    private TextView estado;
    private TextView preco;
    private Anuncio anuncioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produtos);

        getSupportActionBar().setTitle("Detalhe produto");

        inicializarComponenetes();

    anuncioSelecionado = (Anuncio) getIntent().getSerializableExtra("anunciosSelecionado");
    if(anuncioSelecionado != null){

        titulo.setText(anuncioSelecionado.getTitulo());
        descricao.setText(anuncioSelecionado.getDescricao());
        estado.setText(anuncioSelecionado.getEstado());
        preco.setText(anuncioSelecionado.getValor());

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                String urlString = anuncioSelecionado.getFotos().get(position);
                Picasso.get().load(urlString).into(imageView);
            }
        };
        carouselView.setPageCount(anuncioSelecionado.getFotos().size());
        carouselView.setImageListener(imageListener);
    }
    }

    public void visualizarTelefone(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",anuncioSelecionado.getTelefone(),null));
        startActivity(i);
    }
    private void inicializarComponenetes(){
        carouselView = findViewById(R.id.carouselView);
        titulo = findViewById(R.id.textTituloDetalhe);
        descricao = findViewById(R.id.textDescricaoDetalhe);
        estado = findViewById(R.id.textEstadoDetalhe);
        preco = findViewById(R.id.textPrecoDetalhe);
    }
}
