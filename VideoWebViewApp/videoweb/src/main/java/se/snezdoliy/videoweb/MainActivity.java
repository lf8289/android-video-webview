package se.snezdoliy.videoweb;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cprcrack.VideoEnabledWebChromeClient;
import com.cprcrack.VideoEnabledWebView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static final String VIDEO_URL = "http://admin.youplay.se/single_player/9";
        public static final String GOOGLE_URL = "http://google.se";


        private VideoEnabledWebView rootWebView;
        private VideoEnabledWebChromeClient webChromeClient;
        public PlaceholderFragment() {
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            initWebView(rootView);
            return rootView;
        }

        @SuppressLint("NewApi")
        private void initWebView(View v) {
            rootWebView = (VideoEnabledWebView) v.findViewById(R.id.videoWebView);
            if(rootWebView != null) {
                WebSettings ws = rootWebView.getSettings();
                ws.setJavaScriptEnabled(true);
                ws.setDomStorageEnabled(true);

                // Initialize the VideoEnabledWebChromeClient and set event handlers
                View nonVideoLayout = v.findViewById(R.id.nonVideoLayout); // Your own view, read class comments
                ViewGroup videoLayout = (ViewGroup) v.findViewById(R.id.videoLayout); // Your own view, read class comments
                View loadingView = v.findViewById(R.id.videoLoading); // Your own view, read class comments
                webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, rootWebView) // See all available constructors...
                {
                    // Subscribe to standard events, such as onProgressChanged()...
                    @Override
                    public void onProgressChanged(WebView view, int progress)
                    {
                        // Your code...
                    }
                };
                webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
                {
                    @Override
                    public void toggledFullscreen(boolean fullscreen)
                    {
                        // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                        if (fullscreen)
                        {
                            WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                            attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            getActivity().getWindow().setAttributes(attrs);
                            if (android.os.Build.VERSION.SDK_INT >= 14)
                            {
                                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                            }
                        }
                        else
                        {
                            WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                            attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            getActivity().getWindow().setAttributes(attrs);
                            if (android.os.Build.VERSION.SDK_INT >= 14)
                            {
                                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                            }
                        }

                    }
                });
                rootWebView.setWebChromeClient(webChromeClient);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            rootWebView.stopLoading();
            rootWebView.loadUrl(VIDEO_URL);
        }
    }

}
