package com.example.adeba.se_im.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.adeba.se_im.R;
import com.example.adeba.se_im.models.User;
import com.example.adeba.se_im.ui.fragments.LoginFragment;
import com.example.adeba.se_im.utils.Constants;
import com.example.adeba.se_im.utils.SharedPrefUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private final static String TAG = LoginActivity.class.getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;
    public static FirebaseUser firebaseUser;
    public FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    public ProgressDialog progressDialog;

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        mAuth = FirebaseAuth.getInstance();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null)
            goToUserListing();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        progressDialog = new ProgressDialog(this);
    }

    private void bindView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.google_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    public void signInWithGoogle() {
        Intent signInWithGoogleIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInWithGoogleIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        } else {
            //super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressDialog.setMessage("Authenticating");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                            progressDialog.setMessage("Adding User to Database");
                            firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String token = new SharedPrefUtil(LoginActivity.this).getString(Constants.ARG_FIREBASE_TOKEN);
                            Log.e(TAG, "token: " + token);
                            User user = new User(firebaseUser.getUid(),
                                    firebaseUser.getDisplayName(),
                                    new SharedPrefUtil(LoginActivity.this).getString(Constants.ARG_FIREBASE_TOKEN));
                            database.child(Constants.ARG_USERS)
                                    .child(firebaseUser.getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                showToast("User Added to Database");
                                                goToUserListing();
                                            } else {
                                                Log.e(TAG, task.getException().getMessage());
                                                showToast("An error occurred while adding the user");
                                            }
                                        }
                                    });
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showToast("Authentication Failed.");
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void goToUserListing() {
        startActivity(new Intent(this, UserListingActivity.class));
        finish();
    }
}
