package com.lylux.savemytrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lylux.savemytrip.databinding.ActivityMainBinding;
import com.lylux.savemytrip.todolist.TodoListActivity;
import com.lylux.savemytrip.tripbook.TripBookActivity;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding mBinding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setBinding(ActivityMainBinding.inflate(this.getLayoutInflater()));
        setContentView(this.getBinding().getRoot());
        
        this.getBinding().mainButtonTodoList.setOnClickListener((View v) -> {
            Intent todoList = new Intent(this, TodoListActivity.class);
            
            startActivity(todoList);
        });
        
        this.getBinding().mainButtonTripBook.setOnClickListener((View v) -> {
            Intent tripBook = new Intent(this, TripBookActivity.class);
            
            startActivity(tripBook);
        });
    }
    
    private ActivityMainBinding getBinding() {
        return mBinding;
    }
    
    private void setBinding(ActivityMainBinding pBinding) {
        mBinding = pBinding;
    }
}