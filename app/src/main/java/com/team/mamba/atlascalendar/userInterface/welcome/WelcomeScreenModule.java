package com.team.mamba.atlascalendar.userInterface.welcome;


import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivityViewModel;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.ViewPagerViewModel;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.admin_accounts.AdminAccountsViewModel;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login.BusinessLoginViewModel;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.WelcomeViewModel;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_accounts_recycler.BusinessAccountsViewModel;

import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number.EnterPhoneViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class WelcomeScreenModule {

    @Provides
    ViewPagerViewModel providesViewPagerViewModel(){

        return new ViewPagerViewModel();
    }

    @Provides
    WelcomeViewModel providesWelcomeViewModel(){

        return new WelcomeViewModel();
    }

    @Provides
    BusinessAccountsViewModel providesBusinessAccountsViewModel(){

        return new BusinessAccountsViewModel();
    }

    @Provides
    EnterPhoneViewModel providesEnterPhoneViewModel(){

        return new EnterPhoneViewModel();
    }

    @Provides
    WelcomeActivityViewModel providesWelcomeActivityViewModel(){

        return new WelcomeActivityViewModel();
    }

    @Provides
    BusinessLoginViewModel providesBusinessLoginViewModel(){

        return new BusinessLoginViewModel();
    }

    @Provides
    AdminAccountsViewModel providesAdminAccountsViewModel(){

        return new AdminAccountsViewModel();
    }
}
