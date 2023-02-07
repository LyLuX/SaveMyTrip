package com.lylux.savemytrip.todolist;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.lylux.savemytrip.R;
import com.lylux.savemytrip.databinding.ActivityTodoListBinding;
import com.lylux.savemytrip.injection.ViewModelFactory;
import com.lylux.savemytrip.models.Item;
import com.lylux.savemytrip.models.User;

import java.util.List;

public class TodoListActivity extends AppCompatActivity implements ItemAdapter.Listener {
    
    private static final int                     USER_ID = 1;
    private              ActivityTodoListBinding mBinding;
    private              ItemViewModel           mItemViewModel;
    private              ItemAdapter             mItemAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setBinding(ActivityTodoListBinding.inflate(this.getLayoutInflater()));
        setContentView(this.getBinding().getRoot());
        
        this.configureRecyclerView();
        this.configureViewModel();
        this.initView();
        
        this.getCurrentUser();
        
        this.getItemViewModel().createItem(0, "Location", USER_ID);
        this.getItemViewModel().createItem(1, "Idea", USER_ID);
        this.getItemViewModel().createItem(2, "Restaurant", USER_ID);
        
        this.getUserItems();
    }
    
    @Override public void onItemClick(Item pItem) {
        this.updateItem(pItem);
    }
    
    @Override public void onClickDeleteButton(Item pItem) {
        this.deleteItem(pItem);
    }
    
    private void initView() {
        ActionBar actionBar = this.getSupportActionBar();
        
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        
        this.configureSpinner();
        this.getBinding().todoListButtonAdd.setOnClickListener((v) -> Log.i("INFO", "onClickListener from button add"));
    }
    
    private void configureViewModel() {
        this.setItemViewModel(new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(ItemViewModel.class));
        
        this.getItemViewModel().initUser(USER_ID);
    }
    
    private void configureSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array,
                                                                             android.R.layout.simple_spinner_item);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.getBinding().todoListSpinner.setAdapter(adapter);
    }
    
    private void getCurrentUser() {
        LiveData<User> userLiveData = this.getItemViewModel().getUser();
        
        if (userLiveData != null) userLiveData.observe(this, this::updateView);
    }
    
    private void getUserItems() {
        this.getItemViewModel().getItemsByUserID(USER_ID).observe(this, this::updateItemsList);
    }
    
    private void createItem() {
        this.getItemViewModel().createItem(
                this.getBinding().todoListSpinner.getSelectedItemPosition(),
                this.getBinding().todoListEditText.getText().toString(),
                USER_ID);
        
        this.getBinding().todoListEditText.setText(null);
    }
    
    private void updateItem(Item pItem) {
        pItem.setIsSelected(!pItem.isSelected());
        
        this.getItemViewModel().updateItem(pItem);
    }
    
    private void deleteItem(Item pItem) {
        this.getItemViewModel().deleteItem(pItem);
    }
    
    private void configureRecyclerView() {
        this.setItemAdapter(new ItemAdapter(this));
        
        this.getBinding().todoListRecyclerView.setAdapter(this.getItemAdapter());
        this.getBinding().todoListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void updateView(User pUser) {
        if (pUser == null) return;
        DrawableCrossFadeFactory crossFadeFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        
        this.getBinding().todoListProfileText.setText(pUser.getUsername());
        Glide.with(this)
             .load("https://gravatar.com/avatar/" + pUser.getAvatar())
             .transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
             .diskCacheStrategy(DiskCacheStrategy.NONE)
             .placeholder(R.drawable.ic_image_dark)
             .apply(RequestOptions.circleCropTransform())
             .into(this.getBinding().todoListProfileImage);
    }
    
    private void updateItemsList(List<Item> pItems) {
        this.getItemAdapter().updateData(pItems);
    }
    
    private ActivityTodoListBinding getBinding() {
        return mBinding;
    }
    
    private void setBinding(ActivityTodoListBinding pBinding) {
        mBinding = pBinding;
    }
    
    public ItemViewModel getItemViewModel() {
        return mItemViewModel;
    }
    
    public void setItemViewModel(ItemViewModel pItemViewModel) {
        mItemViewModel = pItemViewModel;
    }
    
    public ItemAdapter getItemAdapter() {
        return mItemAdapter;
    }
    
    public void setItemAdapter(ItemAdapter pItemAdapter) {
        mItemAdapter = pItemAdapter;
    }
}