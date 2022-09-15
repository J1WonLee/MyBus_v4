package com.example.mybus.homeedit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback{
//    private ItemTouchHelperListener itemTouchHelperListener;
    private HomeEditAdapter homeEditAdapter;

//    public ItemTouchHelperCallback(ItemTouchHelperListener itemTouchHelperListener) {
//        this.itemTouchHelperListener = itemTouchHelperListener;
//    }


    public ItemTouchHelperCallback(HomeEditAdapter homeEditAdapter) {
        this.homeEditAdapter = homeEditAdapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(drag_flags, swipe_flags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        homeEditAdapter.swapData(from, to);
//        return itemTouchHelperListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        homeEditAdapter.removeData(viewHolder.getAdapterPosition());
    }



    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
