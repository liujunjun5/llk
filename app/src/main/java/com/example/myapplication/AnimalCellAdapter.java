package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AnimalCellAdapter extends RecyclerView.Adapter<AnimalCellAdapter.AnimalCellViewHolder> {
    private List<AnimalCell> cells;

    public AnimalCellAdapter(List<AnimalCell> cells) {
        this.cells = cells;
    }

    @Override
    public AnimalCellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new AnimalCellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimalCellViewHolder holder, int position) {
        AnimalCell cell = cells.get(position);
        holder.textView.setText("Row: " + cell.row + ", Col: " + cell.col + ", Type: " + cell.type);
    }

    @Override
    public int getItemCount() {
        return cells.size();
    }

    public static class AnimalCellViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public AnimalCellViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}