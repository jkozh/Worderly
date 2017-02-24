/*
* Copyright 2017 Julia Kozhukhovskaya
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.julia.android.worderly.ui.game.view;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.julia.android.worderly.R;
import com.julia.android.worderly.model.Player;
import com.julia.android.worderly.model.Round;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GameRoundDialogFragment extends DialogFragment {

    @BindView(R.id.text_word) TextView mWordView;
    @BindView(R.id.text_score) TextView mScoreView;
    @BindView(R.id.text_definition) TextView mDefinitionView;
    @BindView(R.id.text_score_game_user1) TextView mUser1ScoreGameView;
    @BindView(R.id.text_username_user1) TextView mUsernameUser1View;
    @BindView(R.id.text_word_user1) TextView mWordUser1View;
    @BindView(R.id.text_score_word_user1) TextView mScoreWordUser1View;
    @BindView(R.id.text_score_game_user2) TextView mUser2ScoreGameView;
    @BindView(R.id.text_username_user2) TextView mUsernameUser2View;
    @BindView(R.id.text_word_user2) TextView mWordUser2View;
    @BindView(R.id.text_score_word_user2) TextView mScoreWordUser2View;


    public GameRoundDialogFragment() {
        // Empty constructor required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead
    }


    public static GameRoundDialogFragment newInstance(Round round, Player user1, Player user2) {
        GameRoundDialogFragment frag = new GameRoundDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("round", round);
        args.putParcelable("user1", user1);
        args.putParcelable("user2", user2);
        frag.setArguments(args);
        return frag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Fetch arguments from bundle
        Round round = getArguments().getParcelable("round");
        Player user1 = getArguments().getParcelable("user1");
        Player user2 = getArguments().getParcelable("user2");

        alertDialogBuilder.setTitle(String.valueOf(round.getRoundNumber()));
        View v = View.inflate(getContext(), R.layout.fragment_dialog_round_finished, null);
        ButterKnife.bind(this, v);

        mWordView.setText(round.getWord());
        mScoreView.setText(String.valueOf(round.getWordScore()));
        mDefinitionView.setText(round.getDefinition());

        mUser1ScoreGameView.setText(String.valueOf(user1.getGameScore()));
        mUsernameUser1View.setText(user1.getUsername());
        mWordUser1View.setText(user1.getWord());
        mScoreWordUser1View.setText(String.valueOf(user1.getWordScore()));

        mUser2ScoreGameView.setText(String.valueOf(user2.getGameScore()));
        mUsernameUser2View.setText(user2.getUsername());
        mWordUser2View.setText(user2.getWord());
        mScoreWordUser2View.setText(String.valueOf(user2.getWordScore()));

        alertDialogBuilder.setView(v);
        return alertDialogBuilder.create();
    }

}