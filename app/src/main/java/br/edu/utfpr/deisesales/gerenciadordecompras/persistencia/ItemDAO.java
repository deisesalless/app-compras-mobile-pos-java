package br.edu.utfpr.deisesales.gerenciadordecompras.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.deisesales.gerenciadordecompras.modelo.Item;

@Dao
public interface ItemDAO {
    @Insert
    long insert(Item item);

    @Delete
    int delete(Item item);

    @Update
    int update(Item item);

    @Query("SELECT * FROM Item WHERE id = :id")
    Item queryById(long id);

    @Query("SELECT * FROM Item")
    List<Item> queryAll();

    @Query("SELECT * FROM Item ORDER BY nome ASC")
    List<Item> queryAllAscending();

    @Query("SELECT * FROM Item ORDER BY nome DESC")
    List<Item> queryAllDescending();
}
