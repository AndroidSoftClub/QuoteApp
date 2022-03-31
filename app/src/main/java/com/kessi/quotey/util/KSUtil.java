package com.kessi.quotey.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.kessi.quotey.R;

public class KSUtil {

    public static AnimatorSet InUp(View view){
        AnimatorSet animatorSet = new AnimatorSet();
        long measured_height = view.getMeasuredHeight();

        ObjectAnimator object1 = ObjectAnimator.ofFloat(view,   "translationY", measured_height, -30, 10, 0);
        ObjectAnimator object2 = ObjectAnimator.ofFloat(view,   "alpha", 0, 1, 1, 1);

        animatorSet.playTogether(object1, object2);
        return animatorSet;
    }

    public static AnimatorSet In(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator object = ObjectAnimator.ofFloat(view, "alpha", 0, 1);

        animatorSet.playTogether(object);
        return animatorSet;
    }

    public static AnimatorSet InDown(View view){
        AnimatorSet animatorSet = new AnimatorSet();
        long height = - view.getHeight();

        ObjectAnimator object1 =   ObjectAnimator.ofFloat(view,  "alpha", 0, 1, 1, 1);
        ObjectAnimator object2 =   ObjectAnimator.ofFloat(view,  "translationY", height, 30, -10, 0);

        animatorSet.playTogether(object1, object2);
        return animatorSet;
    }

    public static AnimatorSet Out(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator object1 = ObjectAnimator.ofFloat(view,  "alpha", 1, 0, 0);
        ObjectAnimator object2 = ObjectAnimator.ofFloat(view,   "scaleX", 1, 0.3f, 0);
        ObjectAnimator object3 = ObjectAnimator.ofFloat(view,    "scaleY", 1, 0.3f, 0);

        animatorSet.playTogether(object1, object2, object3);
        return animatorSet;
    }

    public static AnimatorSet Tada(View view){
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator object1 = ObjectAnimator.ofFloat(view,   "scaleX", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        ObjectAnimator object2 = ObjectAnimator.ofFloat(view,   "scaleY", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        ObjectAnimator object3 = ObjectAnimator.ofFloat(view,   "rotation", 0, -3, -3, 3, -3, 3, -3, 3, -3, 0);

        animatorSet.playTogether(object1, object2, object3);
        return animatorSet;
    }

    public static AnimatorSet ZoomIn(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator object1 = ObjectAnimator.ofFloat(view,  "scaleX", 0.45f, 1);
        ObjectAnimator object2 = ObjectAnimator.ofFloat(view,  "scaleY", 0.45f, 1);
        ObjectAnimator object3 = ObjectAnimator.ofFloat(view,   "alpha", 0, 1);

        animatorSet.playTogether(object1, object2, object3);
        return animatorSet;
    }


    public static void Bounce(Context context, View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce_anim);
        myAnim.setInterpolator(new BounceyInterpolator(0.2d, 20.0d));
        view.startAnimation(myAnim);
    }

}
