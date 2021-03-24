package edu.hebut.here.ui.pack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PackViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PackViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("打包");
    }

    public LiveData<String> getText() {
        return mText;
    }
}