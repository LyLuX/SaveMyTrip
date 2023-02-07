package com.lylux.savemytrip.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lylux.savemytrip.databinding.ActivityTodoListItemBinding;
import com.lylux.savemytrip.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final Listener   mListener;
    private       List<Item> mItemList;
    
    protected ItemAdapter(Listener pListener) {
        this.mListener = pListener;
        this.mItemList = new ArrayList<>();
        
    }
    
    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items of the given type. You can either create a new
     * View manually or inflate it from an XML layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using {@link #onBindViewHolder(ItemViewHolder, int, List)}. Since it
     * will be re-used to display different items in the data set, it is a good idea to cache references to sub views of the View to avoid
     * unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ItemViewHolder, int)
     */
    @NonNull @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context        context  = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        
        return new ItemViewHolder(ActivityTodoListItemBinding.inflate(inflater, parent, false));
    }
    
    /**
     * Called by RecyclerView to display the data at the specified position. This method should update the contents of the
     * {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method again if the position of the item changes in the data set
     * unless the item itself is invalidated or the new position cannot be determined. For this reason, you should only use the
     * <code>position</code> parameter while acquiring the related data item inside this method and should not keep a copy of it. If you
     * need the position of an item later on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Partial bind vs full bind:
     * <p>
     * The payloads parameter is a merge list from {@link #notifyItemChanged(int, Object)} or
     * {@link #notifyItemRangeChanged(int, int, Object)}. If the payloads list is not empty, the ViewHolder is currently bound to old data
     * and Adapter may run an efficient partial update using the payload info.  If the payload is empty,  Adapter must run a full bind.
     * Adapter should not assume that the payload passed in notify methods will be received by onBindViewHolder().  For example when the
     * view is not attached to the screen, the payload in notifyItemChange() will be simply dropped.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.updateWithItem(this.getItemList().get(position), this.getListener());
    }
    
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.getItemList().size();
    }
    
    public void updateData(List<Item> pItemList) {
        this.setItemList(pItemList);
        
        this.notifyDataSetChanged();
    }
    
    protected Listener getListener() {
        return mListener;
    }
    
    protected List<Item> getItemList() {
        return mItemList;
    }
    
    protected void setItemList(List<Item> pItemList) {
        mItemList = pItemList;
    }
    
    public interface Listener {
        void onItemClick(Item pItem);
        
        void onClickDeleteButton(Item pItem);
    }
}
