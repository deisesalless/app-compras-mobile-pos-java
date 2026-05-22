package br.edu.utfpr.deisesales.gerenciadordecompras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import br.edu.utfpr.deisesales.gerenciadordecompras.modelo.FormaDeMedida;
import br.edu.utfpr.deisesales.gerenciadordecompras.modelo.Item;
import br.edu.utfpr.deisesales.gerenciadordecompras.persistencia.ItensDataBase;
import br.edu.utfpr.deisesales.gerenciadordecompras.utils.UtilsAlert;

public class ItemActivity extends AppCompatActivity {

    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_MODO = "KEY_MODO";
    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;
    public static final String KEY_SUGERIR_TIPO = "SUGERIR_TIPO";
    public static final String KEY_ULTIMO_TIPO = "ULTIMO_TIPO";
    private EditText editTextCampoNomeItem;
    private EditText editTextNumberQuantidade;
    private EditText editTextNumberDecimalValorUnitario;
    private Spinner spinnerCategoriaItem;
    private RadioGroup radioGroupItem;
    private CheckBox checkBoxItemPerecivel;
    private int modo;
    private boolean sugerirTipo = false;
    private int ultimoTipo = 0;
    private Item itemOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        editTextCampoNomeItem = findViewById(R.id.editTextCampoNomeItem);
        editTextNumberQuantidade = findViewById(R.id.editTextNumberQuantidade);
        editTextNumberDecimalValorUnitario = findViewById(R.id.editTextNumberDecimalValorUnitario);
        spinnerCategoriaItem = findViewById(R.id.spinnerCategoriaItem);
        radioGroupItem = findViewById(R.id.radioGroupItem);
        checkBoxItemPerecivel = findViewById(R.id.checkBoxItemPerecivel);

        lerPreferencias();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            modo = bundle.getInt(KEY_MODO, MODO_NOVO);

            if (modo == MODO_EDITAR) {
                setTitle(getString(R.string.text_view_titulo_editar_item));

                long id = bundle.getLong(KEY_ID);
                ItensDataBase database = ItensDataBase.getDatabase(this);
                itemOriginal = database.getItemDAO().queryById(id);

                if (itemOriginal != null) {
                    editTextCampoNomeItem.setText(itemOriginal.getNome());
                    editTextNumberQuantidade.setText(String.valueOf(itemOriginal.getQuantidade()));
                    editTextNumberDecimalValorUnitario.setText(String.valueOf(itemOriginal.getValorUnitario()));
                    checkBoxItemPerecivel.setChecked(itemOriginal.isPerecivel());

                    if (itemOriginal.getFormaDeMedida() == FormaDeMedida.Kg) {
                        radioGroupItem.check(R.id.radioButtonKg);
                    } else if (itemOriginal.getFormaDeMedida() == FormaDeMedida.Unidade) {
                        radioGroupItem.check(R.id.radioButtonUnidade);
                    }

                    if (itemOriginal.getCategoria() != null) {
                        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerCategoriaItem.getAdapter();
                        int spinnerPosicao = adapter.getPosition(itemOriginal.getCategoria());
                        spinnerCategoriaItem.setSelection(spinnerPosicao);
                    }

                    editTextCampoNomeItem.requestFocus();
                    editTextCampoNomeItem.setSelection(editTextCampoNomeItem.getText().length());
                }
            } else {
                setTitle(getString(R.string.text_view_titulo));
                if (sugerirTipo) {
                    spinnerCategoriaItem.setSelection(ultimoTipo);
                }
            }
        }
    }

    public void salvarDados() {
        String nome = editTextCampoNomeItem.getText().toString();
        String quantidadeStr = editTextNumberQuantidade.getText().toString();
        String valorStr = editTextNumberDecimalValorUnitario.getText().toString();
        int categoriaPosicao = spinnerCategoriaItem.getSelectedItemPosition();
        int idSelecionado = radioGroupItem.getCheckedRadioButtonId();
        boolean isPerecivel = checkBoxItemPerecivel.isChecked();

        if (nome.trim().isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_nome_vazio);
            editTextCampoNomeItem.requestFocus();
            return;
        }

        if (quantidadeStr.trim().isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_quantidade_vazia);
            editTextNumberQuantidade.requestFocus();
            return;
        }

        if (valorStr.trim().isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.erro_valor_vazio);
            editTextNumberDecimalValorUnitario.requestFocus();
            return;
        }

        if (categoriaPosicao == AdapterView.INVALID_POSITION) {
            UtilsAlert.mostrarAviso(this, R.string.erro_categoria_vazia);
            return;
        }

        FormaDeMedida formaDeMedida;
        if (idSelecionado == R.id.radioButtonKg) {
            formaDeMedida = FormaDeMedida.Kg;
        } else if (idSelecionado == R.id.radioButtonUnidade) {
            formaDeMedida = FormaDeMedida.Unidade;
        } else {
            UtilsAlert.mostrarAviso(this, R.string.erro_unidade_nao_selecionada);
            return;
        }

        int quantidade = Integer.parseInt(quantidadeStr);
        double valor = Double.parseDouble(valorStr);
        String categoria = spinnerCategoriaItem.getSelectedItem().toString();

        Item novoItem = new Item(nome, quantidade, valor, categoria, formaDeMedida, isPerecivel);

        if (novoItem.equals(itemOriginal)) {
            setResult(ItemActivity.RESULT_CANCELED);
            finish();
            return;
        }

        Intent intentResposta = new Intent();
        ItensDataBase database = ItensDataBase.getDatabase(this);
        if (modo == MODO_NOVO) {
            long novoId = database.getItemDAO().insert(novoItem);
            if (novoId <= 0) {
                UtilsAlert.mostrarAviso(this, getString(R.string.erro_ao_tentar_inserir_novo_item));
                return;
            }

            novoItem.setId(novoId);
        } else {
            novoItem.setId(itemOriginal.getId());
            int qtdAlterada = database.getItemDAO().update(novoItem);
            if (qtdAlterada != 1) {
                UtilsAlert.mostrarAviso(this, getString(R.string.erro_ao_tentar_alterar_item));
                return;
            }
        }

        if (modo == MODO_NOVO)
            Toast.makeText(this, R.string.dados_salvos_sucesso, Toast.LENGTH_LONG).show();

        salvarUltimoTipo(categoriaPosicao);
        intentResposta.putExtra(KEY_ID, novoItem.getId());
        setResult(ItemActivity.RESULT_OK, intentResposta);
        finish();
    }

    public void limparCampos() {
        final String nome = editTextCampoNomeItem.getText().toString();
        final String quantidade = editTextNumberQuantidade.getText().toString();
        final String valor = editTextNumberDecimalValorUnitario.getText().toString();
        final int categoria = spinnerCategoriaItem.getSelectedItemPosition();
        final int formaDeMedida = radioGroupItem.getCheckedRadioButtonId();
        final boolean isPerecivel = checkBoxItemPerecivel.isChecked();

        final ScrollView scrollView = findViewById(R.id.main);

        editTextCampoNomeItem.setText(null);
        editTextNumberQuantidade.setText(null);
        editTextNumberDecimalValorUnitario.setText(null);
        spinnerCategoriaItem.setSelection(0);
        radioGroupItem.clearCheck();
        checkBoxItemPerecivel.setChecked(false);
        editTextCampoNomeItem.requestFocus();

        Snackbar snackbar = Snackbar.make(scrollView, R.string.campos_apagados_com_sucesso, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.desfazer, v -> {
            editTextCampoNomeItem.setText(nome);
            editTextNumberQuantidade.setText(quantidade);
            editTextNumberDecimalValorUnitario.setText(valor);
            spinnerCategoriaItem.setSelection(categoria);
            radioGroupItem.check(formaDeMedida);
            checkBoxItemPerecivel.setChecked(isPerecivel);
            editTextCampoNomeItem.requestFocus();
        });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuItemSugerirTipo);
        if (item != null) {
            item.setChecked(sugerirTipo);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemSalvar) {
            salvarDados();
            return true;
        } else if (item.getItemId() == R.id.menuItemLimpar) {
            limparCampos();
            return true;
        } else if (item.getItemId() == R.id.menuItemSugerirTipo) {
            boolean novoValor = !item.isChecked();
            salvarSugerirTipo(novoValor);
            item.setChecked(novoValor);

            if (sugerirTipo) {
                spinnerCategoriaItem.setSelection(ultimoTipo);
            } else {
                spinnerCategoriaItem.setSelection(0);
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void lerPreferencias() {
        SharedPreferences shared = getSharedPreferences(ItensActivity.ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        sugerirTipo = shared.getBoolean(KEY_SUGERIR_TIPO, false);
        ultimoTipo = shared.getInt(KEY_ULTIMO_TIPO, 0);
    }

    private void salvarSugerirTipo(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(ItensActivity.ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(KEY_SUGERIR_TIPO, novoValor);
        editor.apply();
        sugerirTipo = novoValor;
    }

    private void salvarUltimoTipo(int novoValor) {
        SharedPreferences shared = getSharedPreferences(ItensActivity.ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(KEY_ULTIMO_TIPO, novoValor);
        editor.apply();
        ultimoTipo = novoValor;
    }
}
