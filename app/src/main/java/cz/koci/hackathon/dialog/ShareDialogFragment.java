package cz.koci.hackathon.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.koci.hackathon.R;
import cz.koci.hackathon.model.Metadata;
import cz.koci.hackathon.model.dto.SharedLinkArgument;
import cz.koci.hackathon.model.dto.SharedLinkSettings;
import cz.koci.hackathon.service.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by daniel on 25.10.17.
 */

public class ShareDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String ARG_ID = "ARG_ID";
    private static final String ARG_PATH = "ARG_PATH";

    private String id;
    private String path;
    private Button positiveButton;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.emailEditText)
    EditText emailEditText;
    @BindView(R.id.noteEditText)
    EditText noteEditText;
    @BindView(R.id.passwordCheckedTextView)
    CheckedTextView passwordCheckedTextView;

    public static ShareDialogFragment newInstance(String id, String path) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, id);
        args.putSerializable(ARG_PATH, path);

        ShareDialogFragment fragment = new ShareDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            id = args.getString(ARG_ID);
            path = args.getString(ARG_PATH);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_share_dialog, null);
        ButterKnife.bind(this, view);

        passwordCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordCheckedTextView.setChecked(!passwordCheckedTextView.isChecked());

                if (passwordCheckedTextView.isChecked()) {
                    passwordEditText.setVisibility(View.VISIBLE);
                } else {
                    passwordEditText.setText("");
                    passwordEditText.setVisibility(View.GONE);
                }
            }
        });

        builder.setView(view)
                .setTitle(R.string.share_dialog_title)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setOnClickListener(ShareDialogFragment.this);
                }

            }
        });

        return dialog;

    }

    @Override
    public void onClick(View view) {
        String email = emailEditText.getText().toString();
        if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.error_missing_email));
            return;
        }

        String note = noteEditText.getText().toString();

        positiveButton.setEnabled(false);
        showProgress(true);
        createSharedLink(email, note);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void createSharedLink(final String email, final String note) {
        String password = passwordEditText.getText().toString();

        SharedLinkSettings settings;
        if (password.isEmpty()) {
            settings = new SharedLinkSettings();
        } else {
            settings = new SharedLinkSettings(password);
        }

        SharedLinkArgument argument = new SharedLinkArgument(path, settings);

        Call<Metadata> call = RestClient.get().createSharedLink(argument);
        call.enqueue(new Callback<Metadata>() {
            @Override
            public void onResponse(Call<Metadata> call, Response<Metadata> response) {
                showProgress(false);

                Metadata data = response.body();

                if (response.isSuccessful() && data != null) {
                    shareLink(data.getUrl(), email, note);
                    return;
                }

                positiveButton.setEnabled(true);

            }

            @Override
            public void onFailure(Call<Metadata> call, Throwable t) {
                showProgress(false);
                positiveButton.setEnabled(true);

            }
        });
    }

    private void shareLink(String url, String email, String note) {
        dismiss();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, note + "/n/n" + url);

        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
    }
}
