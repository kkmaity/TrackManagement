package com.demo.fragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.CommonAdapter;
import com.demo.adapter.OutDoorUpdateOthersListAdapter;
import com.demo.model.CommonDialogModel;
import com.demo.model.OutDoorHistory;
import com.demo.restservice.RestService;
import com.demo.services.LocationUpdateService;
import com.demo.utils.Constant;
import com.demo.utils.FileUtils;
import com.demo.utils.Imageutils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by root on 20/8/15.
 */
public class OutDoorWorkEntryAttendanceHistoryUpdateFragment extends BaseFragment implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Imageutils.ImageAttachmentListener {

    //    private static EditText et_challan_number;
//    private static EditText et_challan_date;
//    private EditText et_hospital_name;
//    private EditText et_doctor_name;
//    private EditText et_invoice_number;
//    private static EditText et_invoice_date;
//    private EditText et_mode_of_transport;
//    private EditText et_bike_list;
    private EditText et_description;
    //    private EditText et_expence;
    private ImageView ivPicture1;
    private ImageView ivPicture2;
    private ImageView ivPicture3;
    private ImageView ivPicture4;
    private ImageView ivPicture5;
    private TextView tv_update;
    //    private TextView tv_end_work;
//    private TextView tv_start_date_time;
//    private TextView tv_end_date_time;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private String lat, lng;
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 0;
    private boolean isAlreadyStarted = false;
    private boolean isButtonClicked = false;
    private int val = 0;
    private ListView gridAttendanceHis;
    private OutDoorUpdateOthersListAdapter outDoorHistoryGridAdapter;
    private ArrayList<OutDoorHistory> outDoorHistories = new ArrayList<>();
    private ArrayList<CommonDialogModel> commonDialogModels = new ArrayList<>();
    private JSONArray hostpitalListArr, doctorListArr, modeOfTranportArr, bileArr;
    private CommonAdapter adapter;
    private Imageutils imageutils;
    private int imageViewID;
    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private Bitmap mBitmap3;
    private Bitmap mBitmap4;
    private Bitmap mBitmap5;
    private List<String> images = new ArrayList<>();
    private LinearLayout linBikelist;
    private View view;
    private String hospitalid, doctorid, transportid, bikeid;
    private Uri uri5;
    private Uri uri1;
    private Uri uri2;
    private Uri uri3;
    private Uri uri4;
    private String job_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_outdoor_work_entry_other_details_history_update, null, false);
        ((MainActivity) getActivity()).setTitle("Outdoor Work Entry");

        Bundle bundle = getArguments();
        job_id = bundle.getString("job_id");

        imageutils = new Imageutils(getActivity(), this, true);
        et_description = v.findViewById(R.id.et_description);
        ivPicture1 = v.findViewById(R.id.ivPicture1);
        ivPicture2 = v.findViewById(R.id.ivPicture2);
        ivPicture3 = v.findViewById(R.id.ivPicture3);
        ivPicture4 = v.findViewById(R.id.ivPicture4);
        ivPicture5 = v.findViewById(R.id.ivPicture5);

        ivPicture1.setOnClickListener(this);
        ivPicture2.setOnClickListener(this);
        ivPicture3.setOnClickListener(this);
        ivPicture4.setOnClickListener(this);
        ivPicture5.setOnClickListener(this);

        tv_update = v.findViewById(R.id.tv_update);

        tv_update.setOnClickListener(this);

        ((MainActivity) getActivity()).setTitle(getArguments().getString("category_title"));

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_update:
                if (!baseActivity.isNetworkConnected())
                    return;
                if (isValid()) {
                    LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else {
                        baseActivity.showProgressDialog();
                        isButtonClicked = true;
                        val = 1;
                        createLocationRequest();
                        mGoogleApiClient = new GoogleApiClient.Builder(baseActivity)
                                .addApi(LocationServices.API)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .build();
                        mGoogleApiClient.connect();
                    }
                }
                break;
            case R.id.ivPicture1:
                imageViewID = 1;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture2:
                imageViewID = 2;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture3:
                imageViewID = 3;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture4:
                imageViewID = 4;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture5:
                imageViewID = 5;
                imageutils.imagepicker(1);
                break;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        //((MainActivity) getActivity()).setTitle(getArguments().getString("category_title"));
    }


    public boolean isValid() {
        boolean flag = true;
        if (et_description.getText().toString().trim().length() == 0) {
            et_description.setError("Please fill description");
            flag = false;
        }
        return flag;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateData();
    }

    private void updateData() {

        if (null != mCurrentLocation) {
            if (isButtonClicked) {
                lat = String.valueOf(mCurrentLocation.getLatitude());
                lng = String.valueOf(mCurrentLocation.getLongitude());
                mGoogleApiClient.disconnect();
                if (val == 1) {
//                    submitUpdate(job_id, et_description.getText().toString(), mBitmap1, mBitmap2, mBitmap3, mBitmap4, mBitmap5);
                    submitUpdate(job_id, et_description.getText().toString(), images);
                }
            }
            isButtonClicked = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Fragment", "onRequestPermissionsResult: " + requestCode);
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        Bitmap bitmap = file;
        String file_name = filename;
        switch (imageViewID) {
            case 1:
                ivPicture1.setImageBitmap(file);
                mBitmap1 = file;
                uri1 = uri;
                String select1 = FileUtils.getPath(baseActivity, uri);
                images.add(select1);
                break;
            case 2:
                uri2 = uri;
                ivPicture2.setImageBitmap(file);
                mBitmap2 = file;
                String select2 = FileUtils.getPath(baseActivity, uri);
                images.add(select2);
                break;
            case 3:
                uri3 = uri;
                ivPicture3.setImageBitmap(file);
                mBitmap3 = file;
                String select3 = FileUtils.getPath(baseActivity, uri);
                images.add(select3);
                break;
            case 4:
                uri4 = uri;
                ivPicture4.setImageBitmap(file);
                mBitmap4 = file;
                String select4 = FileUtils.getPath(baseActivity, uri);
                images.add(select4);
                break;
            case 5:
                uri5 = uri;
                ivPicture5.setImageBitmap(file);
                mBitmap5 = file;
                String select5 = FileUtils.getPath(baseActivity, uri);
                images.add(select5);
                break;

        }


        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Fragment", "onActivityResult: ");
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "SMS");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Call") {
            Toast.makeText(baseActivity, "calling code", Toast.LENGTH_LONG).show();
        } else if (item.getTitle() == "SMS") {
            Toast.makeText(baseActivity, "sending sms code", Toast.LENGTH_LONG).show();
        } else {
            return false;
        }
        return true;
    }


    private void submitUpdate(String job_id,
                              String description,
                              List<String> images) {


        job_id = null == job_id ? "" : job_id;
        description = null == description ? "" : description;

        RequestBody bodyApiKey = RequestBody.create(MultipartBody.FORM, Constant.API_KEY);
        RequestBody job_idBody = RequestBody.create(MultipartBody.FORM, job_id);
        RequestBody descriptionBody = RequestBody.create(MultipartBody.FORM, description);
//        MultipartBody.Part body1 = null, body2 = null, body3 = null, body4 = null, body5 = null;
//        if (picture1 != null) {
//            RequestBody requestFile1 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture1));
//            body1 = MultipartBody.Part.createFormData("picture1", baseActivity.getImageFile(picture1).getName(), requestFile1);
////            body1 = prepareFilePart("picture1", uri1);
//        }
//        if (picture2 != null) {
//            RequestBody requestFile2 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture2));
//            body2 = MultipartBody.Part.createFormData("picture2", baseActivity.getImageFile(picture2).getName(), requestFile2);
////            body2 = prepareFilePart("picture2", uri2);
//        }
//        if (picture3 != null) {
//            RequestBody requestFile3 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture3));
//            body3 = MultipartBody.Part.createFormData("picture3", baseActivity.getImageFile(picture3).getName(), requestFile3);
////            body3 = prepareFilePart("picture3", uri3);
//        }
//        if (picture4 != null) {
//            RequestBody requestFile4 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture4));
//            body4 = MultipartBody.Part.createFormData("picture4", baseActivity.getImageFile(picture4).getName(), requestFile4);
////            body4 = prepareFilePart("picture4", uri4);
//        }
//        if (picture5 != null) {
//            RequestBody requestFile5 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture5));
//            body5 = MultipartBody.Part.createFormData("picture5", baseActivity.getImageFile(picture5).getName(), requestFile5);
////            body5 = prepareFilePart("picture5", uri5);
//        }

        List<MultipartBody.Part> parts = new ArrayList<>();
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                parts.add(prepareFilePart("picture[]", Uri.fromFile(new File(images.get(i)))));
            }
        }

        Call<ResponseBody> getDepartment = RestService.getInstance().restInterface.update_outdoor_work(bodyApiKey, job_idBody,
                descriptionBody, parts);
        getDepartment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                baseActivity.dismissProgressDialog();

                try {
                    String res = response.body().string();
                    try {
                        JSONObject json = new JSONObject(res);
                        try {
                            if (json.getInt("ResponseCode") == 200) {
                                String msg = json.getString("message");
                                Toast.makeText(baseActivity, msg, Toast.LENGTH_LONG).show();
                                getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));
                                baseActivity.onBackPressed();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseActivity.dismissProgressDialog();
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(baseActivity, fileUri);
        MediaType type = MediaType.parse(getMimeType(fileUri));
        RequestBody requestFile = RequestBody.create(type, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = baseActivity.getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    /*if (isNetworkAvailable()){
        showProgressDialog();
        List<MultipartBody.Part> parts = new ArrayList<>();
        RequestBody prescription = createPartFromString("PRESCRIPTION");
        RequestBody reference_number = createPartFromString(reference_id);
        RequestBody user_family_id = createPartFromString(family_member_id);
        RequestBody upload_by = createPartFromString("DOCTOR");
        RequestBody purpose = createPartFromString("DOCTOR_ONLINE_APPOINTMENT");
        RequestBody isVirtual = createPartFromString(String.valueOf(is_virtual));
        RequestBody can_delete = createPartFromString("0");
        RequestBody chief_complaints=createPartFromString(et_chiefcomplaints.getText().toString());
        RequestBody examination_and_findings=createPartFromString(et_examinationandclinicalfinding.getText().toString());
        RequestBody has_signatureRes=createPartFromString(String.valueOf(has_signature));
        RequestBody medicine_list = null;
        if (has_signature==0){
            medicine_list=createPartFromString(et_suggestion.getText().toString());

        }else{
            medicine_list=createPartFromString(requestBody());
        }


        if (images != null&&images.size() >0) {
            for (int i=0;i<images.size();i++){
                parts.add(prepareFilePart("prescriptions[]",Uri.fromFile(new File(images.get(i).getImagePath()))));
            }

        }


        retrofit2.Call<ResponseBody> call1=RestService.getInstance().restInterface.createPrescription(prescription,
                reference_number,user_family_id,upload_by,purpose,isVirtual,can_delete,chief_complaints,examination_and_findings,medicine_list,has_signatureRes,parts);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    String res=response.body().string();
                    try {
                        JSONObject object=new JSONObject(res);
                        boolean status=object.getBoolean("status");
                        String msg=object.getString("message");
                        if (status){

                            firebaseAnaliticsText("visit_successfull","D_ "+
                                    "P_ "+name);

                            Toast.makeText(VideoCallActivityNew.this,msg,Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
            }
        });

    }*/
}
