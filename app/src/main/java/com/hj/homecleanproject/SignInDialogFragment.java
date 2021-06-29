package com.hj.homecleanproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hj.homecleanproject.customInterface.onBackPressedListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SignInDialogFragment extends DialogFragment implements onBackPressedListener {

    ViewGroup viewGroup;
    EditText edt_Name,edt_GropName;
    Button btn_Ok;
    RadioGroup rdo_Group;
    String position;
    String name,groupName;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_sign_in_dialog,container,false);
        edt_Name=viewGroup.findViewById(R.id.editTextPersonName);
        edt_GropName=viewGroup.findViewById(R.id.edt_GropName);
        rdo_Group=viewGroup.findViewById(R.id.radio_Group);
        btn_Ok=viewGroup.findViewById(R.id.btn_Ok);


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rdo_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdo_leader :
                        edt_GropName.setHint("그룹을 생성해주세요");
                        position="리더";
                        break;

                    case R.id.rdo_member :
                        edt_GropName.setHint("존재하는 그룹을 입력해주세요");
                        position="구성원";
                        break;
                }
            }
        });

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db =FirebaseFirestore.getInstance();
               if(edt_Name.getText().toString().isEmpty()||edt_GropName.getText().toString().isEmpty()) {
                   Toast.makeText(getActivity(),"정보를 입력해주세요",Toast.LENGTH_SHORT).show();
               }else {
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

        Map<String, Object> member = new HashMap<>();
        name = edt_Name.getText().toString();
        groupName = edt_GropName.getText().toString();

        member.put("Name", name);
        member.put("GroupName", groupName);
        member.put("Position", position);

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
            groupName = edt_GropName.getText().toString();
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
        groupName = edt_GropName.getText().toString();
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
        goToMain(SignInDialogFragment.this);
    }
    //프래그먼트 종료
    private void goToMain(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //requireActivity=getActivity(있다는걸 보장,없으면 Excepion. app crash 발생) activity가 null한 상황을 아예 없애려고 쓰인다.
        fragmentManager.beginTransaction().remove(fragment).commit();//프래그먼트를 지운다.
        fragmentManager.popBackStack();
    }

}