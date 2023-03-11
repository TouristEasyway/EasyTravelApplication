package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.HotelHistoryAdapter;
import com.example.easytravelapplication.Model.HotelHistoryListResponse;
import com.example.easytravelapplication.databinding.ActivityHotelBokkingHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HotelBokkingHistoryActivity extends AppCompatActivity {


    ActivityHotelBokkingHistoryBinding binding;
    ArrayList<HotelHistoryListResponse> arrayList;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_hotel_bokking_history);
        getSupportActionBar().setTitle("Hotel Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        ProgressDialog pd = new ProgressDialog(HotelBokkingHistoryActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        reference = FirebaseDatabase.getInstance().getReference("Book Hotel");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HotelHistoryListResponse data = snapshot.getValue(HotelHistoryListResponse.class);
                        arrayList.add(data);
                    }

                    HotelHistoryAdapter adapter = new HotelHistoryAdapter(arrayList, HotelBokkingHistoryActivity.this);
                    binding.rvHotel.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();
                Toast.makeText(HotelBokkingHistoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Use For Close Application
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}