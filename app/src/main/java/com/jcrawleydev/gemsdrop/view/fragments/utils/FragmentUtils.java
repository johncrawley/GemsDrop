package com.jcrawleydev.gemsdrop.view.fragments.utils;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.view.fragments.game.GameFragment;
import com.jcrawleydev.gemsdrop.view.fragments.GameOverFragment;
import com.jcrawleydev.gemsdrop.view.fragments.MainMenuFragment;

import java.util.function.Consumer;

public class FragmentUtils {


    public static void showDialog(Fragment parentFragment, DialogFragment dialogFragment, String tag, Bundle bundle){
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentTransaction, tag);
    }


    public static void loadGame(Fragment parentFragment){
        loadFragment(parentFragment, new GameFragment(), "game_fragment");
    }


    public static void loadAbout(Fragment parentFragment){
        loadFragment(parentFragment, new GameFragment(), "about_fragment");
    }


    public static void loadGameOver(Fragment parentFragment){
        loadFragment(parentFragment, new GameOverFragment(), "game_over_fragment");
    }


    public static void loadMainMenu(Fragment parentFragment){
        loadFragment(parentFragment, new MainMenuFragment(), "main_menu_fragment");
    }


    public static void loadFragmentOnBackButtonPressed(Fragment parentFragment, Fragment destinationFragment, String fragmentTag){
        onBackButtonPressed(parentFragment, () -> loadFragment(parentFragment, destinationFragment, fragmentTag));
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag, Bundle bundle){
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.pop_enter, R.anim.pop_exit )
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    public static void onBackButtonPressed(Fragment parentFragment, Runnable action){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                action.run();
            }
        };
        parentFragment.requireActivity().getOnBackPressedDispatcher().addCallback(parentFragment.getViewLifecycleOwner(), callback);
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag){
        loadFragment(parentFragment, fragment, tag, new Bundle());
    }


    private static void removePreviousFragmentTransaction(FragmentManager fragmentManager, String tag, FragmentTransaction fragmentTransaction){
        Fragment prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
    }


    public static void setListener(Fragment fragment, String key, Consumer<Bundle> consumer){
        fragment.getParentFragmentManager().setFragmentResultListener(key, fragment, (requestKey, bundle) -> consumer.accept(bundle));
    }


    public static void setListener(Fragment fragment, FragmentMessage fragmentMessage, Consumer<Bundle> consumer){
        fragment.getParentFragmentManager().setFragmentResultListener(fragmentMessage.name(), fragment, (requestKey, bundle) -> consumer.accept(bundle));
    }


    public static void sendMessage(Fragment fragment, String key){
        sendMessage(fragment, key, new Bundle());
    }


    public static void sendMessage(Fragment fragment, String key, Bundle bundle){
        fragment.getParentFragmentManager().setFragmentResult(key, bundle);
    }


    public static void sendMessage(AppCompatActivity activity, FragmentMessage message, Bundle bundle){
        activity.runOnUiThread(()-> activity.getSupportFragmentManager().setFragmentResult(message.name(), bundle));
    }


    public static void sendMessage(AppCompatActivity activity, FragmentMessage message){
        sendMessage(activity, message, new Bundle());
    }

    public static Bundle createBundleOf(BundleTag tag, int value){
        var bundle = new Bundle();
        bundle.putInt(tag.name(), value);
        return bundle;
    }


    public static void addIntTo(Bundle bundle, BundleTag tag, int value){
        bundle.putInt(tag.name(), value);
    }


    public static void addIntArrayTo(Bundle bundle, BundleTag tag, int[] values){
        bundle.putIntArray(tag.name(), values);
    }


    public static void addLongArrayTo(Bundle bundle, BundleTag tag, long[] values){
        bundle.putLongArray(tag.name(), values);
    }


    public static void addLongTo(Bundle bundle, BundleTag tag, long value){
        bundle.putLong(tag.name(), value);
    }


    public static int getInt(Bundle bundle, Enum<?> tag){
        return bundle.getInt(tag.name());
    }


    public static int[] getIntArrayFrom(Bundle bundle, Enum<?> tag){
        return bundle.getIntArray(tag.name());
    }


    public static long[] getLongArrayFrom(Bundle bundle, Enum<?> tag){
        return bundle.getLongArray(tag.name());
    }


    public static long[] getLongArray(Bundle bundle, Enum<?> tag){
        return bundle.getLongArray(tag.name());
    }


    public static String getStr(Bundle bundle, Enum<?> tag){
        return bundle.getString(tag.name());
    }


    public static boolean getBoolean(Bundle bundle, Enum<?> tag){
        return  bundle.getBoolean(tag.name());
    }



}