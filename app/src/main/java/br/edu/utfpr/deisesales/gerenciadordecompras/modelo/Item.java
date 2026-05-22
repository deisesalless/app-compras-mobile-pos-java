package br.edu.utfpr.deisesales.gerenciadordecompras.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.Objects;

@Entity
public class Item implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    @ColumnInfo(index = true)
    private String nome;
    private int quantidade;
    private double valorUnitario;
    @NonNull
    private String categoria;
    @NonNull
    private FormaDeMedida formaDeMedida;
    private boolean isPerecivel;

    public static final Comparator<Item> NOME_ASCENDENTE = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            if (o1 == null || o2 == null) return 0;
            return o1.getNome().compareToIgnoreCase(o2.getNome());
        }
    };

    public static final Comparator<Item> NOME_DESCENDENTE = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return -1 * o2.getNome().compareToIgnoreCase(o1.getNome());
        }
    };

    public Item(String nome, int quantidade, double valorUnitario, String categoria, FormaDeMedida formaDeMedida, boolean isPerecivel) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.categoria = categoria;
        this.formaDeMedida = formaDeMedida;
        this.isPerecivel = isPerecivel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public FormaDeMedida getFormaDeMedida() {
        return formaDeMedida;
    }

    public void setFormaDeMedida(FormaDeMedida formaDeMedida) {
        this.formaDeMedida = formaDeMedida;
    }

    public boolean isPerecivel() {
        return isPerecivel;
    }

    public void setPerecivel(boolean perecivel) {
        isPerecivel = perecivel;
    }

    @Override
    public String toString() {
        return nome + "\n" + quantidade + "\n" + valorUnitario + "\n" + categoria + "\n" + formaDeMedida + "\n" + isPerecivel;
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        Item clone = (Item) super.clone();
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return quantidade == item.quantidade && Double.compare(valorUnitario,
                item.valorUnitario) == 0 && isPerecivel == item.isPerecivel &&
                nome.equals(item.nome) && Objects.equals(categoria, item.categoria)
                && formaDeMedida == item.formaDeMedida;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, quantidade, valorUnitario, categoria, formaDeMedida, isPerecivel);
    }
}
