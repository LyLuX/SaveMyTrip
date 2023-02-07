package com.lylux.savemytrip.Utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lylux.savemytrip.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Objects;

public class StorageUtils {
    
    @NonNull
    public static File createOrGetFile(File pDestination, String pFileName, String pFolderName) {
        File folder = new File(pDestination, pFolderName);
        
        return new File(folder, pFileName);
    }
    
    @NonNull
    public static File getFileFromStorage(File pRootDestination, Context pContext, String pFilename, String pFolderName) {
        return createOrGetFile(pRootDestination, pFolderName, pFolderName);
    }
    
    public static String getTextFromStorage(File pRootDestination, Context pContext, String pFileName, String pFolderName) {
        File file = createOrGetFile(pRootDestination, pFileName, pFolderName);
        
        return readOnFile(pContext, file);
    }
    
    public static void setTextInStorage(File pRootDestination, Context pContext, String pFileName, String pFolderName, String pText) {
        File file = createOrGetFile(pRootDestination, pFileName, pFolderName);
        
        writeOnFile(pContext, pText, file);
    }
    
    // ----------------------
    // External Storage tests
    // ----------------------
    
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        
        return (Environment.MEDIA_MOUNTED.equals(state));
    }
    
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
    
    // -------------------------
    // Read and Write on storage
    // -------------------------
    
    public static String readOnFile(Context pContext, @NonNull File pFile) {
        String result = null;
        
        if (pFile.exists()) {
            BufferedReader reader;
            
            try {
                reader = new BufferedReader(new FileReader(pFile));
                try {
                    StringBuilder builder = new StringBuilder();
                    String        line    = reader.readLine();
                    
                    while (line != null) {
                        builder.append(line);
                        builder.append("\n");
                        
                        line = reader.readLine();
                    }
                    
                    result = builder.toString();
                } finally {
                    reader.close();
                }
            } catch (IOException pE) {
                throw new RuntimeException(pE);
            }
        }
        
        return result;
    }
    
    public static void writeOnFile(Context pContext, String pText, @NonNull File pFile) {
        try {
            Objects.requireNonNull(pFile.getParentFile()).mkdirs();
            FileOutputStream outputStream = new FileOutputStream(pFile);
            
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                writer.write(pText);
                writer.flush();
                outputStream.getFD().sync();
            } finally {
                
                Toast.makeText(pContext, pContext.getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException pE) {
            throw new RuntimeException(pE);
        }
    }
}
