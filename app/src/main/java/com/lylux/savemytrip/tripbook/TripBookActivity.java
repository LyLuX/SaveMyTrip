package com.lylux.savemytrip.tripbook;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.lylux.savemytrip.R;
import com.lylux.savemytrip.Utils.StorageUtils;
import com.lylux.savemytrip.databinding.ActivityTripBookBinding;

import java.io.File;

public class TripBookActivity extends AppCompatActivity {
    
    private static final String                                 AUTHORITY              = "com.lylux.savemytrip.FileProvider";
    private static final String                                 FILENAME               = "tripBook.txt";
    private static final String                                 FOLDERNAME             = "bookTrip";
    private static final int                                    RC_STORAGE_WRITE_PERMS = 100;
    private              ActivityTripBookBinding                mBinding;
    private              CompoundButton.OnCheckedChangeListener mChangeListener;
    
    /**
     * {@inheritDoc}
     * <p>
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setBinding(ActivityTripBookBinding.inflate(this.getLayoutInflater()));
        setContentView(this.getBinding().getRoot());
        
        this.setupCompoundButtonCheckedChangeListener();
        this.initView();
        this.readFromStorage();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == RC_STORAGE_WRITE_PERMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.readFromStorage();
            }
        }
    }
    
    /**
     * Initialize the contents of the Activity's standard options pMenu.  You should place your pMenu items in to
     * <var>pMenu</var>.
     *
     * <p>This is only called once, the first time the options pMenu is
     * displayed.  To update the pMenu every time it is displayed, see {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the pMenu with standard system
     * pMenu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that they will be correctly ordered with
     * application-defined pMenu items. Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>pMenu</var> (and any items created
     * from it), making modifications to it as desired, until the next time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the pMenu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param pMenu The options pMenu in which you place your items.
     * @return You must return true for the pMenu to be displayed; if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        MenuInflater inflater = this.getMenuInflater();
        
        inflater.inflate(R.menu.menu, pMenu);
        return true;
    }
    
    /**
     * This hook is called whenever an pMenuItem in your options menu is selected. The default implementation simply returns false to have
     * the normal processing happen (calling the pMenuItem's Runnable or sending a message to its Handler as appropriate).  You can use this
     * method for any items for which you would like to do processing without those other facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param pMenuItem The menu pMenuItem that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem pMenuItem) {
        int itemID = pMenuItem.getItemId();
        
        if (itemID == R.id.action_share) {
            this.shareFile();
            
            return true;
        } else if (itemID == R.id.action_save) {
            this.save();
            
            return true;
        }
        
        return super.onOptionsItemSelected(pMenuItem);
    }
    
    protected void readFromStorage() {
        if (checkWriteExternalStoragePermission()) return;
        
        if (this.getBinding().tripBookRadioExternal.isChecked()) {
            File directory;
            
            if (StorageUtils.isExternalStorageReadable()) {
                if (this.getBinding().tripBookRadioPublic.isChecked()) {
                    directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                } else {
                    directory = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                }
                
                this.getBinding().tripBookEditText.setText(StorageUtils.getTextFromStorage(directory, this, FILENAME, FOLDERNAME));
            }
        } else {
            File directory;
            
            if (this.getBinding().tripBookRadioVolatile.isChecked()) {
                directory = this.getCacheDir();
            } else {
                directory = this.getFilesDir();
            }
            
            this.getBinding().tripBookEditText.setText(StorageUtils.getTextFromStorage(directory, this, FILENAME, FOLDERNAME));
        }
    }
    
    protected boolean checkWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ WRITE_EXTERNAL_STORAGE }, RC_STORAGE_WRITE_PERMS);
            
            return true;
        }
        
        return false;
    }
    
    protected void save() {
        if (this.getBinding().tripBookRadioExternal.isChecked()) {
            this.writeOnExternalStorage();
        } else {
            this.writeOnInternalStorage();
        }
    }
    
    protected void shareFile() {
        File   internalFile  = StorageUtils.getFileFromStorage(this.getFilesDir(), this, FILENAME, FOLDERNAME);
        Uri    contentUri    = FileProvider.getUriForFile(this.getApplicationContext(), AUTHORITY, internalFile);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        
        sharingIntent.setType("text/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        
        startActivity(Intent.createChooser(sharingIntent, this.getString(R.string.trip_book_share)));
    }
    
    protected void writeOnExternalStorage() {
        if (StorageUtils.isExternalStorageWritable()) {
            File directory;
            
            if (this.getBinding().tripBookRadioPublic.isChecked()) {
                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            } else {
                directory = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            }
            
            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, this.getBinding().tripBookEditText.getText().toString());
        } else {
            Toast.makeText(this, this.getString(R.string.external_storage_impossible_create_file), Toast.LENGTH_SHORT).show();
        }
    }
    
    protected void writeOnInternalStorage() {
        File directory;
        
        if (this.getBinding().tripBookRadioVolatile.isChecked()) {
            directory = this.getCacheDir();
        } else {
            directory = this.getFilesDir();
        }
        
        StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, this.getBinding().tripBookEditText.getText().toString());
    }
    
    private void setupCompoundButtonCheckedChangeListener() {
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (isChecked) {
                int id = buttonView.getId();
                
                if (id == R.id.trip_book_radio_internal) {
                    getBinding().tripBookExternalChoice.setVisibility(View.GONE);
                    getBinding().tripBookInternalChoice.setVisibility(View.VISIBLE);
                } else if (id == R.id.trip_book_radio_external) {
                    getBinding().tripBookExternalChoice.setVisibility(View.VISIBLE);
                    getBinding().tripBookInternalChoice.setVisibility(View.GONE);
                }
            }
            
            this.readFromStorage();
        };
        
        this.setChangeListener(listener);
    }
    
    private void initView() {
        ActionBar actionBar = this.getSupportActionBar();
        
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        
        this.getBinding().tripBookRadioExternal.setOnCheckedChangeListener(this.getChangeListener());
        this.getBinding().tripBookRadioInternal.setOnCheckedChangeListener(this.getChangeListener());
        
        this.getBinding().tripBookRadioPrivate.setOnCheckedChangeListener(this.getChangeListener());
        this.getBinding().tripBookRadioPublic.setOnCheckedChangeListener(this.getChangeListener());
        
        this.getBinding().tripBookRadioNormal.setOnCheckedChangeListener(this.getChangeListener());
        this.getBinding().tripBookRadioVolatile.setOnCheckedChangeListener(this.getChangeListener());
    }
    
    protected CompoundButton.OnCheckedChangeListener getChangeListener() {
        return mChangeListener;
    }
    
    protected void setChangeListener(CompoundButton.OnCheckedChangeListener pChangeListener) {
        mChangeListener = pChangeListener;
    }
    
    private ActivityTripBookBinding getBinding() {
        return mBinding;
    }
    
    private void setBinding(ActivityTripBookBinding pBinding) {
        mBinding = pBinding;
    }
}
