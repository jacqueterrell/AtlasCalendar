package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.add_new;

import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.googlePlaces.Prediction;
import com.team.mamba.atlascalendar.data.model.api.googlePlaces.request.AutoCompleteRequest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddWorkDataModel {

    private AppDataManager dataManager;


    @Inject
    public AddWorkDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void getGooglePlaceHints(AddWorkViewModel viewModel,String text){


        AutoCompleteRequest request = new AutoCompleteRequest(text);
        List<String> descriptionList = new ArrayList<>();

        viewModel.getCompositeDisposable().add(dataManager.getGooglePlacesApiEndPoint()
        .requestGoogleAutoComplete(request.getRequestMap())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(googleLocations -> {


            if (googleLocations == null || googleLocations.getPredictions() == null){


            } else {

                List<Prediction> predictionList = googleLocations.getPredictions();

                for (Prediction prediction: predictionList){

                    descriptionList.add(prediction.getDescription());
                }

                viewModel.getNavigator().onAutoCompleteReturned(descriptionList);
            }

        },throwable -> {

            viewModel.getNavigator().handleError(throwable.getMessage());
            Logger.e(throwable.getLocalizedMessage());

        }));

    }
}
