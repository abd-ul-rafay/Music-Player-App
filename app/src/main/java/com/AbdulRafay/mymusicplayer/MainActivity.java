package com.AbdulRafay.mymusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.AbdulRafay.mymusicplayer.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickedListener {

    ActivityMainBinding binding;
    private ArrayList<File> myAllSongs;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MyMusicPlayer);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        myAllSongs = getSongs(Environment.getExternalStorageDirectory());

                        if (myAllSongs.isEmpty()) {
                            binding.textView.setVisibility(View.VISIBLE);
                            binding.textView.setText(R.string.textViewNoSongsFound);
                        }
                        else {

                            items = new String[myAllSongs.size()];

                            for (int i = 0; i < myAllSongs.size(); i++) {
                                items[i] = myAllSongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
                            }

                            MyAdapter myAdapter = new MyAdapter(items, MainActivity.this);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            binding.recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                            binding.recyclerView.setAdapter(myAdapter);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        binding.textView.setVisibility(View.VISIBLE);
                        binding.textView.setText(R.string.textViewValue);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> getSongs(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] songs = file.listFiles();

        if (songs != null) {
            for (File mySongs : songs) {
                if (!mySongs.isHidden() && mySongs.isDirectory()) {
                    arrayList.addAll(getSongs(mySongs));
                }
                else {
                    if ((mySongs.getName().endsWith(".mp3") || mySongs.getName().endsWith(".wav")) && (!mySongs.getName().startsWith("."))) {
                        arrayList.add(mySongs);
                    }
                }
            }
        }
        return arrayList;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, PlayingMusic.class);
        intent.putExtra("MySongsList", myAllSongs);
        intent.putExtra("Position", position);
        intent.putExtra("CurrentSong", items[position]);

            startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
