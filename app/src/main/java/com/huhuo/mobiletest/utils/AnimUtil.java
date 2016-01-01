package com.huhuo.mobiletest.utils;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class AnimUtil {

	static Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	static final int ROTATION_ANIMATION_DURATION = 1200;
	private static Handler handler = new Handler();
	private static long ANIM_DURATION_TIME = 500;
	private static long DELAY_TIME = 500;

	public static Animation getRotateAnim() {
		Animation mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		return mRotateAnimation;
	}

	public static void delayFloatAnim(final View v, long delay) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				ObjectAnimator.ofFloat(v, "alpha", 1, 0).setDuration(ANIM_DURATION_TIME).start();
			}
		}, delay);
	}
	
	public static void delayFloatAnimAgainst(final View v, long delay) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				ObjectAnimator.ofFloat(v, "alpha", 0, 1).setDuration(ANIM_DURATION_TIME).start();
			}
		}, delay);
	}

	public static void delayFloatAnim(final View v, final long animDuration, final long delayTime) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				ObjectAnimator.ofFloat(v, "alpha", 1, 0)
						.setDuration(animDuration == 0 ? ANIM_DURATION_TIME : animDuration)
						.start();
			}
		}, delayTime);
	}
}
