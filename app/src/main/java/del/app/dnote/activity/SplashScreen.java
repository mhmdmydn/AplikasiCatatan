package del.app.dnote.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import del.app.dnote.R;
import android.os.Handler;
import android.content.Intent;

public class SplashScreen extends AppCompatActivity {
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
		
		
        new Handler().postDelayed(new Runnable(){

                @Override
                public void run() {
                    Intent next = new Intent();
                    next.setClass(getApplicationContext(), MainActivity.class);
                   startActivity(next);
                    finish();
                }
            }, 3000);
    }
    
}
