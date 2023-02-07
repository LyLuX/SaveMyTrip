package com.lylux.savemytrip.todolist;

import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;

import com.lylux.savemytrip.R;
import com.lylux.savemytrip.databinding.ActivityTodoListItemBinding;
import com.lylux.savemytrip.models.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    
    private ActivityTodoListItemBinding mBinding;
    
    protected ItemViewHolder(ActivityTodoListItemBinding pBinding) {
        super(pBinding.getRoot());
        
        this.mBinding = pBinding;
    }
    
    public void updateWithItem(Item pItem, ItemAdapter.Listener pListener) {
        this.getBinding().getRoot().setOnClickListener(view -> pListener.onItemClick(pItem));
        this.getBinding().todoListItemRemoveButton.setOnClickListener((view) -> {
            pListener.onClickDeleteButton(pItem);
        });
        this.getBinding().todoListItemText.setText(pItem.getText());
        
        switch (pItem.getCategory()) {
            case 0:
                this.getBinding().todoListItemImage.setBackgroundResource(R.drawable.ic_location_dark);
                break;
            case 1:
                this.getBinding().todoListItemImage.setBackgroundResource(R.drawable.ic_idea_dark);
                break;
            case 2:
                this.getBinding().todoListItemImage.setBackgroundResource(R.drawable.ic_restaurant_dark);
                break;
        }
        
        if (pItem.isSelected()) {
            this.getBinding().todoListItemText.setPaintFlags(
                    this.getBinding().todoListItemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            this.getBinding().todoListItemText.setPaintFlags(
                    this.getBinding().todoListItemText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
    
    protected ActivityTodoListItemBinding getBinding() {
        return mBinding;
    }
    
    protected void setBinding(ActivityTodoListItemBinding pBinding) {
        mBinding = pBinding;
    }
}
