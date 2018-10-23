package com.team.mamba.atlascalendar.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.team.mamba.atlascalendar.R;

/**
 * Use this class to change or show your fragments
 */

public class ChangeFragments {

    public static boolean isUnitTesting = false;


    public static void addFragmentHorizontally(Fragment fragmentId, FragmentManager fragmentManager, String tag, Bundle args){


        if (isUnitTesting){//Robolectric cannot handle fragment animations (gets stuck in infinite loop)

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragmentId.setArguments(args);
            transaction.add(R.id.fragment_container, fragmentId, tag);
            transaction.addToBackStack(tag);

            transaction.commit();

            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentId.setArguments(args);
        transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right);
        transaction.add(R.id.fragment_container, fragmentId, tag);
        transaction.addToBackStack(tag);

        transaction.commit();
    }

    public static void addFragmentVertically(Fragment fragmentId, FragmentManager fragmentManager, String tag, Bundle args){


        if (isUnitTesting){//Robolectric cannot handle fragment animations (gets stuck in infinite loop)

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragmentId.setArguments(args);
            transaction.add(R.id.fragment_container, fragmentId, tag);
            transaction.addToBackStack(tag);

            transaction.commit();

            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentId.setArguments(args);
        transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,android.R.anim.fade_in,R.anim.slide_out_down);
        transaction.add(R.id.fragment_container, fragmentId, tag);
        transaction.addToBackStack(tag);

        transaction.commit();
    }


    public static void addFragmentFadeIn(Fragment fragmentId, FragmentManager fragmentManager, String tag, Bundle args){


        if (isUnitTesting){//Robolectric cannot handle fragment animations (gets stuck in infinite loop)

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragmentId.setArguments(args);
            transaction.add(R.id.fragment_container, fragmentId, tag);
            transaction.addToBackStack(tag);

            transaction.commit();

            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentId.setArguments(args);
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.add(R.id.fragment_container, fragmentId, tag);
        transaction.addToBackStack(tag);

        transaction.commit();
    }


    public static void replaceHorizontallyFromBackStack(Fragment fragmentId, FragmentManager fragmentManager, String tag, Bundle args){


        if (isUnitTesting){//Robolectric cannot handle fragment animations (gets stuck in infinite loop)

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragmentId.setArguments(args);
            transaction.replace(R.id.fragment_container, fragmentId, tag);

            transaction.commit();

            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentId.setArguments(args);
        transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right);
        transaction.replace(R.id.fragment_container, fragmentId, tag);

        transaction.commit();
    }

    public static void replaceFragmentFadeIn(Fragment fragmentId, FragmentManager fragmentManager, String tag, Bundle args){


        if (isUnitTesting){//Robolectric cannot handle fragment animations (gets stuck in infinite loop)

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragmentId.setArguments(args);
            transaction.replace(R.id.fragment_container, fragmentId, tag);
            transaction.addToBackStack(tag);
            transaction.commit();

            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentId.setArguments(args);
        transaction.setCustomAnimations(android.R.anim.fade_in, R.anim.no_fade,R.anim.no_fade,android.R.anim.fade_out);
        transaction.replace(R.id.fragment_container, fragmentId, tag);
        transaction.addToBackStack(tag);

        transaction.commit();
    }


    public static void replaceFragmentVertically(Fragment fragmentId, FragmentManager fragmentManager, String tag, Bundle args){


        if (isUnitTesting){//Robolectric cannot handle fragment animations (gets stuck in infinite loop)

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragmentId.setArguments(args);
            transaction.replace(R.id.fragment_container, fragmentId, tag);
            transaction.addToBackStack(tag);

            transaction.commit();

            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentId.setArguments(args);
        transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,android.R.anim.fade_in,R.anim.slide_out_down);
        transaction.replace(R.id.fragment_container, fragmentId, tag);
        transaction.addToBackStack(tag);

        transaction.commit();
    }


    public static void replaceFromBackStack(Fragment fragmentId, FragmentManager fragmentManager, String tag, Bundle args){


        if (isUnitTesting){//Robolectric cannot handle fragment animations (gets stuck in infinite loop)

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragmentId.setArguments(args);
            transaction.replace(R.id.fragment_container, fragmentId, tag);
            transaction.commit();

            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentId.setArguments(args);
        transaction.replace(R.id.fragment_container, fragmentId, tag);
        transaction.commit();

    }
}
