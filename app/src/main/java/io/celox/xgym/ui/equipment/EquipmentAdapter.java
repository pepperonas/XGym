package io.celox.xgym.ui.equipment;

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
import io.celox.xgym.data.entity.Equipment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EquipmentAdapter extends ListAdapter<Equipment, EquipmentAdapter.EquipmentViewHolder> {

    private OnEquipmentClickListener onEquipmentClickListener;
    private OnEquipmentMoreClickListener onEquipmentMoreClickListener;

    public interface OnEquipmentClickListener {
        void onEquipmentClick(Equipment equipment);
    }

    public interface OnEquipmentMoreClickListener {
        void onEquipmentMoreClick(Equipment equipment, View view);
    }

    public EquipmentAdapter(OnEquipmentClickListener onEquipmentClickListener,
                           OnEquipmentMoreClickListener onEquipmentMoreClickListener) {
        super(DIFF_CALLBACK);
        this.onEquipmentClickListener = onEquipmentClickListener;
        this.onEquipmentMoreClickListener = onEquipmentMoreClickListener;
    }

    private static final DiffUtil.ItemCallback<Equipment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Equipment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Equipment oldItem, @NonNull Equipment newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Equipment oldItem, @NonNull Equipment newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getMinWeight() == newItem.getMinWeight() &&
                   oldItem.getMaxWeight() == newItem.getMaxWeight() &&
                   oldItem.getUpdatedAt() == newItem.getUpdatedAt();
        }
    };

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {
        Equipment equipment = getItem(position);
        holder.bind(equipment);
    }

    class EquipmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageEquipment;
        private TextView textEquipmentName;
        private TextView textWeightRange;
        private TextView textLastUsed;
        private ImageButton buttonMore;

        public EquipmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageEquipment = itemView.findViewById(R.id.image_equipment);
            textEquipmentName = itemView.findViewById(R.id.text_equipment_name);
            textWeightRange = itemView.findViewById(R.id.text_weight_range);
            textLastUsed = itemView.findViewById(R.id.text_last_used);
            buttonMore = itemView.findViewById(R.id.button_more);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onEquipmentClickListener != null) {
                    onEquipmentClickListener.onEquipmentClick(getItem(position));
                }
            });

            buttonMore.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onEquipmentMoreClickListener != null) {
                    onEquipmentMoreClickListener.onEquipmentMoreClick(getItem(position), v);
                }
            });
        }

        public void bind(Equipment equipment) {
            textEquipmentName.setText(equipment.getName());
            textWeightRange.setText(String.format(Locale.getDefault(), "%.0f - %.0f kg", 
                    equipment.getMinWeight(), equipment.getMaxWeight()));
            
            // Format last used date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String lastUsed = "Added: " + sdf.format(new Date(equipment.getCreatedAt()));
            textLastUsed.setText(lastUsed);

            // Load equipment image if available
            if (equipment.getImagePath() != null && !equipment.getImagePath().isEmpty()) {
                // TODO: Load image using Glide
                // Glide.with(itemView.getContext()).load(equipment.getImagePath()).into(imageEquipment);
            } else {
                // Set default equipment icon
                imageEquipment.setImageResource(R.drawable.ic_equipment_black_24dp);
            }
        }
    }
}