package com.marcosledesma.listacompra_ormlite;

import android.content.DialogInterface;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.marcosledesma.listacompra_ormlite.adapters.ProductosAdapter;
import com.marcosledesma.listacompra_ormlite.databinding.ActivityMainBinding;
import com.marcosledesma.listacompra_ormlite.databinding.ProductoCardBinding;
import com.marcosledesma.listacompra_ormlite.helpers.ProductosHelper;
import com.marcosledesma.listacompra_ormlite.modelos.Producto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // viewBinding
    private ActivityMainBinding binding;
    // Binding para eliminar desde la Card
    private ProductoCardBinding bindingCard;

    // Base de Datos
    private ProductosHelper helper;
    private Dao<Producto, Integer> daoProductos;

    // Objetos para el RecyclerView
    private ProductosAdapter adapter;
    private int resource = R.layout.producto_card;
    private RecyclerView.LayoutManager lm;
    private ArrayList<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        bindingCard = ProductoCardBinding.inflate(getLayoutInflater()); //
        setContentView(binding.getRoot());

        // Obtener el id del Producto a eliminar
        //int idEliminar = getIntent().getExtras().getInt("ID_ELIMINAR");

        // Instanciar Base de Datos -> // helper = new ProductosHelper(this);
        // Instanciar Base de Datos (compatible con cualquier versión de ORM Lite)
        helper = OpenHelperManager.getHelper(this, ProductosHelper.class);

        // Instanciar Recycler
        listaProductos = new ArrayList<>();
        lm = new LinearLayoutManager(this);
        adapter = new ProductosAdapter(this, listaProductos, resource);
        binding.contenido.recyclerView.setHasFixedSize(true);
        binding.contenido.recyclerView.setAdapter(adapter);
        binding.contenido.recyclerView.setLayoutManager(lm);


        if (helper != null){
            try {
                daoProductos = helper.getDaoProducto();
                // Rellenar lista con datos de la Base de datos (queryForAll)
                listaProductos.addAll(daoProductos.queryForAll());
                adapter.notifyDataSetChanged();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        // CREAR con el fab
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llamar a crearProducto y mostrarlo (Alert Dialog)
                crearProducto().show();
            }
        });

        // ELIMINAR con ImageButton en card
        // (pedir confirmación con otro Alert Dialog)
        bindingCard.btnEliminarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("¿Estás seguro de que quieres eliminar?");
                builder.setNegativeButton("CANCELAR", null);

                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("¿Seguro? No podrás recuperar el dato eliminado");
                        builder.setPositiveButton("SÍ, ELIMINAR", null);
                        try {
                            // Obtener ID del producto a eliminar (id de la BD, no resource del adapter)
                            Producto producto = daoProductos.queryForId(getIntent().getExtras().getInt("ID_ELIMINAR"));
                            daoProductos.delete(producto);  //
                            finish();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
                // Mostrar Alert Dialog
                builder.show();
            }
        });
    }

    // Func para crear ordenador con el Alert Dialog
    private AlertDialog crearProducto(){
        // 1. Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Producto");
        builder.setCancelable(false);
        View layoutAlert = getLayoutInflater().inflate(R.layout.producto_dialog, null);

        // 2. TextViews para el Alert
        TextView txtNombre = layoutAlert.findViewById(R.id.txtNombreDialog);
        TextView txtPrecio = layoutAlert.findViewById(R.id.txtPrecioDialog);
        TextView txtCantidad = layoutAlert.findViewById(R.id.txtCantidadDialog);
        TextView txtImporteTotal = layoutAlert.findViewById(R.id.txtImporteTotalDialog);

        builder.setView(layoutAlert);

        // 3. Botones del Alert Dialog
        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("CREAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!txtNombre.getText().toString().isEmpty()
                        && !txtPrecio.getText().toString().isEmpty()
                        && !txtCantidad.getText().toString().isEmpty()){
                    // Si los campos están rellenos, crear producto y añadirlo a la lista
                    Producto producto = new Producto();
                    producto.setNombre(txtNombre.getText().toString());
                    producto.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                    producto.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                    producto.setImporteTotal(producto.getImporteTotal());   //

                    try {
                        daoProductos.create(producto);
                        int lastId = daoProductos.extractId(producto);
                        producto.setId(lastId);
                        listaProductos.add(producto);
                        adapter.notifyDataSetChanged();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });

        // 3. Return AlertDialog
        return builder.create();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Siempre que vuelta de la otra actividad limpiará la lista y la volverá a rellenar
        // con lo obtenido de la query
        listaProductos.clear();
        try {
            listaProductos.addAll(daoProductos.queryForAll());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}