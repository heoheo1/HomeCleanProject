package com.hj.homecleanproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hj.homecleanproject.customInterface.onBackPressedListener;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_sign_up_dialog,container,false);
        edt_Name=viewGroup.findViewById(R.id.editTextPersonName);
        edt_GroupName=viewGroup.findViewById(R.id.edt_GroupName);
        rdo_Group=viewGroup.findViewById(R.id.radio_Group);
        btn_Ok=viewGroup.findViewById(R.id.btn_Ok);
        rdo_leader=viewGroup.findViewById(R.id.rdo_leader);
        rdo_member=viewGroup.findViewById(R.id.rdo_member);
        img_profile=viewGroup.findViewById(R.id.img_profile);
        firebaseStorage = FirebaseStorage.getInstance();
        reference = firebaseStorage.getReference();

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //갤러리 로 이동
                Intent intent=new Intent();
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,1);
            }
        });



        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rdo_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdo_leader :
                        edt_GroupName.setHint("그룹을 생성해주세요");
                        position="리더";
                        break;

                    case R.id.rdo_member :
                        edt_GroupName.setHint("존재하는 그룹을 입력해주세요");
                        position="구성원";
                        break;
                }
            }
        });

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db =FirebaseFirestore.getInstance();
               if(edt_Name.getText().toString().isEmpty()||edt_GroupName.getText().toString().isEmpty()) {
                   Toast.makeText(getActivity(),"정보를 입력해주세요.",Toast.LENGTH_SHORT).show();
               }else if (!(rdo_leader.isChecked()||rdo_member.isChecked())){
                   Toast.makeText(getActivity(),"그룹장과 구성원중 선택해주세요.",Toast.LENGTH_SHORT).show();
               }
               else {
                   if(position.equals("리더")){
                       pluseGroup();
                   }else{
                       saveGroup();
                   }
               }
            }
        });

        return viewGroup;
    }

    private void selectDoc() {

         member = new HashMap<>();
        name = edt_Name.getText().toString();
        groupName = edt_GroupName.getText().toString();
        Bundle emailResult =getArguments();//번들 받기

        if(emailResult != null){
            email = emailResult.getString("email");
        }

        member.put("Name", name);
        member.put("GroupName", groupName);
        member.put("Position", position);
        member.put("email",email);

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

        private void pluseGroup()
        {
            name = edt_Name.getText().toString();
            groupName = edt_GroupName.getText().toString();
            CollectionReference rocRef=db.collection(groupName);
            rocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        QuerySnapshot collection = task.getResult();
                       Iterator<QueryDocumentSnapshot> iterator = collection.iterator();


                        if (iterator.hasNext()){ //존재하면
                            String n =iterator.next().getId();
                            Log.d("dd",n);
                            Toast.makeText(getActivity(),"그룹이 존재합니다.",Toast.LENGTH_SHORT).show();

                        }else {
                            selectDoc();
                            Toast.makeText(getActivity(), "회원가입이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                            dismiss();
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

    private void saveGroup()
    {
        name = edt_Name.getText().toString();
        groupName = edt_GroupName.getText().toString();
        CollectionReference rocRef=db.collection(groupName);
        rocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    QuerySnapshot collection = task.getResult();
                    Iterator<QueryDocumentSnapshot> iterator = collection.iterator();


                    if (iterator.hasNext()){
                        selectDoc();
                        Toast.makeText(getActivity(), "회원가입이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                        dismiss();


                    }else {
                        Toast.makeText(getActivity(),"그룹이 존재하지않습니다.",Toast.LENGTH_SHORT).show();
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
    //프래그먼트 종료
    private void goToMain(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //requireActivity=getActivity(있다는걸 보장,없으면 Excepion. app crash 발생) activity가 null한 상황을 아예 없애려고 쓰인다.
        fragmentManager.beginTransaction().remove(fragment).commit();//프래그먼트를 지운다.
        fragmentManager.popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            try {
            Uri uri =data.getData();

                URL url =new URL(uri.toString());
               imgTask(url.toString(),img_profile);
               member.put("url",url);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void imgTask(String imgUrl, ImageView imageView) {
        ImageLoadTask task = new ImageLoadTask(imgUrl, imageView);
        task.execute();

    }


}