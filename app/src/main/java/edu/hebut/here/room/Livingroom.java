package edu.hebut.here.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.hebut.here.R;

public class Livingroom extends Fragment {

    public Livingroom() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.room_livingroom, container, false);
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}