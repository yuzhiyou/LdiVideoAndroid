package com.ldi.android.Activitys;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.App_;
import com.ldi.android.BuildConfig;
import com.ldi.android.Constants;
import com.ldi.android.R;
import com.ldi.android.Utils.LogUtils;
import com.nononsenseapps.filepicker.FilePickerActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EActivity(R.layout.activity_change_profile)
public class ChangeProfileActivity extends BaseActivity implements UploadStatusDelegate {

    private static final int FILE_CODE = 100;
    private static final String USER_AGENT = "LDIVideo/" + BuildConfig.VERSION_NAME;
    private String filesToUpload = "";
    private Map<String, UploadProgressViewHolder> uploadProgressHolders = new HashMap<>();
    private AlertDialog mTakeDialog;
    private String mPhotoPath;// Photo from Camera.
    private String mPhotoPathCrop; //Photo from crop.
    public static final int REQUEST_PICK_IMAGE = 1001;
    public static final int REQUEST_TAKE_PHOTO = 1002;
    public static final int REQUEST_CROP = 1003;
    private String FILE_PATH_TEMP = "";

    @ViewById(R.id.btn_ok)
    Button btn_ok;

    @ViewById(R.id.iv_icon)
    ImageView iv_icon;

    @AfterViews
    void afterview() {
        setTitle(R.id.navigation_bar_back_tv, R.string.change_info);
        FILE_PATH_TEMP = Environment.getExternalStorageDirectory().toString() + Constants.USER_PHOTO_SAVE_PATH;
    }

    @Click({R.id.navigation_bar_back_ib, R.id.btn_ok, R.id.iv_icon})
    void click(View v) {
        switch (v.getId()) {
            case R.id.navigation_bar_back_ib: {  //导航条返回
                finish();
                break;
            }
            case R.id.btn_ok: {
                onMultipartUploadClick();
                break;
            }
            case R.id.iv_icon: {
                showPhotoWayDialog();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {
        LogUtils.i(String.format(Locale.getDefault(), "ID: %1$s (%2$d%%) at %3$.2f Kbit/s",
                uploadInfo.getUploadId(), uploadInfo.getProgressPercent(),
                uploadInfo.getUploadRate()));
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());

        if (uploadProgressHolders.get(uploadInfo.getUploadId()) == null)
            return;

        uploadProgressHolders.get(uploadInfo.getUploadId())
                .progressBar.setProgress(uploadInfo.getProgressPercent());
    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {
        LogUtils.i("Error with ID: " + uploadInfo.getUploadId() + ": "
                + exception.getLocalizedMessage());
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());

        if (uploadProgressHolders.get(uploadInfo.getUploadId()) == null)
            return;

        //container.removeView(uploadProgressHolders.get(uploadInfo.getUploadId()).itemView);
        uploadProgressHolders.remove(uploadInfo.getUploadId());
    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
        LogUtils.i(String.format(Locale.getDefault(),
                "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                serverResponse.getBodyAsString()));
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());
        for (Map.Entry<String, String> header : serverResponse.getHeaders().entrySet()) {
            Log.i("Header", header.getKey() + ": " + header.getValue());
        }

        if (uploadProgressHolders.get(uploadInfo.getUploadId()) == null)
            return;

        uploadProgressHolders.get(uploadInfo.getUploadId())
                .progressBar.setProgress(uploadInfo.getProgressPercent());
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {
        LogUtils.i("Upload with ID " + uploadInfo.getUploadId() + " is cancelled");
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());

        if (uploadProgressHolders.get(uploadInfo.getUploadId()) == null)
            return;

        //container.removeView(uploadProgressHolders.get(uploadInfo.getUploadId()).itemView);
        uploadProgressHolders.remove(uploadInfo.getUploadId());
    }

    private void logSuccessfullyUploadedFiles(List<String> files) {
        for (String file : files) {
            LogUtils.i("Success:" + file);
        }
    }

    private UploadNotificationConfig getNotificationConfig(String filename) {

        return new UploadNotificationConfig()
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(filename)
                .setInProgressMessage(getString(R.string.uploading))
                .setCompletedMessage(getString(R.string.upload_success))
                .setErrorMessage(getString(R.string.upload_error))
                .setAutoClearOnSuccess(false)
                .setClickIntent(new Intent(this, ChangeProfileActivity_.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    private void addUploadToList(String uploadID, String filename) {
        View uploadProgressView = getLayoutInflater().inflate(R.layout.view_upload_progress, null);
        UploadProgressViewHolder viewHolder = new UploadProgressViewHolder(uploadProgressView, filename);
        viewHolder.uploadId = uploadID;
        //container.addView(viewHolder.itemView, 0);
        uploadProgressHolders.put(uploadID, viewHolder);
    }

    void onPickFileClick() {
        // Starts NoNonsense-FilePicker (https://github.com/spacecowboy/NoNonsense-FilePicker)
        Intent intent = new Intent(this, FilePickerActivity.class);

        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        intent.putExtra(FilePickerActivity.EXTRA_START_PATH,
                Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(intent, FILE_CODE);
    }


    class UploadProgressViewHolder {
        View itemView;

        TextView uploadTitle;
        ProgressBar progressBar;
        Button cancelUploadButton;

        String uploadId;

        UploadProgressViewHolder(View view, String filename) {
            itemView = view;
            setContentView(itemView);
            uploadTitle = (TextView) view.findViewById(R.id.uploadTitle);
            progressBar = (ProgressBar) view.findViewById(R.id.uploadProgress);
            cancelUploadButton = (Button) view.findViewById(R.id.cancelUploadButton);

            progressBar.setMax(100);
            progressBar.setProgress(0);

            uploadTitle.setText(getString(R.string.upload_progress, filename));
            cancelUploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uploadId == null)
                        return;

                    UploadService.stopUpload(uploadId);
                }
            });
        }

    }

    private String getFilename(String filepath) {
        if (filepath == null)
            return null;

        final String[] filepathParts = filepath.split("/");

        return filepathParts[filepathParts.length - 1];
    }

    @OnActivityResult(FILE_CODE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<Uri> resultUris = new ArrayList<>();

            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            resultUris.add(clip.getItemAt(i).getUri());
                        }
                    }

                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            resultUris.add(Uri.parse(path));
                        }
                    }
                }
            } else {
                resultUris.add(data.getData());
            }

            StringBuilder absolutePathsConcat = new StringBuilder();
            for (Uri uri : resultUris) {
                if (absolutePathsConcat.length() == 0) {
                    absolutePathsConcat.append(new File(uri.getPath()).getAbsolutePath());
                } else {
                    absolutePathsConcat.append(",").append(new File(uri.getPath()).getAbsolutePath());
                }
            }
            filesToUpload = absolutePathsConcat.toString();
        }
    }

    void onMultipartUploadClick() {
        final String serverUrlString = "http://posttestserver.com/post.php";
        final String paramNameString = "123.png";


        final String[] filesToUploadArray = filesToUpload.split(",");

        for (String fileToUploadPath : filesToUploadArray) {
            try {
                final String filename = getFilename(fileToUploadPath);

                MultipartUploadRequest req = new MultipartUploadRequest(this, serverUrlString)
                        .addFileToUpload(fileToUploadPath, paramNameString)
                        .setNotificationConfig(getNotificationConfig(filename))
                        .setCustomUserAgent(USER_AGENT)
                        .setAutoDeleteFilesAfterSuccessfulUpload(false)
                        .setUsesFixedLengthStreamingMode(false)
                        .addParameter("u_id", String.valueOf(App_.getInstance().mUser.getU_id()))
                        .setMaxRetries(3);


                req.setUtf8Charset();

                String uploadID = req.setDelegate(this).startUpload();

                addUploadToList(uploadID, filename);

                // these are the different exceptions that may be thrown
            } catch (FileNotFoundException exc) {
                showToast(exc.getMessage());
            } catch (IllegalArgumentException exc) {
                showToast("Missing some arguments. " + exc.getMessage());
            } catch (MalformedURLException exc) {
                showToast(exc.getMessage());
            }
        }
    }

    private void showPhotoWayDialog() {
        mTakeDialog = new AlertDialog.Builder(this).create();
        mTakeDialog.show();
        Window window = mTakeDialog.getWindow();
        window.setContentView(R.layout.take_photo_dialog);
        TextView cameraTv = (TextView) window.findViewById(R.id.tv_content1);
        cameraTv.setText(getResources().getString(R.string.text_photo_camera));
        cameraTv.setTag(iv_icon.getId() + "");
        TextView pickTv = (TextView) window.findViewById(R.id.tv_content2);
        pickTv.setText(getResources().getString(R.string.text_photo_pick));
        pickTv.setTag(iv_icon.getId() + "");
        TextView cancelTv = (TextView) window.findViewById(R.id.tv_content3);
        cancelTv.setText(getResources().getString(R.string.text_photo_cancel));

        cameraTv.setOnClickListener(takePhotoListener);
        pickTv.setOnClickListener(takePhotoListener);
        cancelTv.setOnClickListener(takePhotoListener);
    }

    View.OnClickListener takePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_content1) {
                takePhoto((String) view.getTag());
            } else if (view.getId() == R.id.tv_content2) {
                pickPicture((String) view.getTag());
            }
            mTakeDialog.cancel();
        }
    };

    private String mCurrCorpImageId;

    private void takePhoto(String imgId) {
        File photoFile = new File(getFILE_PATH_TEMP(), String.format("%s.png", System.currentTimeMillis()));
        mPhotoPath = photoFile.getAbsolutePath();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mCurrCorpImageId = imgId;
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void pickPicture(String imgId) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mCurrCorpImageId = imgId;
        startActivityForResult(i, REQUEST_PICK_IMAGE);
    }

    @OnActivityResult(REQUEST_CROP)
    void onCropResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (mPhotoPathCrop != null && new File(mPhotoPathCrop).exists()) {
                setImageResource_(Uri.fromFile(new File(mPhotoPathCrop)));
            }
        }

    }

    @OnActivityResult(REQUEST_PICK_IMAGE)
    void onPickImgResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            filesToUpload = picturePath;
            // 选择的图片的Uri
            Uri uri = Uri.fromFile(new File(picturePath));
            startPhotoZoom(uri, 480, mCurrCorpImageId);
        }
    }

    @OnActivityResult(REQUEST_TAKE_PHOTO)
    void onTakePhotoResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri uri = Uri.fromFile(new File(mPhotoPath));
            startPhotoZoom(uri, 480, mCurrCorpImageId);
        }

    }


    private void startPhotoZoom(Uri uri1, int size, String imgId) {
        // 保存最终的截图
        File photoFileCorp = new File(getFILE_PATH_TEMP(), String.format("%s.jpg", System.currentTimeMillis())); // 剪切后存储的图片
        mPhotoPathCrop = photoFileCorp.getAbsolutePath();
        filesToUpload = mPhotoPathCrop;

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPhotoPathCrop))); // 剪切完输出路径
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);

        startActivityForResult(intent, REQUEST_CROP);
    }

    /**
     * 设置图片资源
     */
    private void setImageResource_(Uri uri) {
        String filePath = uri.getPath();
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        iv_icon.setImageBitmap(bm);
    }

    private String getFILE_PATH_TEMP() {
        File filePath = new File(FILE_PATH_TEMP);
        if (!filePath.isDirectory()) {
            filePath.mkdir();
        }
        return FILE_PATH_TEMP;
    }

}
