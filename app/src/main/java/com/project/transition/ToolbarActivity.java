package com.project.transition;

import android.os.Bundle;
import android.transition.Fade;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ToolbarActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    NestedScrollView nestedScrollView;
    AppBarLayout appbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();
        setContentView(R.layout.activity_toolbar);
        appbar = findViewById(R.id.appbar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        collapsingToolbarLayout = findViewById(R.id.collapsingLayout);
        if (savedInstanceState == null) {
            nestedScrollView.setAlpha(0f);
            nestedScrollView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (nestedScrollView.getHeight() != 0) {
                                appbar.setExpanded(true);
                                appbar.addOnOffsetChangedListener(ToolbarActivity.this);
                                nestedScrollView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                                nestedScrollView.animate().setStartDelay(400).alpha(1f);
                                nestedScrollView.setTranslationY(nestedScrollView.getHeight() / 3);
                                nestedScrollView.animate().setStartDelay(400).translationY(0)
                                        .setInterpolator(new AccelerateDecelerateInterpolator());
                            }
                        }
                    });
        }
    }

    private void setupTransitions() {
        getWindow().setEnterTransition(new Fade());
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            AppBarLayout.LayoutParams layoutParams
                    = (AppBarLayout.LayoutParams)
                    collapsingToolbarLayout.getLayoutParams();
            layoutParams.setScrollFlags(0);
            collapsingToolbarLayout.setLayoutParams(layoutParams);
            appBarLayout.removeOnOffsetChangedListener(this);
        }
    }
}
