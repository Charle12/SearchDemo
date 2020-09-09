package com.appGarrage.search.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import com.appGarrage.search.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class FileUtils {
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    static int COMPRESSED_IMAGE_WIDTH = 1450;
    static int COMPRESSED_IMAGE_HEIGHT = 900;
    static int COMPRESSED_IMAGE_QUALITY = 90;

    private FileUtils() {

    }

    public static File  mergeFile(ArrayList<File> files){
        for (int i=0; i< files.size(); i++){

        }
        return null ;

    }

    /**
     * @param imageFile {@link File}
     * @param reqWidth  int
     * @param reqHeight int
     * @return Bitmap
     */
    static Bitmap decodeSampledBitmapFromFile(File imageFile, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to checkpictureFile dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        Matrix matrix = new Matrix();
        int orientation = getOrientation(imageFile.getAbsolutePath());
        matrix.postRotate(orientation);

        if (scaledBitmap != null) {
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        }
        return scaledBitmap;
    }


    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    private static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        //noinspection ConstantConditions
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = uri.getPath();
            assert result != null;
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtils", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtils", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private static File compressedPath;


    public static double getFileSizeInMb(File file) {
        try {
            if (file != null) {
                // Get length of file in bytes
                double fileSizeInBytes = file.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                double fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                Log.e("fileSizeInMB", "" + fileSizeInKB / 1024);
                return fileSizeInKB / 1024;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }

    public static void copyFile(File source, File dest) {
        if (source != null && dest != null) {
            try (FileChannel sourceChannel = new FileInputStream(source).getChannel(); FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFile(File file) {
        try {
            if (file.exists()) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File file1 : files) {
                        if (file1.isDirectory()) {
                            deleteFile(file1);
                        } else {
                            file1.delete();
                        }
                    }
                }
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static File compressFile(Context context, File originalFile) {
        try {
            return new Compressor(context)
                    .setMaxWidth(COMPRESSED_IMAGE_WIDTH)
                    .setMaxHeight(COMPRESSED_IMAGE_HEIGHT)
                    .setQuality(COMPRESSED_IMAGE_QUALITY)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(compressedPath.getAbsolutePath())
                    .compressToFile(originalFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static File compressFileWithName(Context context, File originalFile, String name) {
        try {
            return new Compressor(context)
                    .setMaxWidth(COMPRESSED_IMAGE_WIDTH)
                    .setMaxHeight(COMPRESSED_IMAGE_HEIGHT)
                    .setQuality(COMPRESSED_IMAGE_QUALITY)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(compressedPath.getAbsolutePath())
                    .compressToFile(originalFile, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File getAlbumDir(Context mContext, String imageID) {
        File storageDir;
        File appointmentDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // Check that the SDCard is mounted
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mContext.getString(R.string.app_name));
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    return null;
                }
            }
            appointmentDir = new File(storageDir, imageID);
            if (!appointmentDir.mkdirs()) {
                if (!appointmentDir.exists()) {
                    return null;
                }
            }
            compressedPath = new File(appointmentDir, imageID);
            if (!compressedPath.mkdir()) {
                if (!compressedPath.exists()) {
                    return null;
                }
            }
            createNoMedia("/" + mContext.getString(R.string.app_name) + "/" + imageID + "/.nomedia");


        }
        return appointmentDir;
    }

    private static void createNoMedia(String path) {
        File fileNoMedia = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + path);
        try {
            if (!fileNoMedia.exists()) //noinspection ResultOfMethodCallIgnored
                fileNoMedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static File createImageFile(Context context, String imageID) {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File albumF = getAlbumDir(context, imageID);
        // using File.createTempFile create 0 mb size file
        return new File(albumF, imageFileName + JPEG_FILE_SUFFIX);
    }


    /**
     * compress related methods
     *
     * @param imageFile       {@link File}
     * @param reqWidth        int
     * @param reqHeight       int
     * @param compressFormat  enum
     * @param quality         int
     * @param destinationPath {@link String}
     * @return File
     */
    static File compressImage(File imageFile, int reqWidth, int reqHeight, Bitmap.CompressFormat compressFormat, int quality, String destinationPath) throws IOException {
        FileOutputStream fileOutputStream = null;
        File file = new File(destinationPath).getParentFile();
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(destinationPath);
            // write the compressed bitmap at the destination specified by destinationPath.
            Bitmap bitmap = decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight);
            if (bitmap != null) {
                bitmap.compress(compressFormat, quality, fileOutputStream);
            }
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return new File(destinationPath);
    }



    public static int getOrientation(String filePath) {
        //check the rotation of the image and display it properly
        ExifInterface exif;
//        Matrix matrix = new Matrix();
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
//            matrix.postRotate(90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
//            matrix.postRotate(180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
//            matrix.postRotate(270);
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // if out of memory, return original bitmap
            }
        }
        return bitmap;
    }

    /**
     * @param options   {@link BitmapFactory.Options}
     * @param reqWidth  int
     * @param reqHeight int
     * @return int
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String getImagePathFromInputStreamUri(Uri uri, Context pContext) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = pContext.getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(inputStream, pContext);

                filePath = photoFile.getPath();

            } catch (IOException e) {
                // log
            } finally {
                try {
                    assert inputStream != null;
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    private static File createTemporalFileFrom(InputStream inputStream, Context pContext) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile(pContext);
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private static File createTemporalFile(Context pContext) {
        return new File(pContext.getExternalCacheDir(), "tempFile.jpg"); // context needed
    }

    public static File createFileFromBitmap(File sourceFile, Bitmap bitmap) {
        if (bitmap == null)
            return null;
        try {
            FileOutputStream oStream = new FileOutputStream(sourceFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();//https://docs.google.com/spreadsheets/d/1OhqiTBo7wRaPs6E3SdSJfUZIJdJLTWIs78NMiyC8wfM/edit?ts=5b92a79c#gid=0
            Log.i("TAG", "There was an issue saving the image.");
        }
        getFileSizeInMb(sourceFile);
        return sourceFile;
    }

    public static Bitmap getBitmapFromPath(String absoluteFilePath) {
        try {
            return getResizedBitmap(BitmapFactory.decodeFile(absoluteFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Bitmap getBitmap(ArrayList<String> imagePaths) {
        int count = 0;
        // Bitmap bitmap=getBitmapFromPath(imagePaths.get(0));
        Bitmap bitmap = getBitmapFromPath(imagePaths.get(count));
        count++;

        // imagePaths.remove(0);
        while (imagePaths.size() > count) {
            assert bitmap != null;
            try {
                bitmap = createSingleImageFromMultipleImages(bitmap, Objects.requireNonNull(getBitmapFromPath(imagePaths.get(count))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            count++;
            //imagePaths.remove(0);
        }
        return bitmap;
    }

    private static Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {
      /*  firstImage=getResizedBitmap(firstImage, 1080, 720);
        secondImage=getResizedBitmap(secondImage, 1080, 720);*/
        int WIDTH_IMAGE = Math.max(firstImage.getWidth(), secondImage.getWidth());
        int HIGHT_IMAGE = firstImage.getHeight() + secondImage.getHeight();
        Bitmap result = Bitmap.createBitmap(WIDTH_IMAGE, HIGHT_IMAGE, firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 10, firstImage.getHeight() + 10, null);
        result = getResizedBitmap(result);
        return result;
    }

    private static Bitmap getResizedBitmap(Bitmap bm) {
        try {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) 720) / width;
            scaleWidth = scaleWidth < 1 ? scaleWidth : 1;
            // float scaleHeight = ((float) newHeight) / height;
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(scaleWidth, scaleWidth);
            // recreate the new Bitmap
            return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File mergeFiles(ArrayList<File> files, Context context){
        ArrayList<String> paths = new ArrayList<>();
        for (int i=0; i< files.size(); i++){
            paths.add(files.get(i).getAbsolutePath());
        }
        Bitmap bitmap= getBitmap(paths);
        return   createFileFromBitmap(createTemporalFile(context), bitmap);
    }
}
