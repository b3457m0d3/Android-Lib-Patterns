package android.lib.patterns.demo;

import android.app.Activity;
import android.lib.patterns.form.Wizard;
import android.lib.patterns.form.WizardAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public final class WizardActivity extends Activity implements View.OnClickListener {
    private Wizard        wizard;
    private WizardAdapter adapter;

    private Button back;
    private Button next;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_wizard);

        this.wizard = (Wizard)this.findViewById(R.id.wizard);
        this.wizard.setAdapter(this.adapter = new SimpleWizardAdapter(this));

        this.adapter.notifyDataSetChanged();

        this.back = (Button)this.findViewById(R.id.back);
        this.next = (Button)this.findViewById(R.id.next);

        this.back.setOnClickListener(this);
        this.next.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.back: this.onBack(); break;
            case R.id.next: this.onNext(); break;
        }
    }

    private void onBack() {
        this.wizard.goBack();

        this.back.setEnabled(this.wizard.getCurrentItem() > 0);
        this.next.setText("Next");
    }

    private void onNext() {
        if (this.wizard.getCurrentItem() == this.adapter.getCount() - 1) {
            Toast.makeText(this, "This is the end of the demo page", Toast.LENGTH_SHORT).show();
        }

        this.back.setEnabled(true);

        if (this.adapter.canGoNext(this.wizard.getCurrentItem())) {
            this.wizard.goNext();

            if (this.wizard.getCurrentItem() == this.adapter.getCount() - 1) {
                this.next.setText("Finish");
            }
        } else {
            Toast.makeText(this, "You must fill in the information before proceeding to the next page", Toast.LENGTH_SHORT).show();
        }
    }
}
