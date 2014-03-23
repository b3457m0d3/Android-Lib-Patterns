package android.lib.patterns.demo;

import android.content.Context;
import android.lib.patterns.form.WizardAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

final class SimpleWizardAdapter extends WizardAdapter {
    private static final String KEY_PAGE_2_OPTION   = "PAGE_2_OPTION";   //$NON-NLS-1$
    private static final String KEY_PAGE_3_USERNAME = "PAGE_3_USERNAME"; //$NON-NLS-1$
    private static final String KEY_PAGE_3_PASSWORD = "PAGE_3_PASSWORD"; //$NON-NLS-1$

    private final LayoutInflater inflater;

    public SimpleWizardAdapter(final Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    protected boolean canGoNext(final int currentPosition, final View page) {
        if (currentPosition == 0) {
            return true;
        }

        if (currentPosition == 1) {
            return ((RadioButton)page.findViewById(R.id.option1)).isChecked() || ((RadioButton)page.findViewById(R.id.option2)).isChecked() || ((RadioButton)page.findViewById(R.id.option3)).isChecked();
        }

        if (currentPosition == 2) {
            return !TextUtils.isEmpty(((EditText)page.findViewById(R.id.username)).getText()) && !TextUtils.isEmpty(((EditText)page.findViewById(R.id.password)).getText());
        }

        return false;
    }

    @Override
    protected View instantiatePage(final ViewGroup container, final int position) {
        final View view;

        switch (position) {
            case 0:
                view = this.inflater.inflate(R.layout.wizard_page_1, container, false);
                container.addView(view);

                break;
            case 1:
                view = this.inflater.inflate(R.layout.wizard_page_2, container, false);
                container.addView(view);

                break;
            case 2:
                view = this.inflater.inflate(R.layout.wizard_page_3, container, false);
                container.addView(view);

                break;

            default:
                throw new IllegalArgumentException();
        }

        return view;
    }

    @Override
    protected void onRestorePageState(final int position, final View page, final Bundle savedInstanceState) {
        switch (position) {
            case 0: SimpleWizardAdapter.restorePage1(page, savedInstanceState); break;
            case 1: SimpleWizardAdapter.restorePage2(page, savedInstanceState); break;
            case 2: SimpleWizardAdapter.restorePage3(page, savedInstanceState); break;
        }
    }

    @Override
    protected void onSavePageState(final int position, final View page, final Bundle outState) {
        switch (position) {
            case 0: SimpleWizardAdapter.savePage1(page, outState); break;
            case 1: SimpleWizardAdapter.savePage2(page, outState); break;
            case 2: SimpleWizardAdapter.savePage3(page, outState); break;
        }
    }

    @SuppressWarnings("unused")
    private static void savePage1(final View page, final Bundle outState) {
        // Does nothing
    }

    @SuppressWarnings("unused")
    private static void restorePage1(final View page, final Bundle savedInstanceState) {
        // Does nothing
    }

    private static void savePage2(final View page, final Bundle outState) {
        if (((RadioButton)page.findViewById(R.id.option1)).isChecked()) {
            outState.putInt(SimpleWizardAdapter.KEY_PAGE_2_OPTION, 1);
        } else if (((RadioButton)page.findViewById(R.id.option2)).isChecked()) {
            outState.putInt(SimpleWizardAdapter.KEY_PAGE_2_OPTION, 2);
        } else if (((RadioButton)page.findViewById(R.id.option3)).isChecked()) {
            outState.putInt(SimpleWizardAdapter.KEY_PAGE_2_OPTION, 3);
        }
    }

    private static void restorePage2(final View page, final Bundle savedInstanceState) {
        final int value = savedInstanceState.getInt(SimpleWizardAdapter.KEY_PAGE_2_OPTION);

        if (value == 1) {
            ((RadioButton)page.findViewById(R.id.option1)).setChecked(true);
        } else if (value == 2) {
            ((RadioButton)page.findViewById(R.id.option2)).setChecked(true);
        } else if (value == 3) {
            ((RadioButton)page.findViewById(R.id.option3)).setChecked(true);
        }
    }

    private static void savePage3(final View page, final Bundle outState) {
        outState.putString(SimpleWizardAdapter.KEY_PAGE_3_USERNAME, ((EditText)page.findViewById(R.id.username)).getText().toString());
        outState.putString(SimpleWizardAdapter.KEY_PAGE_3_PASSWORD, ((EditText)page.findViewById(R.id.password)).getText().toString());
    }

    private static void restorePage3(final View page, final Bundle savedInstanceState) {
        ((EditText)page.findViewById(R.id.username)).setText(savedInstanceState.getString(SimpleWizardAdapter.KEY_PAGE_3_USERNAME));
        ((EditText)page.findViewById(R.id.password)).setText(savedInstanceState.getString(SimpleWizardAdapter.KEY_PAGE_3_PASSWORD));
    }
}
