package com.team.mamba.atlascalendar.dependencyInjection.builder;

import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.create_announcement.CreateAnnouncementFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view.FindUsersRecycler;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.EditAddNotePageSixFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.filter_list.CrmFilterSettingsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.selected_crm.SelectedCrmFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business.AddBusinessFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_user.AddUserFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.FindUsersFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts.SuggestedContactsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.locator.LocatorFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.ContactNotesFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note.HowDidYouMeetFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note.PersonalNoteRecyclerView;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_business.BusinessProfileFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.DashBoardModule;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.ContactProfileFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.UserProfileFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_address_info.EditAddressFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.AddEducationFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.EditEducationFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_email_info.EditEmailFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_employer.EditEmployerFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_phone_info.EditPhoneFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.add_new.AddWorkFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work.EditWorkFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.settings.SettingsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.settings.network_management.NetworkManagementFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.WelcomeScreenModule;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.ViewPagerFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.admin_accounts.AdminAccountsFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login.BusinessLoginFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.WelcomeFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number.EnterPhoneNumberFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = WelcomeScreenModule.class)
    abstract ViewPagerFragment bindViewPagerFragment();

    @ContributesAndroidInjector(modules = WelcomeScreenModule.class)
    abstract EnterPhoneNumberFragment bindEnterPhoneNumberFragment();

    @ContributesAndroidInjector(modules = WelcomeScreenModule.class)
    abstract WelcomeFragment bindWelcomeFragment();

    @ContributesAndroidInjector(modules = WelcomeScreenModule.class)
    abstract BusinessLoginFragment bindBusinessLoginFragment();

    @ContributesAndroidInjector(modules = WelcomeScreenModule.class)
    abstract AdminAccountsFragment bindAdminAccountsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract CrmFragment bindBusinessOpportunitiesFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract ContactsFragment bindContactsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract LocatorFragment bindDashBoardHomeFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract AnnouncementsFragment bindNotificationsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract AddUserFragment bindAddContactsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract DescribeConnectionsFragment bindDescribeConnectionsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract AddBusinessFragment bindAddBusinessFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract FindUsersFragment bindFindUsersFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract SuggestedContactsFragment bindSuggestedContactsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract UserProfileFragment bindUserProfileFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract BusinessProfileFragment bindBusinessProfileFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract EditAddressFragment bindEditAddressFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract EditEducationFragment bindEditEducationFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract EditEmailFragment bindEditEmailFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract EditPhoneFragment bindEditPhoneFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract EditWorkFragment bindEditWorkFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract SelectedCrmFragment bindSelectedCrmFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract EditAddNotePageSixFragment bindEditAddNotePageSixFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract CrmFilterSettingsFragment bindCrmFilterSettingsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract FindUsersRecycler bindFindUsersRecycler();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract SettingsFragment bindSettingsFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract ContactProfileFragment bindContactProfileFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract ContactNotesFragment bindContactNotesFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract HowDidYouMeetFragment bindHowDidYouMeetFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract PersonalNoteRecyclerView bindPersonalNoteRecyclerView();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract NetworkManagementFragment bindNetworkManagementFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract AddWorkFragment bindAddWorkFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract EditEmployerFragment bindEditEmployerFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract AddEducationFragment bindAddEducationFragment();

    @ContributesAndroidInjector(modules = DashBoardModule.class)
    abstract CreateAnnouncementFragment bindCreateAnnouncementFragment();

}
