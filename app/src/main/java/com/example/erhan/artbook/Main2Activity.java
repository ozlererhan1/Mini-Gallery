package com.example.erhan.artbook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Main2Activity extends AppCompatActivity {
    Bitmap Selectedimage;
    static SQLiteDatabase database;
    EditText isim;
    String ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        isim=(EditText) findViewById(R.id.add_name);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        ImageView resim = (ImageView) findViewById(R.id.imageView);


        if (info.equalsIgnoreCase("new")) {


        } else {

            String name = intent.getStringExtra("name");
            isim.setText(name);
            int position = intent.getIntExtra("position", 0);
            resim.setImageBitmap(MainActivity.artImage.get(position));

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==33) {
            Selectedimage = (Bitmap) data.getExtras().get("data");//Çekilen resim id olarak bitmap şeklinde alındı ve imageview'e atandı
            Intent intent = new Intent();
            ImageView resim = (ImageView) findViewById(R.id.imageView);
            resim.setImageBitmap(Selectedimage);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void add_pictures(View view){
         Intent kamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Resim çekme isteği ve activity başlatılıp id'si tanımlandı
          startActivityForResult(kamera,33);

    }
    public void save_picture(View view){
        ByteArrayOutputStream outpustream=new ByteArrayOutputStream();
        Selectedimage.compress(Bitmap.CompressFormat.PNG,50,outpustream);
        byte [] byteArray=outpustream.toByteArray();
        Toast.makeText(getApplicationContext(),"Başarılı",Toast.LENGTH_LONG).show();
        ad=isim.getText().toString();
try {
        database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR, image BLOB)");

        String sqlString = "INSERT INTO arts (name, image) VALUES (?, ?)";
        SQLiteStatement statement = database.compileStatement(sqlString);
        statement.bindString(1,ad);
        statement.bindBlob(2,byteArray);
        statement.execute();

    } catch (Exception e) {
        e.printStackTrace();
    }

    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("info", "new");
        startActivity(intent);



    }
}
