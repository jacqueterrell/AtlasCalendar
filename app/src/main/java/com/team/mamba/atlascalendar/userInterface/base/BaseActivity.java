package com.team.mamba.atlascalendar.userInterface.base;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.utils.NetworkUtils;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public abstract class BaseActivity <T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {


    @Inject
    AppDataManager appDataManager;

    private T mViewDataBinding;
    private V mViewModel;
    private String loadingTag = "BaseActivity";
    private static boolean isIdle = true;
    protected int sdk = Build.VERSION.SDK_INT;
    protected int marshMallow = Build.VERSION_CODES.M;
    protected AppDataManager dataManager;

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

    public T getViewDataBinding() {
        return mViewDataBinding;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        dataManager = appDataManager;
        performDataBinding();
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    protected boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    protected void showToastLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

          protected void showToastShort(String msg) {
              Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
          }


          protected void showSnackBar(String message) {

        Snackbar.make(getViewDataBinding().getRoot(), message, Snackbar.LENGTH_LONG)
                .show();
    }

    protected void showAlertDialog(String title, String body) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(title)
                .setMessage(body)
                .setNegativeButton("Ok", (paramDialogInterface, paramInt) -> {

                });

        if (!isFinishing()) {

            dialog.show();

        }
    }

    protected void showNoDataAlert() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("No Network ConnectionRecord")
                .setMessage("Please connect to a network")
                .setNegativeButton("Ok", (paramDialogInterface, paramInt) -> {

                });

        dialog.show();
    }

    protected void showTooLongAlert() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Whoops!")
                .setMessage("No internet connection found. Check your connection or try again")
                .setNegativeButton("Ok", (paramDialogInterface, paramInt) -> {

                });

        if (!isFinishing()) {

            dialog.show();

        }
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


    protected void setIdlingResource(boolean idle, String tag) {

        if (BuildConfig.DEBUG){
            isIdle = idle;
            loadingTag = tag;
        }
    }

}
