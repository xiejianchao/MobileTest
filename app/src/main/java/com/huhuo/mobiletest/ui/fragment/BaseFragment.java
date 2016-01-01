package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.utils.InputMethodUtil;
import com.huhuo.mobiletest.utils.Logger;

import org.xutils.x;


public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    private boolean injected = false;
    protected Context context = null;
    public static FragmentManager fragmentManager;

    protected Handler handler = new Handler();

    protected InputMethodManager mInputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        context = MobileTestApplication.getInstance().getContext();
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        fragmentManager = getFragmentManager();
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    public Context getContext() {
        return context;
    }

    public void removeFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Fragment findFaragment(int layoutId) {
        FragmentManager fm = fragmentManager;
        return fm.findFragmentById(layoutId);
    }

    protected Fragment findFaragment(String tag) {
        FragmentManager fm = fragmentManager;
        return fm.findFragmentByTag(tag);
    }

    protected Fragment findNestFaragment(String tag) {
        FragmentManager fm = getChildFragmentManager();
        return fm.findFragmentByTag(tag);
    }
    /**
     * 嵌套Fragment的时候使用
     *
     * @param fragment
     */
    public void showNestFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        } catch (Exception e) {
            Logger.d(TAG, "showNestFragment", e);
        }
    }

    /**
     * 嵌套Fragment的时候使用
     *
     * @param fragment
     * @param id
     * @param tag
     */
    public void addNewNestFragment(Fragment fragment, int id, String tag) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.slide_right_in,
        // R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);

        if (tag == null) {
            transaction.add(id, fragment);
        } else {
            transaction.add(id, fragment, tag);
        }
        transaction.commit();
    }

    public void addNewFragment(Fragment fragment, int id, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.setCustomAnimations(R.anim.slide_right_in,
        // R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);

        if (enterAnim != 0 && exitAnim != 0) {
            // transaction.setCustomAnimations(enterAnim, exitAnim, enterAnim,
            // exitAnim);
        }
        transaction.add(id, fragment);
        transaction.commit();
    }

    public void hideFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragment);
            transaction.commit();
        } catch (Exception e) {
            Logger.e(TAG, "hideFragment", e);
        }
    }

    /**
     * 隐藏嵌套的Fragment的时候使用该方法
     *
     * @param fragment
     */
    public void hideNestFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    protected void showInputMethod() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getActivity() != null) {
                    InputMethodUtil.showInputMehtod(getActivity());
                }
            }
        }, 300);
    }

}
