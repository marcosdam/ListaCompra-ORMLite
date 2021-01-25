package com.marcosledesma.listacompra_ormlite.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.marcosledesma.listacompra_ormlite.configuraciones.Configuracion;
import com.marcosledesma.listacompra_ormlite.modelos.Producto;

import java.sql.SQLException;

// Necesita heredar de la librería ORM Helper
public class ProductosHelper extends OrmLiteSqliteOpenHelper {

    // Atributo DAO que va a trabajar sobre la clase Producto y su PK será int
    private Dao<Producto, Integer> daoProducto;
    // necesario su get
    public Dao<Producto, Integer> getDaoProducto() throws SQLException {
        if (daoProducto == null){
            daoProducto = this.getDao(Producto.class);
        }
        return  daoProducto;
    }

    // Modificamos el const super y dejamos solo en context como argumento, y factory a null
    public ProductosHelper(Context context) {
        super(context, Configuracion.DB_NAME, null, Configuracion.DB_VERSION);
    }

    // Obligatorio implementar onCreate & onUpgrade
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        // Crea la tabla en base a la clase Ordenador
        try {
            TableUtils.createTable(connectionSource, Producto.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Cuando cambiemos la Base de Datos (añadir campos -> precio, pantalla, color)
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // Para cumplir con todos los cambios y solucionar errores en usuarios que pasen
        // de la versión 1 a la 3, la 2 a la 5 etc
        if (oldVersion < 3){
            try {
                getDaoProducto().executeRaw("alter table productos add column descuento float not null default 0");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
