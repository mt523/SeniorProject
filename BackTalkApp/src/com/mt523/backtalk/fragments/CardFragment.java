package com.mt523.backtalk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.client.CardPacket;
import com.mt523.backtalk.util.FontUtil;

public class CardFragment extends Fragment {

    CardPacket card;

    public CardFragment(CardPacket basePacket) {
        super();
        card = basePacket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.card_layout, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.display);
        tv.setTypeface(FontUtil.instance(getActivity().getApplicationContext())
                .getFont());
        tv.setText(card.getQuestion());
        return rootView;
    }
}