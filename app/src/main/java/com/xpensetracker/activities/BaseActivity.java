package com.xpensetracker.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.xpensetracker.BuildConfig;
import com.xpensetracker.R;

public class BaseActivity extends AppCompatActivity {

    protected String TAG = getClass().getSimpleName(); // Initialize TAG directly
    private ProgressDialog mProgressDialog;
    // Safer way to check build type
    private boolean DEBUG = BuildConfig.DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setTAG(String activityName) {
        if (activityName != null) {
            TAG = activityName;
        }
    }

    protected void showLogs(String msg) {
        if (DEBUG && msg != null) {
            Log.i(TAG != null ? TAG : getClass().getSimpleName(), msg);
        }
    }

    protected void showLogs(String tag, String msg) {
        if (DEBUG && msg != null) {
            Log.i(tag != null ? tag : TAG, msg);
        }
    }

    public View getView() {
        return getWindow().getDecorView().getRootView();
    }

    protected void showProgress(String msg, Boolean cancellable) {
        if (isFinishing()) return;

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            dismissProgress();
        }

        if (msg != null) {
            mProgressDialog = ProgressDialog.show(this,
                    getString(R.string.app_name),
                    msg,
                    false,
                    cancellable != null ? cancellable : false);
        }
    }

    protected void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void showToast(String msg) {
        if (msg != null && !isFinishing()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showAlert(String msg) {
        if (msg == null || isFinishing()) return;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                });

        if (!isFinishing()) {
            builder.create().show();
        }
    }

    protected void showsnackBar(String msg, int length) {
        if (msg == null) return;

        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, msg, length).show();
        }
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error hiding keyboard: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgress();
        super.onDestroy();
    }
}