package com.example.mobileterm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class EmailDialog extends DialogFragment {
    private MyDialogListener myListener;
    public interface MyDialogListener {
         public void myCallback(String newemail);

    }
    public EmailDialog() {
    }
    //LetterWriteFragment에 데이터를 넘겨주기 위한 인터페이스
    public interface MyFragmentInterfacer {
        void onButtonClick(String input);
    }
    private MyFragmentInterfacer fragmentInterfacer;

    public void setFragmentInterfacer(MyFragmentInterfacer fragmentInterfacer){
        this.fragmentInterfacer = fragmentInterfacer;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            myListener = (MyDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        builder.setView(inflater.inflate(R.layout.dialog_email, null))
                .setPositiveButton("인증메일 받기", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText changeemail = (EditText)getDialog().findViewById(R.id.dialog_email_edittext);
                        String newemail = changeemail.getText().toString();
                        myListener. myCallback(newemail);

                    }

                });


        return builder.create();

    }
//    private Fragment fragment;
//
//    public void FragmentDialog() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_email, container, false);
//
////        Bundle args = getArguments();
////        String value = args.getString("key");
//
//
//        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
//
//        if (fragment != null) {
//            DialogFragment dialogFragment = (DialogFragment) fragment;
//            dialogFragment.dismiss();
//        }
//        return view;
//    }
}
