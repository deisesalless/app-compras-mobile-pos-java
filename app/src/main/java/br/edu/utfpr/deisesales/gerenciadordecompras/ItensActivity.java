package br.edu.utfpr.deisesales.gerenciadordecompras;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.utfpr.deisesales.gerenciadordecompras.modelo.FormaDeMedida;
import br.edu.utfpr.deisesales.gerenciadordecompras.modelo.Item;
import br.edu.utfpr.deisesales.gerenciadordecompras.persistencia.ItensDataBase;
import br.edu.utfpr.deisesales.gerenciadordecompras.utils.UtilsAlert;

public class ItensActivity extends AppCompatActivity {

    private ListView listaViewItens;
    private List<Item> listaItens;
    private ItemAdapter itemAdapter;
    public static final String ARQUIVO_PREFERENCIAS = "br.edu.utfpr.deisesales.gerenciadordecompras.PREFERENCIAS";
    public static final String KEY_ORDENACAO_ASCENDENTE = "ORDENACAO_ASCENDENTE";
    public static final boolean PADRAO_INICIAL_ORDENACAO_ASCENDENTE = true;
    private boolean ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    private MenuItem menuItemOrdenacao;
    private int posicaoSelecionada = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens);

        setTitle(getString(R.string.lista_de_itens));

        listaViewItens = findViewById(R.id.listaViewItens);
        listaViewItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) listaViewItens.getItemAtPosition(position);

                Toast.makeText(ItensActivity.this,
                                getString(R.string.itemAlertaMensagem) + item.getNome() +
                                        getString(R.string.selecionadoAlertaMensagem),
                                Toast.LENGTH_SHORT)
                        .show();
            }

        });
        lerPreferencias();
        popularListaItens();

        // Ativa o menu de contexto para a ListView
        registerForContextMenu(listaViewItens);
    }

    ActivityResultLauncher<Intent> launcherNovoItem = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == ItensActivity.RESULT_OK) {
                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {

                            long id = bundle.getLong(ItemActivity.KEY_ID);
                            ItensDataBase dataBase = ItensDataBase.getDatabase(ItensActivity.this);
                            Item item = dataBase.getItemDAO().queryById(id);
                            listaItens.add(item);
                            ordenarLista();
                        }
                    }
                }
            });

    public void abrirNovoItem() {
        Intent intentAbertura = new Intent(this, ItemActivity.class);
        intentAbertura.putExtra(ItemActivity.KEY_MODO, ItemActivity.MODO_NOVO);
        launcherNovoItem.launch(intentAbertura);
    }

    ActivityResultLauncher<Intent> launcherEditarItem = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == ItensActivity.RESULT_OK) {
                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            final int posicaoParaDesfazer = posicaoSelecionada;
                            final Item itemOriginal = listaItens.get(posicaoSelecionada);
                            long id = bundle.getLong(ItemActivity.KEY_ID);

                            final ItensDataBase dataBase = ItensDataBase.getDatabase(ItensActivity.this);
                            final Item itemEditado = dataBase.getItemDAO().queryById(id);

                            if (itemEditado != null) {
                                listaItens.set(posicaoSelecionada, itemEditado);
                                ordenarLista();
                            }

                            final ConstraintLayout constraintLayout = findViewById(R.id.main);
                            Snackbar snackBar = Snackbar.make(constraintLayout,
                                    R.string.dados_editados_com_sucesso,
                                    Snackbar.LENGTH_LONG);

                            snackBar.setAction(R.string.desfazer, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int qtdAlterada = dataBase.getItemDAO().update(itemOriginal);
                                    if (qtdAlterada != 1) {
                                        UtilsAlert.mostrarAviso(ItensActivity.this, R.string.erro_ao_tentar_alterar_item);
                                        return;
                                    }
                                    listaItens.set(posicaoParaDesfazer, itemOriginal);
                                    ordenarLista();
                                }
                            });

                            snackBar.show();
                        }

                        posicaoSelecionada = -1;
                    }
                }
            });

    public void editarItem(int posicao) {
        // atualização da var global
        posicaoSelecionada = posicao;

        Item item = listaItens.get(posicao);
        Intent intentAbertura = new Intent(this, ItemActivity.class);
        intentAbertura.putExtra(ItemActivity.KEY_MODO, ItemActivity.MODO_EDITAR);
        intentAbertura.putExtra(ItemActivity.KEY_ID, item.getId());
        launcherEditarItem.launch(intentAbertura);
    }

    public void abrirSobre() {
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
    }
    public void popularListaItens() {
        ItensDataBase dataBase = ItensDataBase.getDatabase(this);
        if (ordenacaoAscendente) listaItens = dataBase.getItemDAO().queryAllAscending();
        else listaItens = dataBase.getItemDAO().queryAllDescending();

        itemAdapter = new ItemAdapter(this, listaItens);
        listaViewItens.setAdapter(itemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.itens_opcoes, menu);
        menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.menuItemAdicionar) {
            abrirNovoItem();
            return true;

        } else if (item.getItemId() == R.id.menuItemSobre) {
            abrirSobre();
            return true;

        } else if (item.getItemId() == R.id.menuItemOrdenacao) {
            salvarPreferenciasOrdenacaoAscendente(!ordenacaoAscendente);
            atualizarIconeOrdenacao();
            popularListaItens();
            return true;

        } else if (item.getItemId() == R.id.menuItemRestaurar) {
            confirmarRestaurarPadroes();
            return true;

        } else if (item.getItemId() == R.id.menuItemExcluir) {
            excluirItem(info.position);
            return true;

        } else if (item.getItemId() == R.id.menuItemEditar) {
            editarItem(info.position);
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        atualizarIconeOrdenacao();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.itens_item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.menuItemEditar) {
            editarItem(info.position);
            return true;
        } else if (item.getItemId() == R.id.menuItemExcluir) {
            excluirItem(info.position);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
    public void excluirItem(int posicao) {
        // atualização da var global
        posicaoSelecionada = posicao;
        final Item item = listaItens.get(posicao);

        DialogInterface.OnClickListener listenerYes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ItensDataBase dataBase = ItensDataBase.getDatabase(ItensActivity.this);
                int qtdAlterada = dataBase.getItemDAO().delete(item);

                if (qtdAlterada != 1) {
                    UtilsAlert.mostrarAviso(ItensActivity.this, getString(R.string.erro_ao_tentar_excluir_item));
                    return;
                }

                listaItens.remove(posicaoSelecionada);
                itemAdapter.notifyDataSetChanged();
            }
        };

        UtilsAlert.confirmarAcao(this, R.string.mensagem_confirmacao_exclusao, listenerYes, null);
    }
    private void lerPreferencias() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        ordenacaoAscendente = shared.getBoolean(KEY_ORDENACAO_ASCENDENTE, ordenacaoAscendente);
    }
    private void salvarPreferenciasOrdenacaoAscendente(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(ItensActivity.ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(KEY_ORDENACAO_ASCENDENTE, novoValor);
        editor.apply();
        ordenacaoAscendente = novoValor;
    }
    private void ordenarLista() {
        if (ordenacaoAscendente) {
            Collections.sort(listaItens, Item.NOME_ASCENDENTE);
        } else {
            Collections.sort(listaItens, Item.NOME_DESCENDENTE);
        }

        if (itemAdapter != null) {
            itemAdapter.notifyDataSetChanged();
        }
    }
    private void atualizarIconeOrdenacao() {
        if (ordenacaoAscendente) menuItemOrdenacao.setIcon(R.drawable.icon_sort_az);
        else menuItemOrdenacao.setIcon(R.drawable.icon_sort_za);
    }
    private void restaurarPadroes() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.apply();
        ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    }
    private void confirmarRestaurarPadroes() {
        DialogInterface.OnClickListener listenerYes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restaurarPadroes();
                atualizarIconeOrdenacao();
                ordenarLista();
                Toast.makeText(ItensActivity.this, R.string.padroes_restaurados, Toast.LENGTH_SHORT).show();
            }

        };

        UtilsAlert.confirmarAcao(this, getString(R.string.deseja_voltar_padroes), listenerYes, null);
    }
}
