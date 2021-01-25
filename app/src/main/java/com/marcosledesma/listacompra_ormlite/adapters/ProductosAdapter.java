package com.marcosledesma.listacompra_ormlite.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marcosledesma.listacompra_ormlite.CrudActivity;
import com.marcosledesma.listacompra_ormlite.MainActivity;
import com.marcosledesma.listacompra_ormlite.R;
import com.marcosledesma.listacompra_ormlite.modelos.Producto;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoVH> {

    // Context, objects y resource
    private Context context;
    private List<Producto> objects;
    private int resource;

    public ProductosAdapter(Context context, List<Producto> objects, int resource){
        this.context = context;
        this.objects = objects;
        this.resource = resource;
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
        holder.txtPrecio.setText("Precio:   " + objects.get(position).getPrecio() + "€");
        holder.txtCantidad.setText("Cantidad:   " + objects.get(position).getCantidad());
        holder.txtImporteTotal.setText("Importe total:   " + objects.get(position).getImporteTotal() + "€");

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
        // (Aquí no AlertDialog, solo crear intent para eliminar mediante id en Main)
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID_ELIMINAR", objects.get(position).getId());
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();   // Devuelve el tamaño de la lista objects (no 0)
    }

    public static class ProductoVH extends RecyclerView.ViewHolder {
        ImageButton btnEliminar, btnEditar;
        TextView txtNombre, txtPrecio, txtCantidad, txtImporteTotal;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            btnEditar = itemView.findViewById(R.id.btnEditarCard);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCard);
            txtNombre = itemView.findViewById(R.id.txtNombreCard);
            txtPrecio = itemView.findViewById(R.id.txtPrecioCard);
            txtCantidad = itemView.findViewById(R.id.txtCantidadCard);
            txtImporteTotal = itemView.findViewById(R.id.txtImporteTotalCard);
        }
    }
}
