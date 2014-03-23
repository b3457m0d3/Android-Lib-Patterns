package android.lib.patterns.demo;

import android.app.Activity;
import android.lib.patterns.nav.Dashboard;
import android.os.Bundle;

public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_dashboard);

        ((Dashboard)this.findViewById(R.id.dashboard)).setAdapter(new DashboardAdapter(this));
    }
}
