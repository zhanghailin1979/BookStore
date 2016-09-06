package learning.moliying.com.bookstore.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import learning.moliying.com.bookstore.R;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ProgressBarFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_loading);
        return builder.create();
    }
}
