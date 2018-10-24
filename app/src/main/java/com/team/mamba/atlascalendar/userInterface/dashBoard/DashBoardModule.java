package com.team.mamba.atlascalendar.userInterface.dashBoard;


import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.create_announcement.CreateAnnouncementViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view.FindUsersRecyclerViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.EditAddNotePageSixViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.filter_list.CrmFilterViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.selected_crm.SelectedCrmViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business.AddBusinessViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_user.AddUserViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.FindUsersViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts.SuggestedContactsViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.ContactNotesViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note.HowDidYouMeetViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note.PersonalNotesViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_business.BusinessProfileViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.info.InfoViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.ContactProfileViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.UserProfileViewModel;

import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_address_info.EditAddressViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.AddEducationViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.EditEducationViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_email_info.EditEmailViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_employer.EditEmployerViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_phone_info.EditPhoneViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.add_new.AddWorkViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work.EditWorkViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.settings.SettingsViewModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.settings.network_management.NetworkManagementViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class DashBoardModule {

    @Provides
    CrmViewModel providesBusinessOpportunitiesViewModel(){

        return new CrmViewModel();
    }

    @Provides
    DashBoardActivityViewModel providesDashBoardActivityViewModel(){

        return new DashBoardActivityViewModel();
    }

    @Provides
    ContactsViewModel providesContactsViewModel(){

        return new ContactsViewModel();
    }

    @Provides
    InfoViewModel providesDashBoardHomeViewModel(){

        return new InfoViewModel();
    }

    @Provides
    AnnouncementsViewModel providesNotificationsViewModel(){

        return new AnnouncementsViewModel();
    }

    @Provides
    AddUserViewModel providesAddContactViewModel(){

        return new AddUserViewModel();
    }

    @Provides
    DescribeConnectionsViewModel providesDescribeConnectionsViewModel(){

        return new DescribeConnectionsViewModel();
    }

    @Provides
    AddBusinessViewModel providesAddBusinessViewModel(){

        return new AddBusinessViewModel();
    }

    @Provides
    FindUsersViewModel providesFindUsersViewModel(){

        return new FindUsersViewModel();
    }

    @Provides
    SuggestedContactsViewModel providesSuggestedContactsViewModel(){

        return new SuggestedContactsViewModel();
    }

    @Provides
    UserProfileViewModel providesUserProfileViewModel(){

        return new UserProfileViewModel();
    }

    @Provides
    BusinessProfileViewModel providesBusinessProfileViewModel(){

        return new BusinessProfileViewModel();
    }

    @Provides
    EditAddressViewModel providesEditAddressViewModel(){

        return new EditAddressViewModel();
    }

    @Provides
    EditEducationViewModel providesEditEducationViewModel(){

        return new EditEducationViewModel();
    }

    @Provides
    EditEmployerViewModel providesEditEmployerViewModel(){

        return new EditEmployerViewModel();
    }

    @Provides
    EditEmailViewModel providesEditEmailViewModel(){

        return new EditEmailViewModel();
    }

    @Provides
    EditPhoneViewModel providesEditPhoneViewModel(){

        return new EditPhoneViewModel();
    }

    @Provides
    EditWorkViewModel providesEditWorkViewModel(){

        return new EditWorkViewModel();
    }

    @Provides
    SelectedCrmViewModel providesSelectedCrmViewModel(){

        return new SelectedCrmViewModel();
    }

    @Provides
    EditAddNotePageSixViewModel providesEditAddNotePageSixViewModel(){

        return new EditAddNotePageSixViewModel();
    }

    @Provides
    CrmFilterViewModel providesCrmFilterViewModel(){

        return new CrmFilterViewModel();
    }

    @Provides
    FindUsersRecyclerViewModel providesRecyclerViewModel(){

        return new FindUsersRecyclerViewModel();
    }

    @Provides
    SettingsViewModel providesSettingsViewModel(){

        return new SettingsViewModel();
    }

    @Provides
    ContactProfileViewModel providesContactProfileViewModel(){

        return new ContactProfileViewModel();
    }

    @Provides
    ContactNotesViewModel providesContactNotesViewModel(){

        return new ContactNotesViewModel();
    }

    @Provides
    HowDidYouMeetViewModel providesHowDidYouMeetViewModel(){

        return new HowDidYouMeetViewModel();
    }

    @Provides
    PersonalNotesViewModel providesPersonalNotesViewModel(){

        return new PersonalNotesViewModel();
    }

    @Provides
    NetworkManagementViewModel providesNetworkManagementViewModel(){

        return new NetworkManagementViewModel();
    }

    @Provides
    AddWorkViewModel providesAddWorkViewModel(){

        return new AddWorkViewModel();
    }


    @Provides
    AddEducationViewModel providesAddEducationViewModel(){

        return new AddEducationViewModel();
    }

    @Provides
    CreateAnnouncementViewModel providesCreateAnnouncementViewModel(){

        return new CreateAnnouncementViewModel();
    }

}
