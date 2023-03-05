package com.example.easytravelapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CommonMethod {

    public CommonMethod(Context context, String message) {
        //Toast Used For Display Message On Bottom To User
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public CommonMethod(Context context, Class<?> nextClass) {
        //Two Type Of Intent (Implicite (Use For Redirect On Installed App)/Explicite (Use For Redirect On One Activitiy To Another Activity))
        //Here Use Explicite Intent Method For Redirect On Next Page
        context.startActivity(new Intent(context, nextClass).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
