package com.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.api.ApiAppConfig;
import com.demo.api.ApiRegistration;
import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.model.registration.ApiRegistrationParam;
import com.demo.model.registration.RegistrationMain;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;
import com.demo.utils.Constant;
import com.demo.utils.Imageutils;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseActivity implements Imageutils.ImageAttachmentListener {

    private EditText et_name;
    private EditText et_email;
    private EditText et_mobile;
    private EditText et_password;
    private TextView tv_register;
    public static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    ImageView profile_image;
    private Bitmap bitmap;
    private String file_name;
    Imageutils imageutils;
    CardView cardUpload;
    private String deviceToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (deviceToken!=null){
            preference.setDeviceToken(deviceToken);
        }
        profile_image = (ImageView) findViewById(R.id.profile_image);
        cardUpload = (CardView)findViewById(R.id.cardUpload);
        et_name = (EditText)findViewById(R.id.et_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_mobile = (EditText)findViewById(R.id.et_mobile);
        et_password = (EditText)findViewById(R.id.et_password);
        tv_register = (TextView)findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        cardUpload.setOnClickListener(this);
        imageutils =new Imageutils(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_register:
                if(isvalid()){
                    getRegistration();
                }
                break;
            case R.id.cardUpload:
                imageutils.imagepicker(1);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap=file;
        this.file_name=filename;
        profile_image.setImageBitmap(file);

        String path =  Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file,filename,path,false);

    }
    public boolean isvalid(){

        boolean flag = true;
        if(et_name.getText().toString().trim().length() == 0){
            flag = false;
            et_name.setError("Plesae enter name");
        }else if(et_email.getText().toString().trim().length() == 0){
            flag = false;
            et_email.setError("Plesae enter email");
        }else if(et_mobile.getText().toString().trim().length() == 0){
            flag = false;
            et_mobile.setError("Plesae enter mobile");
        }else if(et_password.getText().toString().trim().length() == 0){
            flag = false;
            et_password.setError("Plesae enter password");
        }else if(!isvalidMailid(et_email.getText().toString().trim())){
            flag = false;
            et_password.setError("Plesae enter valid email");
        }else if(et_mobile.getText().toString().trim().length() != 10){
            flag = false;
            et_mobile.setError("Plesae enter 10 digit mobile");
        }else if(et_password.getText().toString().trim().length() < 6){
            flag = false;
            et_mobile.setError("Plesae enter min 6 length password");
        }else if(bitmap==null){
            flag = false;
            et_mobile.setError("Upload Profile Image");
        }
        return  flag;

    }


    private void getRegistration() {
        if(isNetworkConnected()){
            showProgressDialog();
  /*         */

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), getImageFile(bitmap));
            MultipartBody.Part body = MultipartBody.Part.createFormData("prof_image", getImageFile(bitmap).getName(), requestFile);
            RequestBody usernameBody = RequestBody.create(MediaType.parse("multipart/form-data"), et_name.getText().toString());
            RequestBody bodyApiKey = RequestBody.create(MediaType.parse("multipart/form-data"), Constant.API_KEY);
            RequestBody mobileBody = RequestBody.create(MediaType.parse("multipart/form-data"), et_mobile.getText().toString());
            RequestBody bodyEmail = RequestBody.create(MediaType.parse("multipart/form-data"), et_email.getText().toString());
            RequestBody bodyPass = RequestBody.create(MediaType.parse("multipart/form-data"), et_password.getText().toString());
            RequestBody bodyDevicetoken = RequestBody.create(MediaType.parse("multipart/form-data"), preference.getDeviceToken());

            Call<RegistrationMain> getDepartment = RestService.getInstance().restInterface.userRegister(bodyApiKey,usernameBody,bodyEmail,mobileBody,bodyPass,bodyDevicetoken,body);

            APIHelper.enqueueWithRetry(getDepartment,new Callback<RegistrationMain>() {

                @Override
                public void onResponse(Call<RegistrationMain> call, Response<RegistrationMain> response) {
                    dismissProgressDialog();
                    RegistrationMain main= response.body();

                    if(main.getResponseCode()==200){
                        preference.setUserId(main.getResponseData().getUserid().toString());
                        preference.setName(main.getResponseData().getName().toString());
                        preference.setEmail(main.getResponseData().getEmail().toString());
                        preference.setPhone(main.getResponseData().getPhone().toString());
                        preference.setUserStatus("yes");
                        callNewScreen();

                    }else
                        Toast.makeText(RegistrationActivity.this,main.getMessage(),Toast.LENGTH_LONG).show();




                }

                @Override
                public void onFailure(Call<RegistrationMain> call, Throwable t) {
                    dismissProgressDialog();

                }
            });


        }


    }



    public  boolean isvalidMailid(String mail) {
        return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
    }

    private ApiRegistrationParam getParam() {
        ApiRegistrationParam map=new ApiRegistrationParam();
        map.setApiKey(Constant.API_KEY);
        map.setName(et_name.getText().toString().trim());
        map.setEmail(et_email.getText().toString().trim());
        map.setPassword(et_password.getText().toString().trim());
        map.setPhone(et_mobile.getText().toString().trim());
        map.setDeviceToken(Constant.API_KEY);
        return map;
    }
    private void callNewScreen() {
        Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
