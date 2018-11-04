package com.team.mamba.atlascalendar.userInterface.base;

import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableBoolean;
import com.team.mamba.atlascalendar.data.AppDataManager;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel<N> extends ViewModel {

    private boolean mIsLoading = false;
    private CompositeDisposable mCompositeDisposable;
    private N mNavigator;
    private AppDataManager dataManager;


    public BaseViewModel(){

        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();

    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }

    public N getNavigator() {
        return mNavigator;
    }

    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }
}
