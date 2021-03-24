package edu.hebut.here.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import edu.hebut.here.R;

public class Toilet extends Fragment {

    public Toilet() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.room_toilet, container, false);
    }
}