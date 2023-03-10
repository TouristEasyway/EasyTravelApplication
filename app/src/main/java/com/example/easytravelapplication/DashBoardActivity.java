package com.example.easytravelapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easytravelapplication.Adapter.ManagePackageAdapter;
import com.example.easytravelapplication.Adapter.UserCarAdapter;
import com.example.easytravelapplication.Adapter.UserCarHistoryAdapter;
import com.example.easytravelapplication.Adapter.UserDashBoardPackageAdapter;
import com.example.easytravelapplication.Adapter.UserHotelAdapter;
import com.example.easytravelapplication.Adapter.UserHotelHistoryAdapter;
import com.example.easytravelapplication.Adapter.UserPackageHistoryAdapter;
import com.example.easytravelapplication.Model.CarHistoryResponse;
import com.example.easytravelapplication.Model.HotelHistoryListResponse;
import com.example.easytravelapplication.Model.HotelListResponse;
import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Model.PackageHistoryListResponse;
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityDashBoardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {

    ActivityDashBoardBinding binding;
    FirebaseAuth auth;
    SharedPreferences sp;
    HomeAdapter adapter;
    DatabaseReference reference;
    private ProgressDialog pd;


    String[] adminList = {"Manage User", "Manage Package", "Package Purchase History", "Manage Hotels", "Hotel Booking History", "Manage Car Rents", "Car Rental History"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board);
        auth = FirebaseAuth.getInstance();
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        sp.getString(AppConstant.USERTYPE, "");
        String userType = sp.getString(AppConstant.USERTYPE, "");
        pd = new ProgressDialog(this);

        if (userType.equals("User")) {
            getSupportActionBar().setTitle("Home");
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            setPackageData();
            setPackageHistoryData();
            setHotelData();
            setHotelHistoryData();
            setCarData();
            setCarHistoryData();


//            adapter = new HomeAdapter(this, userList);
        } else {
            getSupportActionBar().setTitle("Admin");
            binding.rvDashboard.setVisibility(View.VISIBLE);
            adapter = new HomeAdapter(this, adminList);
            binding.rvDashboard.setAdapter(adapter);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setCarHistoryData() {

        binding.rvCarHistory.setVisibility(View.VISIBLE);
        binding.tvCarHistory.setVisibility(View.VISIBLE);
        binding.carHistoryViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Book Car");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<CarHistoryResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CarHistoryResponse data = snapshot.getValue(CarHistoryResponse.class);
                        arrayList.add(data);
                    }

                    UserCarHistoryAdapter adapter = new UserCarHistoryAdapter(arrayList);
                    binding.rvCarHistory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.carHistoryViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashBoardActivity.this, CarRentalHistoryActivity.class);
            }
        });
    }

    private void setCarData() {


        binding.rvCar.setVisibility(View.VISIBLE);
        binding.tvCar.setVisibility(View.VISIBLE);
        binding.carViewAll.setVisibility(View.VISIBLE);

        reference = FirebaseDatabase.getInstance().getReference("Manage Car");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<ManageCarResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ManageCarResponse data = snapshot.getValue(ManageCarResponse.class);
                        arrayList.add(data);
                    }

                    UserCarAdapter adapter = new UserCarAdapter(arrayList);
                    binding.rvCar.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.carViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashBoardActivity.this, ManageCarRentActivity.class);
            }
        });
    }

    private void setHotelHistoryData() {


        binding.rvHotelHistory.setVisibility(View.VISIBLE);
        binding.tvHotelHistory.setVisibility(View.VISIBLE);
        binding.hotelHistoryViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Book Hotel");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<HotelHistoryListResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HotelHistoryListResponse data = snapshot.getValue(HotelHistoryListResponse.class);
                        arrayList.add(data);
                    }

                    UserHotelHistoryAdapter adapter = new UserHotelHistoryAdapter(arrayList);
                    binding.rvHotelHistory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.hotelHistoryViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashBoardActivity.this, HotelBokkingHistoryActivity.class);
            }
        });
    }

    private void setHotelData() {


        binding.rvHotel.setVisibility(View.VISIBLE);
        binding.tvHotel.setVisibility(View.VISIBLE);
        binding.hotelViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Manage Hotel");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<HotelListResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HotelListResponse data = snapshot.getValue(HotelListResponse.class);
                        arrayList.add(data);
                    }

                    UserHotelAdapter adapter = new UserHotelAdapter(arrayList);
                    binding.rvHotel.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.hotelViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashBoardActivity.this, ManageHotelActivity.class);
            }
        });
    }

    private void setPackageHistoryData() {


        binding.rvPackageHistory.setVisibility(View.VISIBLE);
        binding.tvPackageHistory.setVisibility(View.VISIBLE);
        binding.packageHistoryViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Book Packages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<PackageHistoryListResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageHistoryListResponse data = snapshot.getValue(PackageHistoryListResponse.class);
                        arrayList.add(data);
                    }

                    UserPackageHistoryAdapter adapter = new UserPackageHistoryAdapter(arrayList);
                    binding.rvPackageHistory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.packageHistoryViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashBoardActivity.this, PackagePurchaseHistoryActivity.class);
            }
        });
    }

    private void setPackageData() {
        binding.rvPackage.setVisibility(View.VISIBLE);
        binding.tvPackage.setVisibility(View.VISIBLE);
        binding.packageViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Manage Packages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<PackageListResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageListResponse data = snapshot.getValue(PackageListResponse.class);
                        arrayList.add(data);
                    }

                    UserDashBoardPackageAdapter adapter = new UserDashBoardPackageAdapter(arrayList);
                    binding.rvPackage.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.packageViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashBoardActivity.this, ManagePackageActivity.class);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.opetion_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishAffinity();
        }
        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            sp = this.getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
            sp.edit().clear().commit();
            openLogin();
        }
        if (item.getItemId() == R.id.profile) {
            openProfile();
        }
        return true;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (auth.getCurrentUser() == null) {
//            openLogin();
//        }
//    }

    private void openProfile() {
        startActivity(new Intent(DashBoardActivity.this, ProfileActivity.class));
//        finish();
    }

    private void openLogin() {
        startActivity(new Intent(DashBoardActivity.this, LogInActivity.class));
        finish();
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyHolder> {

        Context context;
        String[] arrayList;

        //Creating Parameterized Constructor
        public HomeAdapter(DashBoardActivity dashBoardActivity, String[] arrayList) {
            this.context = dashBoardActivity;
            this.arrayList = arrayList;
        }

        //Use This Method For Set Custom XML File
        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
            return new MyHolder(view);
        }

        //Initialized Id Method For Connect XML File Id
        public class MyHolder extends RecyclerView.ViewHolder {

            TextView name;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.tv_name);
            }
        }

        //Initialized Method For Set Data
        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.name.setText(arrayList[position]);

            //Click Event On Specific CustomView
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList[position].equals("Manage User")) {
                        new CommonMethod(context, ManageUserActivity.class);
                    } else if (arrayList[position].equals("Manage Package") || arrayList[position].equals("Package")) {
                        new CommonMethod(context, ManagePackageActivity.class);
                    } else if (arrayList[position].equals("Package Purchase History")) {
                        new CommonMethod(context, PackagePurchaseHistoryActivity.class);
                    } else if (arrayList[position].equals("Manage Hotels")) {
                        new CommonMethod(context, ManageHotelActivity.class);
                    } else if (arrayList[position].equals("Hotel Booking History")) {
                        new CommonMethod(context, HotelBokkingHistoryActivity.class);
                    } else if (arrayList[position].equals("Manage Car Rents")) {
                        new CommonMethod(context, ManageCarRentActivity.class);
                    } else if (arrayList[position].equals("Car Rental History")) {
                        new CommonMethod(context, CarRentalHistoryActivity.class);
                    } else {

                    }
                }
            });

        }

        //Define Array Size
        @Override
        public int getItemCount() {
            return arrayList.length;
        }
    }

}