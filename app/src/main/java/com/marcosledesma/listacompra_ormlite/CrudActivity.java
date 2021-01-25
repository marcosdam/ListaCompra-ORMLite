package com.marcosledesma.listacompra_ormlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.marcosledesma.listacompra_ormlite.databinding.ActivityCrudBinding;
import com.marcosledesma.listacompra_ormlite.databinding.ProductoCardBinding;
import com.marcosledesma.listacompra_ormlite.helpers.ProductosHelper;
import com.marcosledesma.listacompra_ormlite.modelos.Producto;

import java.sql.SQLException;

public class CrudActivity extends AppCompatActivity {

    // Binding para actualizar
    private ActivityCrudBinding binding;
    // Helper para acceder a la BD
    private ProductosHelper helper;
    // DAO para acceder al Producto con Clave Primaria
    private Dao<Producto, Integer> daoProductos;
    // Producto para obtener id
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrudBinding.inflate(getLayoutInflater()); // Obtenemos Layout del binding
        setContentView(binding.getRoot());

        // Obtener el id del Producto a mostrar
        int id = getIntent().getExtras().getInt("ID");

        // Inicializar acceso a Base de Datos
        helper = OpenHelperManager.getHelper(this, ProductosHelper.class);
        try {
            daoProductos = helper.getDaoProducto();
            producto = daoProductos.queryForId(id);

            binding.txtNombreCrud.setText(producto.getNombre());   // TextView (solo lectura)
            binding.txtPrecioCrud.setText("Precio:  " + producto.getPrecio() + "€");
            binding.txtCantidadCrud.setText("Cantidad:  " + producto.getCantidad());
            binding.txtImporteTotalCrud.setText("Importe total:     " + producto.getImporteTotal() + "€");   // TextView (solo lectura)

            // BTN Actualizar (Solo se podrá editar el precio y la cantidad, no el nombre, importeTotal se recalcula)
            binding.btnActualizarCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    producto.setPrecio(Float.parseFloat(binding.txtPrecioCrud.getText().toString()));
                    producto.setCantidad(Integer.parseInt(binding.txtCantidadCrud.getText().toString()));
                    producto.setImporteTotal(producto.getPrecio() * producto.getCantidad());

                    try {
                        daoProductos.update(producto);
                        finish();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}