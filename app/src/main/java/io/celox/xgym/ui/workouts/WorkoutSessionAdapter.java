package io.celox.xgym.ui.workouts;

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
import io.celox.xgym.data.entity.WorkoutSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkoutSessionAdapter extends ListAdapter<WorkoutSession, WorkoutSessionAdapter.WorkoutSessionViewHolder> {

    private OnWorkoutSessionClickListener onWorkoutSessionClickListener;

    public interface OnWorkoutSessionClickListener {
        void onWorkoutSessionClick(WorkoutSession workoutSession);
    }

    public WorkoutSessionAdapter(OnWorkoutSessionClickListener onWorkoutSessionClickListener) {
        super(DIFF_CALLBACK);
        this.onWorkoutSessionClickListener = onWorkoutSessionClickListener;
    }

    private static final DiffUtil.ItemCallback<WorkoutSession> DIFF_CALLBACK = new DiffUtil.ItemCallback<WorkoutSession>() {
        @Override
        public boolean areItemsTheSame(@NonNull WorkoutSession oldItem, @NonNull WorkoutSession newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkoutSession oldItem, @NonNull WorkoutSession newItem) {
            return oldItem.getStartTime() == newItem.getStartTime() &&
                   oldItem.getDuration() == newItem.getDuration() &&
                   oldItem.isCompleted() == newItem.isCompleted();
        }
    };

    @NonNull
    @Override
    public WorkoutSessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_session, parent, false);
        return new WorkoutSessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutSessionViewHolder holder, int position) {
        WorkoutSession workoutSession = getItem(position);
        holder.bind(workoutSession);
    }

    class WorkoutSessionViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconWorkout;
        private TextView textWorkoutDate;
        private TextView textWorkoutExercises;
        private TextView textWorkoutDuration;

        public WorkoutSessionViewHolder(@NonNull View itemView) {
            super(itemView);
            iconWorkout = itemView.findViewById(R.id.icon_workout);
            textWorkoutDate = itemView.findViewById(R.id.text_workout_date);
            textWorkoutExercises = itemView.findViewById(R.id.text_workout_exercises);
            textWorkoutDuration = itemView.findViewById(R.id.text_workout_duration);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onWorkoutSessionClickListener != null) {
                    onWorkoutSessionClickListener.onWorkoutSessionClick(getItem(position));
                }
            });
        }

        public void bind(WorkoutSession workoutSession) {
            // Format date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String dateString = dateFormat.format(new Date(workoutSession.getStartTime()));
            
            // Check if it's today
            SimpleDateFormat todayFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String today = todayFormat.format(new Date());
            if (dateString.equals(today)) {
                dateString = "Today";
            }
            
            textWorkoutDate.setText(dateString);

            // Format duration
            if (workoutSession.isCompleted() && workoutSession.getDuration() > 0) {
                long minutes = workoutSession.getDuration() / (1000 * 60);
                textWorkoutDuration.setText(String.format(Locale.getDefault(), "%dm", minutes));
                
                // TODO: Get actual exercise count from database
                textWorkoutExercises.setText("Completed workout");
            } else {
                textWorkoutDuration.setText("Active");
                textWorkoutExercises.setText("In progress...");
            }
        }
    }
}