package com.ldi.android.Activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.Constants;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.io.File;


@EActivity(R.layout.activity_certification)
public class CertificationActivity extends BaseActivity {
    private AlertDialog mTakeDialog;
    private String mPhotoPath;// Photo from Camera.
    private String mPhotoPathCrop; //Photo from crop.
    public static final int REQUEST_PICK_IMAGE = 1001;
    public static final int REQUEST_TAKE_PHOTO = 1002;
    public static final int REQUEST_CROP = 1003;
    private String FILE_PATH_TEMP = "";
    private String ID_front_path = "";
    private String ID_back_path = "";

    @ViewById(R.id.et_name)
    EditText et_name;

    @ViewById(R.id.et_id)
    EditText et_id;

    @ViewById(R.id.btn_submit)
    Button btn_submit;

    @ViewById(R.id.iv_front_select)
    ImageView iv_front_select;

    @ViewById(R.id.iv_back_select)
    ImageView iv_back_select;

    @ViewById(R.id.iv_id_front)
    ImageView iv_id_front;

    @ViewById(R.id.iv_id_back)
    ImageView iv_id_back;

    @ViewById(R.id.ll_front)
    LinearLayout ll_front;

    @ViewById(R.id.ll_back)
    LinearLayout ll_back;

    @AfterViews
    void afterview() {
        setTitle(R.id.navigation_bar_back_tv, R.string.certification);
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(CertificationActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        FILE_PATH_TEMP = Environment.getExternalStorageDirectory().toString() + Constants.USER_PHOTO_SAVE_PATH;

                    }

                    @Override
                    public void onDenied(String permission) {
                        showToast("请您允许应用访问存储设备");
                    }
                }
        );
    }

    @Click({R.id.navigation_bar_back_ib, R.id.iv_front_select, R.id.iv_back_select, R.id.iv_id_front, R.id.iv_id_back})
    void click(View v) {
        switch (v.getId()) {
            case R.id.navigation_bar_back_ib:
                finish();
                break;
            case R.id.iv_front_select:
            case R.id.iv_id_front:
                showPhotoWayDialog(iv_front_select);
                break;
            case R.id.iv_back_select:
            case R.id.iv_id_back:
                showPhotoWayDialog(iv_back_select);
                break;
            default:
                break;
        }
    }

    private void showPhotoWayDialog(View v) {
        mTakeDialog = new AlertDialog.Builder(this).create();
        mTakeDialog.show();
        Window window = mTakeDialog.getWindow();
        window.setContentView(R.layout.take_photo_dialog);
        TextView cameraTv = (TextView) window.findViewById(R.id.tv_content1);
        cameraTv.setText(getResources().getString(R.string.text_photo_camera));
        cameraTv.setTag(v.getId() + "");
        TextView pickTv = (TextView) window.findViewById(R.id.tv_content2);
        pickTv.setText(getResources().getString(R.string.text_photo_pick));
        pickTv.setTag(v.getId() + "");
        TextView cancelTv = (TextView) window.findViewById(R.id.tv_content3);
        cancelTv.setText(getResources().getString(R.string.text_photo_cancel));

        cameraTv.setOnClickListener(takePhotoListener);
        pickTv.setOnClickListener(takePhotoListener);
        cancelTv.setOnClickListener(takePhotoListener);
    }

    View.OnClickListener takePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (view.getId() == R.id.tv_content1) {
                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(CertificationActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {

                            @Override
                            public void onGranted() {
                                takePhoto((String) view.getTag());
                            }

                            @Override
                            public void onDenied(String permission) {
                                showToast("请您允许应用访问摄像头和存储设备");
                            }
                        }
                );
            } else if (view.getId() == R.id.tv_content2) {
                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(CertificationActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {

                            @Override
                            public void onGranted() {
                                pickPicture((String) view.getTag());
                            }

                            @Override
                            public void onDenied(String permission) {
                                showToast("请您允许应用访问存储设备");
                            }
                        }
                );
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
        if (mCurrCorpImageId.equals(iv_front_select.getId() + "")) {
            ID_front_path = mPhotoPathCrop;
        } else {
            ID_back_path = mPhotoPathCrop;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 960);
        intent.putExtra("outputY", 540);
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
        if (mCurrCorpImageId.equals(iv_front_select.getId() + "")) {
            iv_id_front.setImageBitmap(bm);
            iv_id_front.setVisibility(View.VISIBLE);
            ll_front.setVisibility(View.GONE);
        } else {
            iv_id_back.setImageBitmap(bm);
            iv_id_back.setVisibility(View.VISIBLE);
            ll_back.setVisibility(View.GONE);
        }
        updateSubmitStatus();
    }

    private String getFILE_PATH_TEMP() {
        File filePath = new File(FILE_PATH_TEMP);
        if (!filePath.isDirectory()) {
            filePath.mkdir();
        }
        return FILE_PATH_TEMP;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @TextChange({R.id.et_id,R.id.et_name})
    void textChange(){
        updateSubmitStatus();
    }

    private void updateSubmitStatus() {
        if (et_name.getText().toString().length() > 0 && et_id.getText().toString().length() == 18 && !TextUtils.isEmpty(ID_front_path) && !TextUtils.isEmpty(ID_back_path)) {
            btn_submit.setEnabled(true);
        }else{
            btn_submit.setEnabled(false);
        }
    }

}
