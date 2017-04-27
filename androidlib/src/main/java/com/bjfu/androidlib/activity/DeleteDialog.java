package com.bjfu.androidlib.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.bjfu.androidlib.R;

/**
 * Created by 11827 on 2016/8/4.
 */
public class DeleteDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private String[] items;
    private IDeleteListener deleteListener;
    private ArrayAdapter<String> deleteItemAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        items = getActivity().getResources().getStringArray(R.array.chooseItems);
        deleteItemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_delete, android.R.id.text1, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(deleteItemAdapter, this);
        return builder.create();
    }


    public void setDeleteListener(IDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    private void cancel() {
        getDialog().cancel();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                if (deleteListener != null) deleteListener.delete(true);
            case 1:
                if (deleteListener != null) deleteListener.delete(false);
                cancel();
                break;
        }
    }

    public interface IDeleteListener {
        public void delete(boolean delete);
    }
}
