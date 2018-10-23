package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.UserProfileLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;

import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_address_info.EditAddressFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.EditEducationFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_email_info.EditEmailFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_employer.EditEmployerFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_phone_info.EditPhoneFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work.EditWorkFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.io.File;
import javax.inject.Inject;

public class UserProfileFragment extends BaseFragment<UserProfileLayoutBinding,UserProfileViewModel>
        implements UserProfileNavigator {


    @Inject
    UserProfileViewModel viewModel;

    @Inject
    UserProfileDataModel dataModel;

    private UserProfileLayoutBinding binding;
    private DashBoardActivityNavigator parentNavigator;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;
    private File imageFile;


    public static UserProfileFragment newInstance(){

        return new UserProfileFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_profile_layout;
    }

    @Override
    public UserProfileViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentNavigator = (DashBoardActivityNavigator) context;
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = getViewDataBinding();

                viewModel.updateUserDetails(getViewModel());

         return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.setProfile(viewModel.getUserProfile());
        setUserDefaultValues();
    }


    private void setUserDefaultValues(){

        if (viewModel.getUserProfile().getImageUrl() != null){

            try {
                Glide.with(getBaseActivity())
                        .load(viewModel.getUserProfile().getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(binding.ivUserProfile);
            } catch (Exception e){

                Logger.e(e.getMessage());
            }
        }

        if (binding.tvCurrentPosition.getText().toString().trim().equals(",")){

            binding.tvCurrentPosition.setText("Position, Company Name");
        }

        //phone info
        if (binding.tvCellPhone.getText().toString().isEmpty()){

            binding.tvCellPhone.setText("...");
        }

        if (binding.tvOfficePhone.getText().toString().isEmpty()){

            binding.tvOfficePhone.setText("...");
        }

        if (binding.tvHomePhone.getText().toString().isEmpty()){

            binding.tvHomePhone.setText("...");
        }

        if (binding.tvFaxPhone.getText().toString().isEmpty()){

            binding.tvFaxPhone.setText("...");
        }

        if (binding.tvPersonalPhone.getText().toString().isEmpty()){

            binding.tvPersonalPhone.setText("...");
        }

        //email info
        if (binding.tvWorkEmail.getText().toString().isEmpty()){

            binding.tvWorkEmail.setText("...");
        }

        if (binding.tvPersonalEmail.getText().toString().isEmpty()){

            binding.tvPersonalEmail.setText("...");
        }

        //address info
        if (binding.tvHomeAddress.getText().toString().isEmpty()){

            binding.tvHomeAddress.setText("...");
        }

        if (binding.tvWorkAddress.getText().toString().isEmpty()){

            binding.tvWorkAddress.setText("...");
        }

        //work history info
        if (binding.tvWorkHistory.getText().toString().isEmpty()){

            binding.tvWorkHistory.setText("...");
        }

        //education info
        if (binding.tvEducation.getText().toString().isEmpty()){

            binding.tvEducation.setText("...");
        }

    }


    @Override
    public void onSettingsClicked() {

        parentNavigator.openSettingsScreen();
    }

    @Override
    public void editEmployer() {

        ChangeFragments.replaceFragmentFadeIn(EditEmployerFragment.newInstance(viewModel.getUserProfile()),getBaseActivity()
                .getSupportFragmentManager(),"EditEmployer",null);
    }

    @Override
    public void editPhoneInfo() {

        ChangeFragments.replaceFragmentFadeIn(EditPhoneFragment.newInstance(viewModel.getUserProfile()),getBaseActivity()
                .getSupportFragmentManager(),"EditPhone",null);
    }

    @Override
    public void editEmailInfo() {

        ChangeFragments.replaceFragmentFadeIn(EditEmailFragment.newInstance(viewModel.getUserProfile()),getBaseActivity()
                .getSupportFragmentManager(),"EditEmail",null);
    }

    @Override
    public void editAddressInfo() {

        ChangeFragments.replaceFragmentFadeIn(EditAddressFragment.newInstance(viewModel.getUserProfile()),getBaseActivity()
                .getSupportFragmentManager(),"EditAddress",null);
    }

    @Override
    public void editWorkHistoryInfo() {

        ChangeFragments.replaceFragmentFadeIn(EditWorkFragment.newInstance(viewModel.getUserProfile()),getBaseActivity()
                .getSupportFragmentManager(),"EditWorkHistory",null);
    }

    @Override
    public void editEductionInfo() {

        ChangeFragments.replaceFragmentFadeIn(EditEducationFragment.newInstance(viewModel.getUserProfile()),getBaseActivity()
                .getSupportFragmentManager(),"EditAddress",null);
    }



    @Override
    public void onUserProfileClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        AddNewImageDialogFragment dialog = AddNewImageDialogFragment.newInstance(this);
        dialog.show(manager,"ImageDialog");
    }

    @Override
    public void openCamera() {

        if (isCameraPermissionsGranted()){

            try {
                takePicture();
            } catch (Exception e) {
                Logger.e("Error opening camera",e.getMessage());
            }
        }
    }

    @Override
    public void openGallery() {

        if (isReadStoragePermissionsGranted()) {

            openPhotoGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppConstants.REQUEST_CAMERA_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            try {
                takePicture();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == AppConstants.REQUEST_READ_EXTERNAL_STORAGE) {

            openPhotoGallery();
        }
    }

    @Override
    public void onProfileUpdated() {

        UserProfile profile = viewModel.getUserProfile();
        binding.setProfile(profile);

        setUserDefaultValues();
    }



    @Override
    public String getImagePath() {

        Uri uri = getViewModel().getImageUri();

        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getBaseActivity().getContentResolver().query(uri, proj, null, null, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                int columnIndex = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(columnIndex);
            }

            cursor.close();
        }

        if (result == null) {
            result = "Not found";
        }

        return result;

    }

    /**
     * Use the camera to capture a picture and save it into a unique file
     */
    public void takePicture() throws Exception {

        File storageDir = getBaseActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = "atlas_profile";
        imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mImageUri;

        int nougat = Build.VERSION_CODES.N;
        if (sdk >= nougat) {
            mImageUri = FileProvider.getUriForFile(getBaseActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    imageFile);
        } else {
            mImageUri = Uri.fromFile(imageFile);
        }

        viewModel.setImageUri(mImageUri);

        getViewModel().setSelfiePath(imageFile.getAbsolutePath());

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (takePictureIntent.resolveActivity(getBaseActivity().getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }


    /**
     * Opens the device photo gallery
     */
    public void openPhotoGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }


    public boolean isReadStoragePermissionsGranted() {

        if (sdk >= marshMallow) {

            if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        AppConstants.REQUEST_READ_EXTERNAL_STORAGE);

                return false;

            } else {

                return true;
            }

        } else {

            return true;
        }
    }

    public boolean isCameraPermissionsGranted() {


        if (sdk >= marshMallow) {

            if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        AppConstants.REQUEST_CAMERA_PERMISSIONS);


                return false;

            } else {

                return true;
            }

        } else {

            return true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case REQUEST_IMAGE_CAPTURE : {

                if (resultCode == Activity.RESULT_OK) {

                    getViewModel().uploadImage(getViewModel(),viewModel.getUserProfile());

                    Glide.with(getContext())
                            .load(getViewModel().getSelfiePath())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(binding.ivUserProfile);

                }

                    break;
            }
            case REQUEST_GALLERY :{

                if (resultCode == Activity.RESULT_OK) {

                    getViewModel().setImageUri(data.getData());

                    Glide.with(getContext())
                            .load(getImagePath())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(binding.ivUserProfile);

                    getViewModel().uploadImage(getViewModel(),viewModel.getUserProfile());

                }
                    break;
            }
        }
    }


    @Override
    public void onConnectionTypeSaved() {

    }
}
