package io.celox.xgym.ui.workouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import io.celox.xgym.R;
import io.celox.xgym.data.entity.Exercise;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionExerciseAdapter extends ListAdapter<Exercise, SessionExerciseAdapter.SessionExerciseViewHolder> {

    private OnExerciseClickListener onExerciseClickListener;

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }

    public SessionExerciseAdapter(OnExerciseClickListener onExerciseClickListener) {
        super(DIFF_CALLBACK);
        this.onExerciseClickListener = onExerciseClickListener;
    }

    private static final DiffUtil.ItemCallback<Exercise> DIFF_CALLBACK = new DiffUtil.ItemCallback<Exercise>() {
        @Override
        public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.getWeight() == newItem.getWeight() &&
                   oldItem.getSets() == newItem.getSets() &&
                   oldItem.getReps() == newItem.getReps() &&
                   oldItem.getTimestamp() == newItem.getTimestamp();
        }
    };

    @NonNull
    @Override
    public SessionExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_exercise, parent, false);
        return new SessionExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionExerciseViewHolder holder, int position) {
        Exercise exercise = getItem(position);
        holder.bind(exercise);
    }

    class SessionExerciseViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageEquipment;
        private TextView textEquipmentName;
        private TextView textExerciseDetails;
        private TextView textExerciseTime;
        private ImageButton buttonEditExercise;

        public SessionExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageEquipment = itemView.findViewById(R.id.image_equipment);
            textEquipmentName = itemView.findViewById(R.id.text_equipment_name);
            textExerciseDetails = itemView.findViewById(R.id.text_exercise_details);
            textExerciseTime = itemView.findViewById(R.id.text_exercise_time);
            buttonEditExercise = itemView.findViewById(R.id.button_edit_exercise);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onExerciseClickListener != null) {
                    onExerciseClickListener.onExerciseClick(getItem(position));
                }
            });

            buttonEditExercise.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onExerciseClickListener != null) {
                    onExerciseClickListener.onExerciseClick(getItem(position));
                }
            });
        }

        public void bind(Exercise exercise) {
            // TODO: Load equipment name from database
            textEquipmentName.setText("Equipment #" + exercise.getEquipmentId());
            
            // Format exercise details
            String details = String.format(Locale.getDefault(), 
                "%.0fkg • %d sets • %d reps", 
                exercise.getWeight(), exercise.getSets(), exercise.getReps());
            textExerciseDetails.setText(details);

            // Format time
            long timeDiff = System.currentTimeMillis() - exercise.getTimestamp();
            String timeText;
            if (timeDiff < 60000) { // Less than 1 minute
                timeText = "Just now";
            } else if (timeDiff < 3600000) { // Less than 1 hour
                long minutes = timeDiff / 60000;
                timeText = minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                timeText = sdf.format(new Date(exercise.getTimestamp()));
            }
            textExerciseTime.setText(timeText);

            // Set default equipment icon
            imageEquipment.setImageResource(R.drawable.ic_equipment_black_24dp);
        }
    }
}