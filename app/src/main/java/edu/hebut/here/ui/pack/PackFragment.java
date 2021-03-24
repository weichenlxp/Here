package edu.hebut.here.ui.pack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.hebut.here.R;

public class PackFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pack, container, false);
        TextView textView = root.findViewById(R.id.text_pack);
        textView.setText("打包");
        return root;
    }
}