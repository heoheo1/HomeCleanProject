package com.hj.homecleanproject.customDialog;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hj.homecleanproject.R;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class GridDialogFragment extends DialogFragment {

    TextView dialogBtnOk, dialogBtnCancel;
    ImageView dialogImageView;
    EditText dialogContents;

    int resID = 0;
    String contents;

    File photoFile;
    Bitmap bitmap;

    onDialogResultListener result;

    public GridDialogFragment(int resID, String contents){ // GridView에 사진과 내용이 있을때
        this.resID = resID;
        this.contents = contents;
    }

    public GridDialogFragment(){ } //그냥 빈거일때

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid_dialog, container, false);

        dialogBtnOk = view.findViewById(R.id.dialog_OK);
        dialogBtnCancel = view.findViewById(R.id.dialog_CANCEL);
        dialogContents = view.findViewById(R.id.dialog_contents);
        dialogImageView = view.findViewById(R.id.dialog_imageView);
        setRetainInstance(true); //Fragment의 생명주기를

        if(resID == R.drawable.moon) {
            //달 그림이면 그 내용들을 setting
            //후, 기본그림일땐 아무내용없게 만들고, 그림이 있다면 그 내용으로 Dialog를 채울 예정
            init();
        }

        dialogImageView.setOnClickListener(v ->{
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                try{
                    photoFile = createFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(photoFile != null){
                    //provider를 사용해 외부앱과 연결하여 데이터를 Uri형식으로 연결한다.
                    Uri photoUri = FileProvider.getUriForFile(getContext(),"com.hj.homecleanproject.fileprovider",photoFile);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, 1000);
                }
            }
        });

        dialogBtnCancel.setOnClickListener(v -> {
            dismiss();
        });

        dialogBtnOk.setOnClickListener(v -> { //확인버튼을 눌렀을때
            if(bitmap == null){
                Toast.makeText(getActivity(),"등록된 이미지가 없습니다!",Toast.LENGTH_LONG).show();
                return;
            }
            contents = dialogContents.getText().toString(); // Contents 내용
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,40,output);
            byte[] bytes = output.toByteArray();

            if(result != null) {
                result.onMyDialogResult(bytes, contents); //interface로 선언해 dialog와 fragment사이에 데이터 전달하기
            }
            dismiss();
        });
        return view;
    }

    public void init(){
        dialogImageView.setImageResource(resID);
        dialogContents.setText(contents);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 용량을 4분의1로 낮춤
            bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options); //photoFile로 만든 이미지를 bitmap으로 형식변경

            Resources resources = Resources.getSystem(); //어느곳에서든지 이렇게 사용하면 context의 내용을 사용할수있다. 무조건! (직접적인 접근 가능)
            Configuration configuration = resources.getConfiguration();

            // 카메라를 가로/세로로 전환시 Destory까지 내려가 팅기는 현상 발생 -> ConfigurationChanged 사용
            if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) { //세로일때
                Matrix matrix = new Matrix();
                matrix.postRotate(90); //이미지가 90도 돌아간상태로 등장하기때문에 원상태로 돌리기위해 90도 회전

                //좌표값 (0,0) 에서 (이미지의 넓이, 이미지의 높이)까지
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            dialogImageView.setImageBitmap(bitmap); //가로일땐 그냥 찍기
        }
    }

    public File createFile() throws IOException {
        String fileName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //Data/data/패키지/에 app_HomeCleanProject라는 파일이 생성된다.
        File file = File.createTempFile(fileName,".jpg",getContext().getDir("HomeCleanProject",Context.MODE_PRIVATE));

        return file;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dialogImageView.setImageBitmap(bitmap);
    }

    public void setDialogResult (onDialogResultListener listener){
        result = listener;
    }
}