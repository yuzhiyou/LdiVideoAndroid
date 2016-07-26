package com.zhenaixuanyan.app.videos.Activitys.Fragments.Tabbar;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenaixuanyan.app.videos.Activitys.CertificationActivity_;
import com.zhenaixuanyan.app.videos.Activitys.ChangePasswordActivity_;
import com.zhenaixuanyan.app.videos.Activitys.ChangeProfileActivity_;
import com.zhenaixuanyan.app.videos.Activitys.Fragments.BaseFragment;
import com.zhenaixuanyan.app.videos.Activitys.MyVideoListActivity_;
import com.zhenaixuanyan.app.videos.Activitys.UserEnterActivity_;
import com.zhenaixuanyan.app.videos.Activitys.VideoListActivity_;
import com.zhenaixuanyan.app.videos.Activitys.WebViewClientActivity_;
import com.zhenaixuanyan.app.videos.App_;
import com.zhenaixuanyan.app.videos.Beans.User;
import com.zhenaixuanyan.app.videos.Constants;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment {
    @ViewById(R.id.btn_logout)
    Button btn_logout;

    @ViewById(R.id.tv_nickname)
    TextView tv_nickname;

    @ViewById(R.id.tv_phone)
    TextView tv_phone;

    @ViewById(R.id.iv_photo)
    ImageView iv_photo;

    private AlertDialog mTakeDialog;
    private String mPhotoPath;// Photo from Camera.
    private String mPhotoPathCrop; //Photo from crop.
    public static final int REQUEST_PICK_IMAGE = 1001;
    public static final int REQUEST_TAKE_PHOTO = 1002;
    public static final int REQUEST_CROP = 1003;
    private String FILE_PATH_TEMP = "";


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment_();
        return fragment;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    @AfterViews
    void afterViews() {
        //title
        setTitle(R.id.navigation_bar_title_tv, R.string.tabar_profile);
        tv_nickname.setText(App_.getInstance().mUser.getU_name());
        tv_phone.setText(App_.getInstance().mUser.getU_phone());
        FILE_PATH_TEMP = Environment.getExternalStorageDirectory().toString() + Constants.USER_PHOTO_SAVE_PATH;
    }

    @Click({R.id.btn_logout, R.id.rl_appeal, R.id.iv_photo, R.id.rl_change_pass, R.id.rl_gift, R.id.rl_identity_verification, R.id.rl_my_video, R.id.rl_change_info, R.id.rl_recode})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_logout: {
                App_.getInstance().mUser = null;
                User.clearUser(getContext());
                //注册登录页
                UserEnterActivity_.intent(this).start();
                getActivity().finish();
                break;
            }
            case R.id.rl_appeal: {
                break;
            }
            case R.id.iv_photo: {
                showPhotoWayDialog();
                break;
            }
            case R.id.rl_change_pass: {
                ChangePasswordActivity_.intent(this).start();
                break;
            }
            case R.id.rl_gift: {
                WebViewClientActivity_.intent(this).url("http://www.baidu.com").start();
                break;
            }
            case R.id.rl_identity_verification: {
                CertificationActivity_.intent(this).start();
                break;
            }
            case R.id.rl_my_video: {
                MyVideoListActivity_.intent(this).start();
                break;
            }
            case R.id.rl_change_info: {
                ChangeProfileActivity_.intent(this).start();
                break;
            }
            case R.id.rl_recode: {
                VideoListActivity_.intent(this).start();
                break;
            }
            default:
                break;
        }
    }

    private void showPhotoWayDialog() {
        mTakeDialog = new AlertDialog.Builder(getContext()).create();
        mTakeDialog.show();
        Window window = mTakeDialog.getWindow();
        window.setContentView(R.layout.take_photo_dialog);
        TextView cameraTv = (TextView) window.findViewById(R.id.tv_content1);
        cameraTv.setText(getResources().getString(R.string.text_photo_camera));
        cameraTv.setTag(iv_photo.getId() + "");
        TextView pickTv = (TextView) window.findViewById(R.id.tv_content2);
        pickTv.setText(getResources().getString(R.string.text_photo_pick));
        pickTv.setTag(iv_photo.getId() + "");
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
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
        iv_photo.setImageBitmap(bm);
    }

    private String getFILE_PATH_TEMP() {
        File filePath = new File(FILE_PATH_TEMP);
        if (!filePath.isDirectory()) {
            filePath.mkdir();
        }
        return FILE_PATH_TEMP;
    }
}
