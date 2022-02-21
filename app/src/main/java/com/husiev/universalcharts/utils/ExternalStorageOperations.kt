package com.husiev.universalcharts.utils

import android.util.Log
import java.io.*

class ExternalStorageOperations {
    companion object {

        /**
         * Get the directory for the app's private directory.
         * @param rootDirectory the absolute path to the directory on the primary shared/external storage device where the application can place persistent files it owns.
         * @param pathname the path to chosen directory.
         * @return true if the directory was created, false on failure (hope it will not happens) or if the directory already existed.
         */
        fun createDirectory(rootDirectory: File, pathname: String): Boolean {
            return File(rootDirectory, pathname).mkdirs()
        }

        /**
         * Create a path where we will place our private file on external storage.
         * @param rootDirectory the absolute path to the directory on the primary shared/external storage device where the application can place persistent files it owns.
         * @param filename the name of creating file.
         * @param data saving data.
         * @param append set to true to append data to a file, or set to false to replace all data.
         */
        fun saveDataToFile(rootDirectory: File, filename: String, data: ByteArray?, append: Boolean) {
            val file = File(rootDirectory, filename)
            try {
                val fileOutputStream = FileOutputStream(file, append)
                fileOutputStream.write(data)
                fileOutputStream.close()
            } catch (ex: IOException) {
                Log.w("ExternalStorage", "Error writing $file", ex)
            }
        }

        /**
         * Get the list of directories in the app's private directory.
         * @param rootDirectory the absolute path to the directory on the primary shared/external storage device where the application can place persistent files it owns.
         * @return List of directories in app's dir.
         */
        fun getListOfDirs(rootDirectory: File, path: String): List<String> {
            val listOfDirs: MutableList<String> = ArrayList()
            val file = File(rootDirectory, path)
            val files = file.listFiles() ?: return listOfDirs
            for (inFile in files) {
                if (inFile.isDirectory) {
                    listOfDirs.add(inFile.name)
                }
            }
            return listOfDirs
        }

        /**
         * Read data from file line by line.
         * @param rootDirectory the absolute path to the directory on the primary shared/external storage device where the application can place persistent files it owns.
         * @param filename The name of file to be read.
         * @return List of read lines in file.
         */
        fun readLinesFromFile(rootDirectory: File, filename: String): List<String>? {
            val file = File(rootDirectory, filename)
            return try {
                // Open the file
                val stream: InputStream = FileInputStream(file)
                val br = BufferedReader(InputStreamReader(stream))
                val data: MutableList<String> = java.util.ArrayList()

                // Read data from file
                var line = br.readLine()
                while (line != null) {
                    data.add(line)
                    line = br.readLine()
                }

                //Close the input stream
                br.close()
                data
            } catch (e: IOException) {
                // Unable to create file, likely because external storage is
                // not currently mounted.
                Log.w("ExternalStorage", "Error reading $file", e)
                null
            }
        }

        fun deleteDirectory(rootDirectory: File, path: String) {
            val file = File(rootDirectory, path)
            deleteRecursive(file)
        }

        private fun deleteRecursive(fileOrDirectory: File) {
            try {
                if (fileOrDirectory.isDirectory)
                    for (child in fileOrDirectory.listFiles())
                        deleteRecursive(child)
                fileOrDirectory.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}