package br.edu.utfpr.deisesales.gerenciadordecompras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.utfpr.deisesales.gerenciadordecompras.modelo.Item;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<Item> listaItens;

    public ItemAdapter(Context context, List<Item> listaItens) {
        this.context = context;
        this.listaItens = listaItens;
    }

    @Override
    public int getCount() {
        return listaItens.size();
    }

    @Override
    public Object getItem(int position) {
        return listaItens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // se for null cria o viewholder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.linha_lista_itens, parent, false);
            holder = new ViewHolder();
            holder.textViewValorNome = convertView.findViewById(R.id.textViewValorNome);
            holder.textViewQuantidade = convertView.findViewById(R.id.textViewQuantidade);
            holder.textViewNumeroValorunitario = convertView.findViewById(R.id.textViewNumeroValorunitario);
            holder.textViewValorCategoria = convertView.findViewById(R.id.textViewValorCategoria);
            holder.textViewValorFormaDeMedida = convertView.findViewById(R.id.textViewValorFormaDeMedida);
            holder.textViewValorIsPerecivel = convertView.findViewById(R.id.textViewValorIsPerecivel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // com o view holder construído eu passo os valores dos campos da tela que estão na lista
        Item item = listaItens.get(position);

        holder.textViewValorNome.setText(item.getNome());
        holder.textViewQuantidade.setText(String.valueOf(item.getQuantidade()));
        holder.textViewNumeroValorunitario.setText(String.valueOf(item.getValorUnitario()));
        holder.textViewValorCategoria.setText(item.getCategoria());
        holder.textViewValorFormaDeMedida.setText(item.getFormaDeMedida().toString());
        
        String perecivelStr = item.isPerecivel()
                ? context.getString(R.string.check_box_perecivel_sim)
                : context.getString(R.string.check_box_perecivel_nao);
        holder.textViewValorIsPerecivel.setText(perecivelStr);

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewValorNome;
        TextView textViewQuantidade;
        TextView textViewNumeroValorunitario;
        TextView textViewValorCategoria;
        TextView textViewValorFormaDeMedida;
        TextView textViewValorIsPerecivel;
    }
}
