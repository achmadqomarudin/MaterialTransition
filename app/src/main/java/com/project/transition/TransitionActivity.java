package com.project.transition;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.transition.utils.TransitionUtils;

public class TransitionActivity extends AppCompatActivity {

    FloatingActionButton fab;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        fab = findViewById(R.id.fab);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        View circleView = findViewById(R.id.circleView);
        Intent intent = getIntent();
        circleView.setTransitionName(intent.getStringExtra("transition"));
        Transition transition = getWindow().getSharedElementEnterTransition();
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                fab.setScaleX(0);
                fab.setScaleY(0);
                fab.setVisibility(View.INVISIBLE);
                nestedScrollView.setAlpha(0f);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                fab.show();
                nestedScrollView.setTranslationY(
                        TransitionUtils.dpToPixels(TransitionActivity.this, 72));
                nestedScrollView.animate().alpha(1f).translationY(0)
                        .setInterpolator(new DecelerateInterpolator());
                transition.removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportPostponeEnterTransition();
    }

    @Override
    public void onBackPressed() {
        fab.setVisibility(View.INVISIBLE);
        nestedScrollView.animate().alpha(0)
                .setInterpolator(new AccelerateInterpolator())
                .translationY(TransitionUtils.dpToPixels(TransitionActivity.this, 72))
                .start();
        supportFinishAfterTransition();
    }
}
