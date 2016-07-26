package com.zhenaixuanyan.app.videos.Activitys;

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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.App_;
import com.zhenaixuanyan.app.videos.Beans.User;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.UserLoginResponse;
import com.zhenaixuanyan.app.videos.Constants;
import com.zhenaixuanyan.app.videos.Net.MyRestClient;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;

/**
 * 修改用户资料
 */
@EActivity(R.layout.activity_change_profile)
public class ChangeProfileActivity extends BaseActivity {

    private String filesToUpload = "";
    private AlertDialog mTakeDialog;
    private String mPhotoPath;// Photo from Camera.
    private String mPhotoPathCrop; //Photo from crop.
    public static final int REQUEST_PICK_IMAGE = 1001;
    public static final int REQUEST_TAKE_PHOTO = 1002;
    public static final int REQUEST_CROP = 1003;
    private String FILE_PATH_TEMP = "";
    private String mCurrCorpImageId;

    @ViewById(R.id.btn_ok)
    Button btn_ok;

    @ViewById(R.id.iv_icon)
    ImageView iv_icon;

    //网络请求
    @RestService
    MyRestClient restClient;

    @AfterViews
    void afterview() {
        setTitle(R.id.navigation_bar_back_tv, R.string.change_info);
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(ChangeProfileActivity.this,
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

    @Click({R.id.navigation_bar_back_ib, R.id.btn_ok, R.id.iv_icon})
    void click(View v) {
        switch (v.getId()) {
            case R.id.navigation_bar_back_ib: {  //导航条返回
                finish();
                break;
            }
            case R.id.btn_ok: {
                showProcessHUD(null);
                userEdit();
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

    /**
     * 对话框提供照片选择方式
     */
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
        public void onClick(final View view) {
            if (view.getId() == R.id.tv_content1) {
                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(ChangeProfileActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {

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
                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(ChangeProfileActivity.this,
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


    /**
     * 调用系统相机拍照
     * @param imgId
     */
    private void takePhoto(String imgId) {
        File photoFile = new File(getFILE_PATH_TEMP(), String.format("%s.png", System.currentTimeMillis()));
        mPhotoPath = photoFile.getAbsolutePath();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mCurrCorpImageId = imgId;
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 调用系统图库选择图片
     * @param imgId
     */
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

    /**
     * 调用系统图库应用裁剪图片
     * @param uri1
     * @param size
     * @param imgId
     */
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

    /**
     * 获取图片保存的路径
     * @return
     */
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

    /**
     * 修改用户资料
     */
    @Background
    void userEdit(){
        try{
            MultiValueMap<String, Object> request_data = new LinkedMultiValueMap<>();
            FileSystemResource img = new FileSystemResource(filesToUpload);
            request_data.set("u_header",img);
            request_data.set("u_id", App_.getInstance().mUser.getU_id());
            restClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
            UserLoginResponse response = restClient.userEdit(request_data);
            userEditResult(response);
        }catch (Exception e){
            e.printStackTrace();
            userEditResult(null);
        }
    }

    /**
     * 更新修改用户资料后的结果
     * @param response
     */
    @UiThread
    void userEditResult(UserLoginResponse response){
        hideProcessHUD();
        if(response == null){
            showToast("修改用户资料失败！");
            return;
        }

        if (response.getStatus().equalsIgnoreCase(Constants.STATUS_OK)){
            User user = response.getData();
            //赋值到全局变量中
            App_.getInstance().mUser = user;
            //保存到SharedPreferences中
            User.saveUser(this,user);
            showToast("修改用户资料成功！");
        }else{
            showToast(response.getMessage());
        }
    }

}
