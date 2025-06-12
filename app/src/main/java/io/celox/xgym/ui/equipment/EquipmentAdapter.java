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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EquipmentAdapter extends ListAdapter<Equipment, EquipmentAdapter.EquipmentViewHolder> {

    private OnEquipmentClickListener onEquipmentClickListener;
    private OnEquipmentMoreClickListener onEquipmentMoreClickListener;
    
    // Mapping from equipment type to icon resource
    private static final Map<String, Integer> EQUIPMENT_ICONS = new HashMap<>();
    static {
        // Ausdauer Equipment
        EQUIPMENT_ICONS.put("Laufband", R.drawable.ic_laufband);
        EQUIPMENT_ICONS.put("Crosstrainer", R.drawable.ic_crosstrainer);
        EQUIPMENT_ICONS.put("Ergometer", R.drawable.ic_ergometer);
        EQUIPMENT_ICONS.put("Rudergerät", R.drawable.ic_ergometer); // Use ergometer as placeholder
        EQUIPMENT_ICONS.put("Stepper", R.drawable.ic_stepper);
        EQUIPMENT_ICONS.put("Spinning Bike", R.drawable.ic_ergometer); // Use ergometer as placeholder
        
        // Kraftsport Equipment
        EQUIPMENT_ICONS.put("Beinpresse", R.drawable.ic_beinpresse);
        EQUIPMENT_ICONS.put("Latzug", R.drawable.ic_latzug);
        EQUIPMENT_ICONS.put("Bankdrücken", R.drawable.ic_hantelbank);
        EQUIPMENT_ICONS.put("Beinstrecker", R.drawable.ic_beinstrecker);
        EQUIPMENT_ICONS.put("Beinbeuger", R.drawable.ic_beinbeuger);
        EQUIPMENT_ICONS.put("Butterfly", R.drawable.ic_butterfly);
        EQUIPMENT_ICONS.put("Kabelzug", R.drawable.ic_kabelzug);
        EQUIPMENT_ICONS.put("Schulterdrücken", R.drawable.ic_schulterpresse);
        EQUIPMENT_ICONS.put("Bizeps Curl", R.drawable.ic_bizepsmaschine);
        EQUIPMENT_ICONS.put("Trizeps", R.drawable.ic_trizepsmaschine);
        EQUIPMENT_ICONS.put("Bauchtrainer", R.drawable.ic_hantel); // Use general hantel icon
        EQUIPMENT_ICONS.put("Rückenstrecker", R.drawable.ic_latzug); // Use latzug as placeholder
        EQUIPMENT_ICONS.put("Hantelbank", R.drawable.ic_hantelbank);
        EQUIPMENT_ICONS.put("Kurzhantel", R.drawable.ic_hantel);
        EQUIPMENT_ICONS.put("Langhantel", R.drawable.ic_hantel);
    }

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
    
    // Method to force refresh of equipment list
    public void refreshEquipment(java.util.List<Equipment> equipmentList) {
        submitList(null);
        submitList(equipmentList);
    }

    private static final DiffUtil.ItemCallback<Equipment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Equipment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Equipment oldItem, @NonNull Equipment newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Equipment oldItem, @NonNull Equipment newItem) {
            // Simplified comparison - if updatedAt changed, consider content different
            return oldItem.getUpdatedAt() == newItem.getUpdatedAt() &&
                   java.util.Objects.equals(oldItem.getName(), newItem.getName()) &&
                   java.util.Objects.equals(oldItem.getType(), newItem.getType()) &&
                   java.util.Objects.equals(oldItem.getCategory(), newItem.getCategory()) &&
                   oldItem.getMinWeight() == newItem.getMinWeight() &&
                   oldItem.getMaxWeight() == newItem.getMaxWeight() &&
                   oldItem.getCurrentWeight() == newItem.getCurrentWeight();
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

            // Set equipment icon based on type
            setEquipmentIcon(equipment);
        }
        
        private void setEquipmentIcon(Equipment equipment) {
            Integer iconRes = null;
            
            // First try to get icon by equipment type
            if (equipment.getType() != null && !equipment.getType().isEmpty()) {
                iconRes = EQUIPMENT_ICONS.get(equipment.getType());
            }
            
            // If no specific icon found, use category-based fallback
            if (iconRes == null) {
                if ("ausdauer".equals(equipment.getCategory())) {
                    iconRes = R.drawable.ic_laufband; // Default cardio icon
                } else if ("kraftsport".equals(equipment.getCategory())) {
                    iconRes = R.drawable.ic_hantel; // Default strength icon
                }
            }
            
            // Final fallback to generic equipment icon
            if (iconRes == null) {
                iconRes = R.drawable.ic_equipment_black_24dp;
            }
            
            imageEquipment.setImageResource(iconRes);
        }
    }
}