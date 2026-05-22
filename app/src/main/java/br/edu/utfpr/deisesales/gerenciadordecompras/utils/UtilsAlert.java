package br.edu.utfpr.deisesales.gerenciadordecompras.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import br.edu.utfpr.deisesales.gerenciadordecompras.R;

public final class UtilsAlert {
    private UtilsAlert() {} // para evitar que a classe seja instanciada

    public static void mostrarAviso(Context context, int idMensagem) {
        mostrarAviso(context, context.getString(idMensagem), null);
    }

    public static void mostrarAviso(Context context, String mensagem) {
        mostrarAviso(context, mensagem, null);
    }

    public static void mostrarAviso(Context context, int idMensagem, DialogInterface.OnClickListener listener) {
        mostrarAviso(context, context.getString(idMensagem), listener);
    }

    public static void mostrarAviso(Context context, String mensagem, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.confirmacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(mensagem);
        builder.setNeutralButton(R.string.ok, listener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void confirmarAcao(Context context, int idMensagem,
                                     DialogInterface.OnClickListener listenerYes,
                                     DialogInterface.OnClickListener listenerNo) {
        confirmarAcao(context, context.getString(idMensagem), listenerYes, listenerNo);
    }

    public static void confirmarAcao(Context context, String mensagem,
                                     DialogInterface.OnClickListener listenerYes,
                                     DialogInterface.OnClickListener listenerNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.confirmacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(mensagem);
        builder.setPositiveButton(R.string.sim, listenerYes);
        builder.setNegativeButton(R.string.nao, listenerNo);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
