package bd.gov.thakurgaon.thakurgaon;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.Toast;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.design.widget.CoordinatorLayout;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.app.Activity;
import android.content.Context;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private CoordinatorLayout mCLayout;

    private WebView view;
    private WebView splashView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        mContext = getApplicationContext();
        mActivity = FullscreenActivity.this;

        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        view = (WebView) findViewById(R.id.webView);
        view.setWebViewClient(new WebViewClient());

        // splash view code snippet
        splashView = (WebView) findViewById(R.id.splashView);
        splashView.setWebViewClient(new WebViewClient());
        splashView.loadUrl("file:///android_asset/welcome.html");
        // splash view code snippet

        int DELAY = 4000;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run()
            {
                splashView.setVisibility(View.GONE);
                view.loadUrl("file:///android_asset/index.html");
            }
        }, DELAY);

        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.setWebChromeClient(new WebChromeClient());
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setAllowFileAccessFromFileURLs(true);
        view.getSettings().setAllowUniversalAccessFromFileURLs(true);
        view.getSettings().setAllowFileAccess(true);
        view.getSettings().setAllowContentAccess(true);

        // reload code
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                view.reload();
                view.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        swipeLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    protected void showAppExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("Please confirm");
        builder.setMessage("No back history found, want to exit the app?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when user want to exit the app
                // Let allow the system to handle the event, such as exit the app
                FullscreenActivity.super.onBackPressed();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when want to stay in the app
                Toast.makeText(mContext,"thank you",Toast.LENGTH_LONG).show();
            }
        });

        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }

    @Override
    public void onBackPressed(){
        if(view.canGoBack()){
            // If web view have back history, then go to the web view back history
            view.goBack();
            Snackbar.make(mCLayout,"Go to back history",Snackbar.LENGTH_LONG).show();
        }else {
            // Ask the user to exit the app or stay in here
            showAppExitDialog();
        }
    }


}
