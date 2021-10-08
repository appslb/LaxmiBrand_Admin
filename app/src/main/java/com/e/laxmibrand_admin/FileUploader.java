package com.e.laxmibrand_admin;/*
package com.e.laxmibrand;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.app.sradesign.model.request.UpsertCatalogueDesignBase64Request;
import com.app.sradesign.model.request.UpsertCatalogueDesignRequest;
import com.app.sradesign.model.response.BaseResponse;
import com.app.sradesign.utils.Common;
import com.app.sradesign.utils.Utility;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploader {

    public FileUploaderCallback fileUploaderCallback;
    public int uploadIndex = -1;
    ArrayList<Uri> imgList;
    Context context;
    ArrayList<UpsertCatalogueDesignRequest> upsertDesignList;
    private File[] files;
    private long totalFileLength = 0;
    private long totalFileUploaded = 0;
    private String[] responses;


    public FileUploader() {

    }

    public void uploadFiles(Context context, ArrayList<Uri> imgList, ArrayList<UpsertCatalogueDesignRequest> upsertDesignList,
                            FileUploaderCallback fileUploaderCallback) {
        this.context = context;
        this.fileUploaderCallback = fileUploaderCallback;
        this.upsertDesignList = upsertDesignList;
        this.uploadIndex = -1;
        this.imgList = imgList;
        totalFileUploaded = 0;
        totalFileLength = 0;
        uploadIndex = -1;
        files = new File[imgList.size()];
        responses = new String[files.length];
        for (int i = 0; i < imgList.size(); i++) {
            File imgFile = new File(ImageFilePath.getPath(context, imgList.get(i)));
            try {
                float fileSizeKB = Float.parseFloat(String.valueOf(imgFile.length() / 1024));

                if (fileSizeKB > 1000) {
                    int quality = (int) (50000 / fileSizeKB);
                    imgFile = new Compressor(context).setQuality(quality).compressToFile(imgFile);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            files[i] = imgFile;
        }

        for (int i = 0; i < files.length; i++) {
            totalFileLength = totalFileLength + files[i].length();
        }
        uploadNext();
    }

    private void uploadNext() {
        if (files.length > 0) {
            if (uploadIndex != -1)
                totalFileUploaded = totalFileUploaded + files[uploadIndex].length();
            uploadIndex++;
            if (uploadIndex < files.length) {
//                uploadSingleFile(uploadIndex);
                ArrayList<UpsertCatalogueDesignRequest> singleDesign = new ArrayList<>();
                singleDesign.add(upsertDesignList.get(uploadIndex));
                Gson gson = new Gson();
                upsertCatalogueDesignAPI(gson.toJson(singleDesign));
            } else {
                fileUploaderCallback.onFinish(responses);
            }
        } else {
            fileUploaderCallback.onFinish(responses);
        }
    }

    private ArrayList<MultipartBody.Part> prepareFilePart(String partName, RequestBody requestFile) {

        ArrayList<MultipartBody.Part> imageFile = new ArrayList<>();

        File imgFile = files[uploadIndex];
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
        imageFile.add(MultipartBody.Part.createFormData(partName, imgFile.getName(), requestFile));

        return imageFile;
    }

    private void upsertCatalogueDesignAPI(String designListJSON) {

        RequestBody CatalogueDesignlist = RequestBody.create(MediaType.parse("multipart/form-data"), designListJSON);
        PRRequestBody fileBody = new PRRequestBody(files[uploadIndex]);
        ArrayList<MultipartBody.Part> body = prepareFilePart("file", fileBody);

        ///////////////

        UpsertCatalogueDesignRequest request = upsertDesignList.get(uploadIndex);
        String base64 = Utility.getBase64(Common.decodeUriToBitmap(context, imgList.get(uploadIndex)));


        UpsertCatalogueDesignBase64Request base64Request = new UpsertCatalogueDesignBase64Request(request.getCatalogueid(),
                request.getDesignno(), request.getShow_image(), base64);
        base64Request.setCatalogueid(request.getCatalogueid());
        base64Request.setDesignno(request.getDesignno());
        base64Request.setShow_image(request.getShow_image());
        base64Request.setBase64(base64);
        int uid = Utility.mPreferenceSettings().getUserId();
        base64Request.setRegisterid(String.valueOf(uid));
        String token = Utility.mPreferenceSettings().getFirebaseToken();
        base64Request.setAuthkey(token);
////////////
        RequestBody regID = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(uid));
        RequestBody authKey = RequestBody.create(MediaType.parse("multipart/form-data"), token);

//        Call<BaseResponse> upsertDesign = Utility.retroInterface().upsertCatalogueDesign(CatalogueDesignlist, body,
//                regID, authKey, map);
        Call<BaseResponse> upsertDesign = Utility.retroInterface().UpsertCatalogueDesignBase64(base64Request.getCatalogueid(),
                base64Request.getDesignno(), base64Request.getShow_image(), base64Request.getBase64(), base64Request.getExtension()
                , base64Request.getRegisterid(), base64Request.getAuthkey());
        upsertDesign.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        uploadNext();
                    } else {
                        fileUploaderCallback.onError();
                        if (response.body().getResponseMessage().equalsIgnoreCase("Authentication Key is wrong.")) {
                            Utility.showUnauthorisedDialog(context);
                        } else {
                            Toast.makeText(context, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Utility.somethingWentWrong(context);
                    fileUploaderCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.somethingWentWrong(context);
                fileUploaderCallback.onError();
            }
        });

    }


    public interface FileUploaderCallback {
        void onError();

        void onFinish(String[] responses);

        void onProgressUpdate(int currentpercent, int totalpercent, int filenumber);
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int) (100 * mUploaded / mTotal);
            int total_percent = (int) (100 * (totalFileUploaded + mUploaded) / totalFileLength);
            fileUploaderCallback.onProgressUpdate(current_percent, total_percent, uploadIndex + 1);
        }
    }

    public class PRRequestBody extends RequestBody {
        private static final int DEFAULT_BUFFER_SIZE = 2048;
        private File mFile;

        public PRRequestBody(final File file) {
            mFile = file;

        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            return MediaType.parse("multipart/form-data");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }

}
*/
