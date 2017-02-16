package com.julia.android.worderly.ui.game.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.julia.android.worderly.R;

public class RoundFinishedDialogFragment extends DialogFragment {

    public RoundFinishedDialogFragment() {
        // Empty constructor required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static RoundFinishedDialogFragment newInstance(String title) {
        RoundFinishedDialogFragment frag = new RoundFinishedDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_round, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        TextView textView = (TextView) view.findViewById(R.id.txt_example);
        // Fetch arguments from bundle and set title
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