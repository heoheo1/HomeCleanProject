package com.hj.homecleanproject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hj.homecleanproject.customDialog.GridDialogFragment;
import com.hj.homecleanproject.customInterface.GetCustomView;
import com.hj.homecleanproject.customInterface.ImageViewClickListener;
import com.hj.homecleanproject.customInterface.TextViewClickListener;
import com.hj.homecleanproject.customInterface.onDialogResultListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;

//Fragment란, 어플리케이션에서 화면에 직접 보이는 공간의 Activity내에서 분할시키고 다른 화면으로 전환할 수 있는 화면 공간의 단위
//Fragment를 이용하려면, 상위에 있는 Activity에서 출력할 layout을 제어
//하지만 Fragment 내부에서 다른 Fragment로 이동하는 것은 그 Fragment가 자신의 하위레벨이 아니기 때문에
//내부에서 직접 제어할 수 없으므로 , 상위 레벨인 Activity를 호출하여 제어하는 형태가 되어야 함

public class
GridFragment extends Fragment {

    ViewGroup viewGroup;
    GridView gridView;
    ArrayList<MyWork> workList;
    MyGridAdapter adapter;
    FragmentActivity fragmentActivity;
    MyFamilyFragment familyFragment;

    File photoFile; // 찍은 사진에 대한 임시파일
    Bitmap bitmap; // 내장 카메라에서 찍은 사진파일형식
    int adapterPosition; //내가 선택한 cardView의 Position
    String collections, documents; // 그룹명, 유저이름
    String groupName;
    Map<String, Object> myData; // FireStore에 넣어 주기 위한 Map
    String fileName; // TimeStamp! 현재 시간을 뽑아냄
    FloatingActionButton fab;
    String email;
    String name;
    String position;

    FirebaseFirestore db;
    FirebaseStorage storage;
    DatabaseReference realtime;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    StorageReference reference;
    NotificationManager manager;
    NotificationCompat.Builder builder;

    Handler handler;

    String msg;


    public void notifiCM() {
        manager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE); //시스템에 발생시키는 SystemService
        //그냥 이벤트가 아니라 담당하는 시스템 에게 알림 처리를 하기위해서 사용 (인스턴스)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //25미만은 돌아가지가 않는다.변화가 이러난 시점을 써줘야한다.
            String channelID = "one-channel"; // 채널아이디(식별자) 채널 이름
            String channelName = "My channel One";
            String channelDescription = "My channel one Description";//Description = 보충 설명
            NotificationChannel channel = null;// headsup 을 쓸려고 전역으로 뺌
            channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            //알림 채널을 정해주고 각각의 알림 정의를하기위해서 사용
            //headsup은 중요알림 설정할때
            //정상적인 알림이다.IMPORTANCE_DEFAULT
            channel.setDescription(channelDescription);// 보충 설명을 내앱에 담겠다.
            channel.enableLights(true); //여기서도 설정가능,아래에서도 설정가능 25이상버전만 불빛을 사용하겠다.
            channel.setVibrationPattern(new long[]{100});//100 200 300 진동을 준다.
            manager.createNotificationChannel(channel);//매니저가 이제 자기 채널로 인식한다.
            builder = new NotificationCompat.Builder(getContext(), channelID);//오레오버전이하일때는 this 이이름으로 된 빌더를 하나만들겠다.
        } else {
            builder = new NotificationCompat.Builder(getContext()); //이전버전일 경우 25이하 버전은 채널이란 존재를 모른다. builder로 만들어 줘야한다.
        }
        //오래오버전이상이나 이하나 누구나 사용가능
        builder.setSmallIcon(android.R.drawable.ic_menu_camera);//작은 아이콘이미지
        builder.setContentTitle("Home Family Project"); //확장 내용의 타이틀 문자열
        builder.setContentText("사진 등록이 완료 되었습니다."); //확장 내용의 본문 문자열
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        //ALL 3개다쓴다.DEFAULT_LIGHTS 불빛 ,DEFAULT_SOUND 소리 ,DEFAULT_VIBRATE 진동
        builder.setAutoCancel(true); //기본이 트루,터치 시 작동 삭제 여부,터치 시 삭제됨

        //앱으로 돌아가고 싶을때.
        Intent intent = new Intent(getContext(), FragmentActivity.class);//this 다른곳이아니닌까
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //저기로 가는걸 시스템에게 부탁하는 인텐트 생성
//        builder.setContentIntent(pendingIntent);
        //빌드가 연결 되어있으닌까 notify가 알수있다.
        builder.setFullScreenIntent(pendingIntent, true);
//        PendingIntent addActionIntent =PendingIntent.getBroadcast(getContext(),20,new Intent(getContext(),MyReceiver.class)
//                ,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share,"알람 해제",addActionIntent).build());

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.iu);
        builder.setLargeIcon(largeIcon); //큰아이콘 오른쪽에 뜨는 아이콘

        manager.notify(1000, builder.build());
    }

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), //startActivityForResult의 대용품
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) { //requestCode가 필요하지 않다.
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4; // 용량을 4분의1로 낮춤 -> 이미지는 용량이 크기 때문에 임의로 줄이지 않으면 앱이 죽어버릴수있음
                        bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options); //photoFile로 만든 이미지를 bitmap으로 형식변경

                        ChangeRotate(); //이미지 각도 돌리기

                        //Bitmap 을 구한후, 디코딩 -> Bitmap을 통째로 넘기기는 좀 그러니,
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, output);
                        byte[] bytes = output.toByteArray();

                        saveStorage(bytes);

                        ((MyWork) adapter.getItem(adapterPosition)).setResID(0);
                        ((MyWork) adapter.getItem(adapterPosition)).setEncodeResID(bytes);
                       // adapter.view.setImageViewToBitmap(bytes);
                       adapter.view.setImageViewToBitmap(adapter.list.get(adapterPosition).getEncodeResID());


                        adapter.notifyDataSetChanged();
                        notifiCM();
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_grid, container, false);
        init(viewGroup); // 초기화

        String uid = currentUser.getUid();

        realtime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator(); //아이터레이터를 얻어온다.
                while (child.hasNext()) {
                    if (child.next().getKey().equals(uid)) {
                        groupName = snapshot.child(uid).getValue().toString();
                    }
                }
                getProfile();
                //realtime.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); //cancel

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new MyWork(R.drawable.baseline_add_a_photo_black_18, " ")); // 그 후, 새로운 cardView생산
                ((MyWork) adapter.getItem((adapter.getCount() - 1))).setResID(R.drawable.baseline_add_a_photo_black_18); // 0으로 초기화 된것을 다시 기본사진으로 세팅한다.
                ((MyWork) adapter.getItem((adapter.getCount() - 1))).setEncodeResID(null); // compress한 byte[]값이 그대로 남아있다. null로 초기화 시켜야 한다.

                myData.put("size", adapter.getCount());
                db.collection(groupName).document(name).set(myData, SetOptions.merge());
                adapter.notifyDataSetChanged();
            }
        });

        //adapter에서 view를 쓰자고하니 name을 넣어주기 위해 MyWork_View 사용시 이름을 GridFragment, firebase등등 여러가지를 가져와야 한다. (내용 제거)
        //그렇다고 adapter에서 view에 setText하는 방식으로 메서드를 넣고 gridFragment에서 사용시 에러가 난다. (NPE)
        //결론 : 그냥 여기로 납치해오자.
        adapter.setOnCustomView(new GetCustomView() {
            @Override
            public void getCustomView(View view, int position) {
                ((MyWork_View) view).setUserName(name);
                ((MyWork_View) view).cardView.setOnLongClickListener(v -> { //카드뷰를 길게 클릭했을 경우
                    //정렬
                    for (int i = position; i < adapter.getCount(); i++) {
                        myData.put("myContentsTo" + position, myData.get("myContentsTo" + (position + 1)));
                        myData.put("uri" + position, myData.get("uri" + (position + 1)));
                    }
                    myData.put("uri" + (adapter.getCount() - 1), " ");
                    myData.put("myContentsTo" + (adapter.getCount() - 1), " ");
                    adapter.remove(position);
                    myData.put("size", adapter.getCount());
                    db.collection(groupName).document(name).set(myData, SetOptions.merge());
                    adapter.notifyDataSetChanged(); //일단 adapter의 데이터를 지우고.
                    return false;
                });
            }
        });


        adapter.setOnImageViewClickListener(new ImageViewClickListener() {
            @Override
            public void adapterToFragment(Intent intent, int position, View view) {
                adapterPosition = position;
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    try {
                        photoFile = createFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        //provider를 사용해 외부앱과 연결하여 데이터를 Uri형식으로 연결한다.
                        Uri photoUri = FileProvider.getUriForFile(getContext(), "com.hj.homecleanproject.fileprovider", photoFile);
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                        launcher.launch(intent);
                    }
                }
            }
        });

        adapter.setOnTextViewClickListener(new TextViewClickListener() {
            @Override
            public void textViewClicked(DialogFragment dialog, int position) {
                adapterPosition = position;
                dialog.show(getActivity().getSupportFragmentManager(), "tag");
                GridDialogFragment dialogFragment = (GridDialogFragment) dialog;
                dialogFragment.setDialogResult(new onDialogResultListener() {
                    @Override
                    public void onMyDialogResult(String contents) {
                        msg = contents;
                        myData.put("myContentsTo" + adapterPosition, contents);
                        db.collection(groupName).document(name).set(myData, SetOptions.merge());
                        ((MyWork) adapter.getItem(adapterPosition)).setContent(contents);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        return viewGroup;
    }

    public void init(ViewGroup viewGroup) { //초기화
        fragmentActivity = (FragmentActivity) getActivity();
        familyFragment = new MyFamilyFragment();
        gridView = viewGroup.findViewById(R.id.grid);
        fab = viewGroup.findViewById(R.id.fab);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        workList = new ArrayList<>();
        adapter = new MyGridAdapter();
        myData = new HashMap<>();
        handler = new Handler();

        gridView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        realtime = FirebaseDatabase.getInstance().getReference("users");
    }

    public File createFile() throws IOException { //임시파일생성
        fileName = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()) + ".jpeg";
        //Data/data/패키지/에 app_HomeCleanProject라는 파일이 생성된다.
        File file = File.createTempFile(fileName, ".png", getContext().getDir("HomeCleanProject", Context.MODE_PRIVATE));

        return file;
    }

    private void saveStorage(byte[] bytes) { //Storage에 압축한 file 저장하기! -> 그래서 byte[]로 넣음.
        reference.child(groupName + "/" + fileName).putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(fragmentActivity, "잘들어감!", Toast.LENGTH_SHORT).show();
                loadStorage();
            }
        });
    }

    private void loadStorage() {
        storage.getReferenceFromUrl("gs://homeclean-ba4fc.appspot.com/").child(groupName + "/" + fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //FireStore에 uri집어넣기
                myData.put("uri" + adapterPosition, uri.toString());
                db.collection(groupName).document(name).set(myData, SetOptions.merge());
            }
        });
    }

    public void ChangeRotate() { //이미지 각도 돌리기
        Resources resources = Resources.getSystem(); //어느곳에서든지 이렇게 사용하면 context의 내용을 사용할수있다. 무조건! (직접적인 접근 가능)
        Configuration configuration = resources.getConfiguration();

        // 카메라를 가로/세로로 전환시 Destory까지 내려가 팅기는 현상 발생 -> ConfigurationChanged 사용
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) { //세로일때
            ExifInterface exif = null; //이미지가 갖고 있는 정보의 집합 클래스다
            try {
                exif = new ExifInterface(photoFile.getCanonicalPath());

                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                int rotate = 0;

                switch (exifOrientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                }

                if (rotate != 0) {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    // Setting pre rotate
                    Matrix mtx = new Matrix();
                    mtx.preRotate(rotate);

                    // Rotating Bitmap & convert to ARGB_8888, required by tess
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getProfile() {
        db.collection(groupName).whereEqualTo("groupName", groupName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserInfo userInfo = document.toObject(UserInfo.class);

                        groupName = userInfo.getGroupName();
                        name = userInfo.getName();
                        Log.d("yousin",name);
                        email = userInfo.getEmail();
                        position = userInfo.getPosition();
                    }
                    restoreMyData();
                }

            }
        });
    }

    public void restoreMyData() {
        db.collection(groupName).document(name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Long size = documentSnapshot.getLong("size") == null ? 0 : documentSnapshot.getLong("size");
                Log.d("yousin", size + "");

                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        adapter.addItem(new MyWork(R.drawable.baseline_add_a_photo_black_18, " "));
                        String uri = documentSnapshot.getString("uri" + i) == null ? " " : documentSnapshot.getString("uri" + i);
                        myData.put("uri" + i, uri);

                        String msg = documentSnapshot.getString("myContentsTo" + i) == null ? " " : documentSnapshot.getString("myContentsTo" + i);
                        myData.put("myContentsTo" + i, msg);

                        Observable<URL> observable = Observable.create(emitter -> {
                            if (!uri.equals(" ")) {
                                emitter.onNext(new URL(uri));
                            }
                            emitter.onComplete();
                        });

                        int finalI = i;
                        observable.observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(data -> {
                                    HttpURLConnection connection = (HttpURLConnection) data.openConnection();
                                    connection.setDoInput(true);
                                    connection.connect();

                                    InputStream inputStream = connection.getInputStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                    //Bitmap 을 구한후, 디코딩 -> Bitmap을 통째로 넘기기는 좀 그러니,
                                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, output);
                                    byte[] bytes = output.toByteArray();

                                    handler.post(() -> {
                                        ((MyWork) adapter.getItem(finalI)).setEncodeResID(bytes);
                                        ((MyWork) adapter.getItem(finalI)).setContent(msg);
                                        adapter.notifyDataSetChanged();
                                    });
                                }, e -> {
                                    e.printStackTrace();
                                });
                    }
                }
            }
        });
    }
}