package br.edu.utfpr.deisesales.gerenciadordecompras.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import br.edu.utfpr.deisesales.gerenciadordecompras.modelo.Item;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class ItensDataBase extends RoomDatabase {

    public abstract ItemDAO getItemDAO();
    private static ItensDataBase INSTANCE;
    public static ItensDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ItensDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, ItensDataBase.class, "itens.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
