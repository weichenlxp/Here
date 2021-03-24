package edu.hebut.here.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import edu.hebut.here.R;

public class SideBedroom extends Fragment {

    public SideBedroom() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.room_side_bedroom, container, false);
    }
}