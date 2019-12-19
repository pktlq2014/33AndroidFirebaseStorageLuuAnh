package com.example.a33androidfirebasestorageluuanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button button1;
    ImageView imageView1, imageView2, imageView3;
    ListView listView1;
    EditText editText1;
    DatabaseReference mData;
    int REQUEST_CODE_IMAGE = 1;
    ArrayList<HinhAnh> arrayList;
    HinhAnhAdapter anhAdapter = null;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }





        mData = FirebaseDatabase.getInstance().getReference();
        storageReference = storage.getReferenceFromUrl("gs://project-8897060620842085121.appspot.com");
        anhXa();
        loadData();

        imageView1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });




        // video khoapham
        mData.child("logoKhoaPham").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // đọc dữ liệu từ editText
                // arrayList.add(dataSnapshot.getValue().toString())
                // đọc từ 1 class
                byte[] mangHinh = Base64.decode(dataSnapshot.getValue().toString(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(mangHinh, 0, mangHinh.length);
                imageView3.setImageBitmap(bmp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });








        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // vidoe khoapham
                byte[] mangHinh = ImageView_To_Byte(imageView1);
                String chuoiHinh = Base64.encodeToString(mangHinh, Base64.DEFAULT);
                mData.child("logoKhoaPham").setValue(chuoiHinh);








                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageReference.child("image" + calendar.getTimeInMillis() + ".png");




                imageView1.setDrawingCacheEnabled(true);
                imageView1.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView1.getDrawable()).getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                        Toast.makeText(MainActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Uri uri = taskSnapshot.getUploadSessionUri();
                        Toast.makeText(MainActivity.this, "Thành Công", Toast.LENGTH_SHORT).show();
                        Log.d("AAAA", uri + "");

                        // tạo node trên phần database
                        String ten = editText1.getText().toString();
                        HinhAnh hinhAnh = new HinhAnh(ten, String.valueOf(uri));
                        mData.child("Stogare").push().setValue(hinhAnh, new DatabaseReference.CompletionListener()
                        {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                            {
                                if(databaseError == null)
                                {
                                    Toast.makeText(MainActivity.this, "Đưa Dữ Liệu Hình Ảnh Lên Firebase Thành Công!!!", Toast.LENGTH_SHORT).show();
                                    editText1.setText("");
                                    imageView1.setImageResource(R.mipmap.ic_launcher);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Lỗi Dữ Liệu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    // thiết lập chức năng lấy ảnh chụp từ camera in ra imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView1.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void anhXa()
    {
        listView1 = findViewById(R.id.listView1);
        button1 = findViewById(R.id.butTon1);
        imageView1 = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        editText1 = findViewById(R.id.editText1);
        arrayList = new ArrayList<>();
        anhAdapter = new HinhAnhAdapter(MainActivity.this, R.layout.dong_list_view, arrayList);
        listView1.setAdapter(anhAdapter);
    }

    // đọc dữ liệu ảnh từ firebase về gán cho listview
    private void loadData()
    {
        mData.child("Stogare").addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {


                HinhAnh hinhAnh = dataSnapshot.getValue(HinhAnh.class);
                arrayList.add(new HinhAnh(hinhAnh.getTenAnh(), hinhAnh.getLinkAnh()));
                anhAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }




    private void signInAnonymously(){
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        }) .addOnFailureListener(this, new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
                Log.e("TAG", "signInAnonymously:FAILURE", exception);
            }
        });
    }



    // Test video khoa pham
    public byte[] ImageView_To_Byte(ImageView imageView2)
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView2.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }
}
