package com.mcfac.santropolroulant;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Redirect {

    private static final String LOGIN_ERR = "Something went wrong with your Login information. Please login again";
    public static void redirectToLogin(Context context, FirebaseAuth firebaseAuth){
        Toast.makeText(context,
                LOGIN_ERR,
                Toast.LENGTH_LONG
        ).show();
        firebaseAuth.signOut();
        // Go to Login activity
        context.startActivity(new Intent(context, Login.class));
    }

    public static void redirectToLogin(Context context){
        Toast.makeText(context,
                LOGIN_ERR,
                Toast.LENGTH_LONG
        ).show();
        // Go to Login activity
        context.startActivity(new Intent(context, Login.class));
    }
}
