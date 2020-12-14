package com.project.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.transition.utils.TransitionUtils;

public class MainActivity extends AppCompatActivity implements Adapter.OnClickListener {

    private RecyclerView recyclerView;
    private View rippleView;
    private FloatingActionButton fab;
    private boolean launchedActivity;
    private int viewPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setupTransitions();
        initViews();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar !=null) {
            toolbar.setTitle("Material Transition");
            setSupportActionBar(toolbar);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        setupRecyclerView();
        rippleView = findViewById(R.id.rippleView);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (TransitionUtils.isAtLeastLollipop()) {
                startRippleTransitionReveal();
            } else {
                startActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_git) {
            Uri uri = Uri.parse("https://github.com/achmadqomarudin");
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, uri), "Choose Browser"));
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", viewPosition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRippleTransitionReveal() {
        fab.setVisibility(View.INVISIBLE);
        Animator animator = ViewAnimationUtils.createCircularReveal(rippleView,
                (int) fab.getX() + fab.getWidth() / 2,
                (int) fab.getY(), fab.getWidth() / 2, TransitionUtils.getViewRadius(rippleView) * 2);
        rippleView.setVisibility(View.VISIBLE);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                recyclerView.animate().alpha(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity();
            }
        });
        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRippleTransitionUnreveal() {
        Animator animator = ViewAnimationUtils.createCircularReveal(rippleView,
                (int) fab.getX() + fab.getWidth() / 2,
                (int) fab.getY(), TransitionUtils.getViewRadius(rippleView) * 2, fab.getWidth() / 2);
        rippleView.setVisibility(View.VISIBLE);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                recyclerView.animate().alpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fab.setVisibility(View.VISIBLE);
                rippleView.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (launchedActivity) {
            startRippleTransitionUnreveal();
            launchedActivity = false;
        }
    }

    @Override
    public void onItemClick(View sharedView, String transitionName, int position) {
        viewPosition = position;
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra("transition", transitionName);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, sharedView, transitionName);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    private void startActivity() {
        ActivityCompat.startActivity(this, new Intent(this, ToolbarActivity.class),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        launchedActivity = true;
    }

    private void setupTransitions() {
        getWindow().setExitTransition(new Fade());
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(new Adapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
