package android.lib.patterns.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public final class MainActivity extends ListActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, this.getResources().getStringArray(R.array.demos)));
    }

    @Override
    protected void onListItemClick(final ListView listView, final View view, final int position, final long id) {
        switch (position) {
            case 0: this.startActivity(new Intent(this, DashboardActivity.class));       break;
            case 1: this.startActivity(new Intent(this, WizardActivity.class));          break;
            //case 2: this.startActivity(new Intent(this, EndlessListViewActivity.class)); break;
        }
    }
}
