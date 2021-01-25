package com.marcosledesma.listacompra_ormlite.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.Dao;
import com.marcosledesma.listacompra_ormlite.CrudActivity;
import com.marcosledesma.listacompra_ormlite.MainActivity;
import com.marcosledesma.listacompra_ormlite.R;
import com.marcosledesma.listacompra_ormlite.modelos.Producto;

import java.sql.SQLException;
import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoVH> {

    // Context, objects y resource
    private Context context;
    private List<Producto> objects;
    private int resource;
    private Dao<Producto, Integer> daoProductos;

    public ProductosAdapter(Context context, List<Producto> objects, int resource, Dao<Producto, Integer> daoProductos){
        this.context = context;
        this.objects = objects;
        this.resource = resource;
        this.daoProductos = daoProductos;
    }

    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, null);   // View
        // Layout params
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ProductoVH(view);    //Devuelve new ProductoVH (no null)
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {
        holder.txtNombre.setText(objects.get(position).getNombre());
        holder.txtCantidad.setText("Cantidad:   " + objects.get(position).getCantidad());

        // Asignar onClick a ImageButton lápiz (NO a toda la fila (a tod0 el ViewHolder) )
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Crear Bundle con el ID del producto en la BD
                Bundle bundle = new Bundle();
                bundle.putInt("ID", objects.get(position).getId());
                // 2. Crear Intent
                // (desde un Adapter no podemos decir que el origen es una Activity, en este caso es el context)
                Intent intent = new Intent(context, CrudActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        // Asignar onClick a ImageButton papelera (btn del VH)
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar producto");
                builder.setMessage("¿Seguro?");
                builder.setNegativeButton("CANCELAR", null);

                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // Obtener ID del producto a eliminar
                            Producto producto = daoProductos.queryForId(objects.get(position).getId());
                            daoProductos.delete(producto);
                            objects.remove(producto);
                            ProductosAdapter.this.notifyDataSetChanged();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
                // Mostrar Alert Dialog
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();   // Devuelve el tamaño de la lista objects (no 0)
    }

    public static class ProductoVH extends RecyclerView.ViewHolder {
        ImageButton btnEliminar, btnEditar;
        TextView txtNombre, txtCantidad;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            btnEditar = itemView.findViewById(R.id.btnEditarCard);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCard);
            txtNombre = itemView.findViewById(R.id.txtNombreCard);
            txtCantidad = itemView.findViewById(R.id.txtCantidadCard);
        }
    }
}
