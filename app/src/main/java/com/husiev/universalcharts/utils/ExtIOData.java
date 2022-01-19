package com.husiev.universalcharts.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExtIOData {

    /**
     * Get the directory for the app's private directory.
     * @param context Interface to global information about an application environment.
     * @param pathname the path to chosen directory.
     * @return false if the directory was created, true on failure (hope it will not happens) or if the directory already existed.
     */
    public static boolean getExternalStorageDir(Context context, String pathname) {
        return !(new File(context.getExternalFilesDir(null), pathname)).mkdirs();
    }

    /**
     * Create a path where we will place our private file on external storage.
     * @param context interface to global information about an application environment.
     * @param filename the name of creating file.
     * @param data saving data.
     * @param append set to true to append data to a file, or set to false to replace all data.
     */
    public static void saveDataToFile(Context context, String filename, byte[] data, boolean append) {
        File file = new File(context.getExternalFilesDir(null), filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, append);
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (IOException ex) {
            Log.w("ExternalStorage", "Error writing " + file, ex);
        }
    }

    /**
     * Get the list of directories in the app's private directory.
     * @param context Interface to global information about an application environment.
     * @return List of directories in app's dir.
     */
    public static List<String> getListOfDirs(Context context, String path) {
        List<String> listOfDirs = new ArrayList<>();
        File file = new File(context.getExternalFilesDir(null), path);
        File[] files = file.listFiles();

        if (files == null)
            return listOfDirs;

        for (File inFile : files) {
            if (inFile.isDirectory()) {
                listOfDirs.add(inFile.getName());
            }
        }

        return listOfDirs;
    }

    /**
     * Read data from file line by line.
     * @param context Interface to global information about an application environment.
     * @param filename The name of file to be read.
     * @return List of read lines in file.
     */
    public static List<String> readLinesFromFile(Context context, String filename) {
        File file = new File(context.getExternalFilesDir(null), filename);

        try {
            // Open the file
            InputStream stream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            List<String> data = new ArrayList<>();

            // Read data from file
            String line = br.readLine();
            while (line != null) {
                data.add(line);
                line = br.readLine();
            }

            //Close the input stream
            br.close();
            return data;
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error reading " + file, e);
            return null;
        }
    }
}
