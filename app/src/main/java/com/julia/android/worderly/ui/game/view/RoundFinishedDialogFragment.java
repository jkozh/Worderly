package com.julia.android.worderly.ui.game.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

// TODO: Make it work
public class RoundFinishedDialogFragment extends DialogFragment {

    public RoundFinishedDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static RoundFinishedDialogFragment newInstance(String title) {
        RoundFinishedDialogFragment frag = new RoundFinishedDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}


/*
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
if (isWon) {
    builder.setTitle(getString(R.string.title_you_won));
    builder.setMessage(getString(R.string.msg_good_game));
} else {
    builder.setTitle(getString(R.string.title_you_lose));
    builder.setMessage(getString(R.string.msg_opponent_faster));
    builder.setMessage(getString(R.string.msg_word_was, word));
    String prefs = mPrefs.getString(PREF_WORDS_FOR_LEARNING, Constants.PREF_USER_DEFAULT_VALUE);
    if (!Objects.equals(prefs, Constants.PREF_USER_DEFAULT_VALUE)) {
        mPrefs.edit().putString(PREF_WORDS_FOR_LEARNING, prefs + word + ",").apply();
    } else {
        mPrefs.edit().putString(PREF_WORDS_FOR_LEARNING, word + ",").apply();
    }
    // Updating the widget
    updateWidget();
}

builder.setPositiveButton(getString(R.string.action_ok),
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteWord();
                navigateToMainActivity();
            }
        });
final AlertDialog dialog = builder.create();
dialog.setCanceledOnTouchOutside(false);
dialog.show();
 */