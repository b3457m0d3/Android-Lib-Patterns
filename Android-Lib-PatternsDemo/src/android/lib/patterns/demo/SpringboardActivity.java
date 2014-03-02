package android.lib.patterns.demo;

import android.app.Activity;
import android.lib.patterns.nav.Springboard;
import android.os.Bundle;

public class SpringboardActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        ((Springboard)this.findViewById(R.id.springboard)).setAdapter(new SpringboardAdapter(this));
    }
}
