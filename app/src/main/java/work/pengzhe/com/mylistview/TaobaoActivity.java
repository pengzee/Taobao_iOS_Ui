package work.pengzhe.com.mylistview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TaobaoActivity extends Activity {

    private RelativeLayout rootView;
    private LinearLayout iv_bg;
    private ImageView iv_bottom;
    private int iv_height = 1600;
    private int duration = 1000;
    private ObjectAnimator showAnimator;
    private ObjectAnimator bgAnimator;
    private ObjectAnimator alphaAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
        iv_bg = (LinearLayout) findViewById(R.id.iv_bg);
        iv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewAnim();
                iv_bg.setClickable(false);
            }
        });
        iv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideViewAnim();
            }
        });
    }

    private void hideViewAnim() {
        iv_bg.setClickable(true);
        showAnimator.reverse();
        bgAnimator.reverse();
        alphaAnimator.reverse();

    }

    private void showViewAnim() {
        showAnimator = ObjectAnimator.ofFloat(iv_bottom, "translationY", iv_bottom.getHeight(), 0f);
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                iv_bottom.setVisibility(View.VISIBLE);
            }
        });
        showAnimator.setDuration(duration);

        bgAnimator = ObjectAnimator.ofFloat(iv_bg, "RotationX", 0, 6);
        bgAnimator.setDuration(duration);

        alphaAnimator = ObjectAnimator.ofFloat(iv_bg, "alpha", 1.0f, 0.7f);
        alphaAnimator.setDuration(duration);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(showAnimator, bgAnimator, alphaAnimator);
        set.start();


    }

}