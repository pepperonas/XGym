package io.celox.xgym.ui.statistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import io.celox.xgym.R;
import io.celox.xgym.data.entity.Achievement;

import java.util.Locale;

public class AchievementAdapter extends ListAdapter<Achievement, AchievementAdapter.AchievementViewHolder> {

    public AchievementAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Achievement> DIFF_CALLBACK = new DiffUtil.ItemCallback<Achievement>() {
        @Override
        public boolean areItemsTheSame(@NonNull Achievement oldItem, @NonNull Achievement newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Achievement oldItem, @NonNull Achievement newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                   oldItem.getCurrentValue() == newItem.getCurrentValue() &&
                   oldItem.isUnlocked() == newItem.isUnlocked();
        }
    };

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = getItem(position);
        holder.bind(achievement);
    }

    class AchievementViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconAchievement;
        private TextView textAchievementTitle;
        private TextView textAchievementDescription;
        private TextView textAchievementProgress;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            iconAchievement = itemView.findViewById(R.id.icon_achievement);
            textAchievementTitle = itemView.findViewById(R.id.text_achievement_title);
            textAchievementDescription = itemView.findViewById(R.id.text_achievement_description);
            textAchievementProgress = itemView.findViewById(R.id.text_achievement_progress);
        }

        public void bind(Achievement achievement) {
            textAchievementTitle.setText(achievement.getTitle());
            textAchievementDescription.setText(achievement.getDescription());
            
            String progressText = String.format(Locale.getDefault(), "%d/%d",
                    achievement.getCurrentValue(), achievement.getTargetValue());
            textAchievementProgress.setText(progressText);

            // Set icon based on achievement state
            if (achievement.isUnlocked()) {
                iconAchievement.setImageResource(R.drawable.ic_trophy_24dp);
                iconAchievement.setAlpha(1.0f);
            } else {
                iconAchievement.setImageResource(R.drawable.ic_trophy_24dp);
                iconAchievement.setAlpha(0.3f);
            }
        }
    }
}