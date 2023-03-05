package com.example.easytravelapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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

import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityDashBoardBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DashBoardActivity extends AppCompatActivity {

    ActivityDashBoardBinding binding;
    FirebaseAuth auth;
    SharedPreferences sp;
    HomeAdapter adapter;


    String[] adminList = {"Manage User", "Manage Package", "Package Purchase History", "Manage Hotels", "Hotel Booking History", "Manage Car Rents", "Car Rental History"};
    String[] userList = {"Package","Package Purchase History", "Manage Hotels", "Hotel Booking History", "Manage Car Rents", "Car Rental History"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board);
        auth = FirebaseAuth.getInstance();
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        sp.getString(AppConstant.USERTYPE, "");
        String  userType = sp.getString(AppConstant.USERTYPE, "");

        if (userType.equals("User")){
            adapter = new HomeAdapter(this, userList);
        }
        else{
            adapter = new HomeAdapter(this, adminList);
        }

        binding.rvDashboard.setAdapter(adapter);

        Log.d("TAG", sp.getString(AppConstant.USERTYPE,""));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.opetion_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            auth.signOut();
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
                    }else {

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