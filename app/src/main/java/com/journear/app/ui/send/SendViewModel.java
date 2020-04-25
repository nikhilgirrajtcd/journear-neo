package com.journear.app.ui.send;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.journear.app.core.services.JnMessage;

import java.util.List;

public class    SendViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<JnMessage>> messages;

    public SendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}