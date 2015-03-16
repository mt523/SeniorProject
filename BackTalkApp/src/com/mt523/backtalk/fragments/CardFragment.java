package com.mt523.backtalk.fragments;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.fragments.GuessFragment.GuessInterface;
import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.util.FontUtil;

public class CardFragment extends Fragment implements GuessInterface {

    private CardInterface activity;
    private Card card;
    private TextView tv;

    public static CardFragment newCard(Card card) {
        CardFragment cardFragment = new CardFragment();
        cardFragment.card = card;
        return cardFragment;
    }

    private CardFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.card_layout, container, false);
        // activity = (CardInterface) getActivity();
        tv = (TextView) rootView.findViewById(R.id.display);
        tv.setTypeface(FontUtil.instance(getActivity().getApplicationContext())
                .getFont());
        tv.setText("\"" + card.getQuestion() + "\"");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (CardInterface) activity;
    }

    @Override
    public void guess(String guess) {
        // Toast.makeText(
        // getActivity().getApplicationContext(),
        // normalize(guess).equals(normalize(card.getAnswer())) ? "Match"
        // : "No match", Toast.LENGTH_SHORT).show();
    }

    private String normalize(String s) {
        return s.replaceAll("\\W", "").toUpperCase(Locale.ENGLISH);
    }

    public interface CardInterface {

        public void goToCard(int index);

        public int getIndex();
    }
}
