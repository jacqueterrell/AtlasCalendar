package com.team.mamba.atlascalendar.userInterface.welcome._viewPager;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.WelcomeViewPagerBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.howYouMet.RememberHowYouMetFragment;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.latestNews.LatestNewsFragment;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.mobileCrm.MobileCrmFragment;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.upToDate.UpToDateFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.WelcomeFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.WelcomeNavigator;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewPagerFragment extends BaseFragment<WelcomeViewPagerBinding, ViewPagerViewModel>
        implements ViewPagerNavigator {

    @Inject
    ViewPagerViewModel viewModel;


    private static final int SCROLL_ACTION_STOPPED = 0;
    private WelcomeViewPagerBinding binding;
    private WelcomePager welcomePager;
    private int currentPage;

    public static ViewPagerFragment newInstance() {

        return new ViewPagerFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.welcome_view_pager;
    }

    @Override
    public ViewPagerViewModel getViewModel() {
        return viewModel;
    }


    @Override
    public View getProgressSpinner() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);

        getDeviceToken();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = getViewDataBinding();

        if (dataManager.getSharedPrefs().isUserLoggedIn()) {

            getBaseActivity().finishAffinity();
            startActivity(DashBoardActivity.newIntent(getBaseActivity()));
        }

        welcomePager = new WelcomePager(getBaseActivity().getSupportFragmentManager());
        binding.viewPagerWelcome.setAdapter(welcomePager);

        return binding.getRoot();
    }

    private class WelcomePager extends FragmentStatePagerAdapter {


        public WelcomePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {

                return WelcomeFragment.newInstance();

            } else if (position == 1) {

                return UpToDateFragment.newInstance();

            } else if (position == 2) {

                return RememberHowYouMetFragment.newInstance();

            } else if (position == 3) {

                return MobileCrmFragment.newInstance();
            } else if (position == 4) {

                return LatestNewsFragment.newInstance();
            } else if (position == 5) {

                return WelcomeFragment.newInstance();

            } else {

                return WelcomeFragment.newInstance();

            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            ((ViewPager) container).addOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

//                        if (currentPage ==0){
//
//                            ((ViewPager)container).setCurrentItem(5,false);
//                        }

                    if (currentPage == 5) {
                        ((ViewPager) container).setCurrentItem(0, false);
                    }
                }
            });

            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public void onKeyDown() {

        if (binding.viewPagerWelcome.getCurrentItem() == 0) {

            Fragment fragment = (Fragment) binding.viewPagerWelcome
                    .getAdapter()
                    .instantiateItem(binding.viewPagerWelcome, 0);

            if (fragment instanceof WelcomeFragment) {

                WelcomeNavigator navigator = (WelcomeNavigator) fragment;
                navigator.onBackPressed();
            }

        } else {

            getBaseActivity().onBackPressed();
        }

    }


    private void getDeviceToken() {

        Completable.fromCallable(() -> {

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {

                String token = instanceIdResult.getToken();
                dataManager.getSharedPrefs().setFirebaseDeviceToken(token);

            });

            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}


