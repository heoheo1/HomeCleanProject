package com.hj.homecleanproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SignUpDialogFragment extends DialogFragment implements onBackPressedListener {

    ViewGroup viewGroup;
    EditText edt_Name,edt_GroupName;
    Button btn_Ok;
    RadioGroup rdo_Group;
    String position;
    String name,groupName;
    FirebaseFirestore db;
    RadioButton rdo_leader,rdo_member;
    String email;
    ImageView img_profile;
    FirebaseStorage firebaseStorage;
    StorageReference reference;
    Map<String, Object> member;
    LoginActivity loginActivity;
    Uri selectedImageUri;
    DatabaseReference realTime;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    CollectionReference rocRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_dialog, container, false);
        edt_Name = viewGroup.findViewById(R.id.editTextPersonName);
        edt_GroupName = viewGroup.findViewById(R.id.edt_GroupName);
        rdo_Group = viewGroup.findViewById(R.id.radio_Group);
        btn_Ok = viewGroup.findViewById(R.id.btn_Ok);
        rdo_leader = viewGroup.findViewById(R.id.rdo_leader);
        rdo_member = viewGroup.findViewById(R.id.rdo_member);
        firebaseStorage = FirebaseStorage.getInstance();
        reference = firebaseStorage.getReference();
        loginActivity = (LoginActivity) getActivity();
        img_profile = viewGroup.findViewById(R.id.img_profile);


        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????? ??? ??????
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);

            }
        });


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rdo_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_leader:
                        edt_GroupName.setHint("????????? ??????????????????");
                        position = "??????";
                        break;

                    case R.id.rdo_member:
                        edt_GroupName.setHint("???????????? ????????? ??????????????????");
                        position = "?????????";
                        break;
                }
            }
        });

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = FirebaseFirestore.getInstance();
                if (edt_Name.getText().toString().isEmpty() || edt_GroupName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                } else if (!(rdo_leader.isChecked() || rdo_member.isChecked())) {
                    Toast.makeText(getActivity(), "???????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                } else if(selectedImageUri==null){
                    Toast.makeText(getActivity(), "???????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (position.equals("??????")) {
                        pluseGroup();
                    } else {
                        saveGroup();
                    }
                }

            }

        });

        return viewGroup;
    }

    private void storageSave() {

        name = edt_Name.getText().toString();
        groupName = edt_GroupName.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(loginActivity);
        progressDialog.show();

        firebaseStorage = FirebaseStorage.getInstance();
        reference = firebaseStorage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com").child(groupName + "/" + name + "/" + "myImage");

        reference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                firebaseStorage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com").child(groupName + "/" + name + "/myImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        member.put("url", uri.toString());
                        db.collection(groupName).document(name).set(member);
                        Log.d("yousin", uri.toString());
                    }
                });
                Toast.makeText(loginActivity, "???????????????", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity, "????????? ??????", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                @SuppressWarnings("VisibleFortests") //???????????? ????????? ?????? ?????? (????????? ????????????)
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("UPLOAD " + (int) progress + "%...");
            }
        });


    }


    private void selectDoc() {

        member = new HashMap<>();
        name = edt_Name.getText().toString();
        groupName = edt_GroupName.getText().toString();
        Bundle emailResult = getArguments();//?????? ??????
        realTime = FirebaseDatabase.getInstance().getReference("users"); //??????????????? ??? ??????????????????.
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        String uid = auth.getInstance().getCurrentUser().getUid();

        Log.d("uid", uid);


        if (emailResult != null) {
            email = emailResult.getString("email");
            realTime.child(uid).setValue(groupName);
        }

        member.put("name", name);
        member.put("groupName", groupName);
        member.put("position", position);
        member.put("email", email);


        db.collection(groupName).document(name).set(member).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }

    private void pluseGroup() {
        name = edt_Name.getText().toString();
        groupName = edt_GroupName.getText().toString();
         rocRef = db.collection(groupName);
        rocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    QuerySnapshot collection = task.getResult();
                    Iterator<QueryDocumentSnapshot> iterator = collection.iterator();

                    if (iterator.hasNext()) { //????????????
                        String n = iterator.next().getId();
                        Log.d("dd", n);
                        Toast.makeText(getActivity(), "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    } else {
                        storageSave();
                        selectDoc();
                        Toast.makeText(getActivity(), "??????????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Data", "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void saveGroup() {
        name = edt_Name.getText().toString();
        groupName = edt_GroupName.getText().toString();
        rocRef = db.collection(groupName);
        rocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot collection = task.getResult();
                    Iterator<QueryDocumentSnapshot> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        String n = iterator.next().getId();
                        Log.d("dd", n);
                        storageSave();
                        selectDoc();
                        Toast.makeText(getActivity(), "??????????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "????????? ????????????????????????.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("Data", "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        goToMain(SignUpDialogFragment.this);
    }

    //??????????????? ??????
    private void goToMain(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //requireActivity=getActivity(???????????? ??????,????????? Excepion. app crash ??????) activity??? null??? ????????? ?????? ???????????? ?????????.
        fragmentManager.beginTransaction().remove(fragment).commit();//?????????????????? ?????????.
        fragmentManager.popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            selectedImageUri = data.getData();
            img_profile.setImageURI(selectedImageUri);
        }
    }
}