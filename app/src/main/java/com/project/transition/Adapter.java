package com.project.transition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.transition.utils.ExpandAnimation;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final OnClickListener listener;

    public Adapter(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View circleView;
        View expandView;
        View expandableView;
        boolean expanded;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            expandView = itemView.findViewById(R.id.expandView);
            expandableView = itemView.findViewById(R.id.expandableView);
            circleView = itemView.findViewById(R.id.circleView);
            circleView.setOnClickListener(this);
        }

        public void bind(int position) {
            circleView.setTransitionName("transition" + position);
            circleView.invalidate();
            ViewGroup.LayoutParams params = expandView.getLayoutParams();
            params.height = expanded ?
                    itemView.getResources().getDimensionPixelOffset(R.dimen.card_expanded_height)
                    : itemView.getResources().getDimensionPixelOffset(R.dimen.card_default_height);
            expandView.setLayoutParams(params);
            expandableView.setAlpha(expanded ? 1.0f : 0.0f);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.circleView) {
                if (listener != null) {
                    listener.onItemClick(v, v.getTransitionName(), getAdapterPosition());
                }
            } else {
                expandOrCollapse();
            }
        }

        private void expandOrCollapse() {
            int expandedHeight = itemView.getResources()
                    .getDimensionPixelOffset(R.dimen.card_expanded_height);
            int defaultHeight = itemView.getResources()
                    .getDimensionPixelOffset(R.dimen.card_default_height);

            if (expanded) {
                int tmp = defaultHeight;
                defaultHeight = expandedHeight;
                expandedHeight = tmp;
            }

            ExpandAnimation expandAnimation = new ExpandAnimation(expandView, defaultHeight,
                    expandedHeight);
            expandAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            expandAnimation.setDuration(375);
            expandAnimation.start();

            expandableView.animate().alpha(expanded ? 0.0f : 1.0f).setStartDelay(75);
            expanded = !expanded;
        }
    }

    public interface OnClickListener {
        void onItemClick(View sharedView, String transitionName, int position);
    }
}
