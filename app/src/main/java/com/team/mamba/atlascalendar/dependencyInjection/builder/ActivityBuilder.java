package com.team.mamba.atlascalendar.dependencyInjection.builder;

import com.team.mamba.atlascalendar.userInterface.dashBoard.DashBoardModule;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivity;
import com.team.mamba.atlascalendar.userInterface.welcome.WelcomeScreenModule;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_accounts_recycler.BusinessAccountsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = WelcomeScreenModule.class)
    abstract WelcomeActivity bindWelcomeActivity();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract DashBoardActivity bindDashBoardActivity();

    @ContributesAndroidInjector(modules = WelcomeScreenModule.class)
    abstract BusinessAccountsActivity bindBusinessAccountsActivity();
}
