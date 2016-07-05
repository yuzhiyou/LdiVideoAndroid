package com.ldi.android.Activitys;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.App_;
import com.ldi.android.BuildConfig;
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

    @ViewById(R.id.btn_ok)
    Button btn_ok;

    @AfterViews
    void afterview() {
        setTitle(R.id.navigation_bar_back_tv, R.string.change_info);
    }

    @Click({R.id.navigation_bar_back_ib, R.id.btn_ok, R.id.iv_photo})
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
            case R.id.iv_photo: {
                onPickFileClick();
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

        //container.removeView(uploadProgressHolders.get(uploadInfo.getUploadId()).itemView);
        uploadProgressHolders.remove(uploadInfo.getUploadId());
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

}
