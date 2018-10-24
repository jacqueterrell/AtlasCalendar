package com.team.mamba.atlascalendar.userInterface.base;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.team.mamba.atlascalendar.data.AppDataManager;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {


    @Inject
    AppDataManager appDataManager;

    protected AppDataManager dataManager;
    protected int sdk = Build.VERSION.SDK_INT;
    protected int marshMallow = Build.VERSION_CODES.M;
    private BaseActivity mActivity;
    private View mRootView;
    private T mViewDataBinding;
    private V mViewModel;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    public abstract View getProgressSpinner();

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        dataManager = appDataManager;
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {

        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }


    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    protected void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    protected void showToastLong(String msg) {

        if (getBaseActivity() != null) {

            Toast.makeText(getBaseActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }

    protected void showToastShort(String msg) {

        if (getBaseActivity() != null) {

            Toast.makeText(getBaseActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showSnackbar(String message) {

        Snackbar.make(this.getViewDataBinding().getRoot(), message, Snackbar.LENGTH_LONG)
                .show();
    }

    protected void showProgressSpinner() {

        if (getProgressSpinner() != null) {

            getProgressSpinner().setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgressSpinner() {

        if (getProgressSpinner() != null) {

            getProgressSpinner().setVisibility(View.GONE);
        }
    }

    protected void showAlert(String title, String body) {

        if (getBaseActivity() == null) {

            return;
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(title)
                .setMessage(body)
                .setNegativeButton("Ok", (paramDialogInterface, paramInt) -> {

                });

        dialog.show();
    }

    protected void showNoDataAlert() {

        if (getBaseActivity() == null) {

            return;
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle("Whoops!")
                .setMessage("No internet connection found. Check your connection or try again")
                .setNegativeButton("Ok", (paramDialogInterface, paramInt) -> {

                });

        dialog.show();
    }


    protected void showTooLongAlert() {

        if (getBaseActivity() == null) {

            return;
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle("Long Request")
                .setMessage("This request is taking way too long, please try again later.")
                .setNegativeButton("Ok", (paramDialogInterface, paramInt) -> {

                });

        dialog.show();
    }




    protected void setIdlingResource(boolean isIdle, String tag) {

        getBaseActivity().setIdlingResource(isDetached(),tag);
    }
}
