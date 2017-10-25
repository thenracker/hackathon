package cz.koci.hackathon.login;

/**
 * Created by vlado on 25/10/2017.
 */

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.dropbox.core.android.Auth;

import cz.koci.hackathon.R;
import cz.koci.hackathon.login.service.DropboxClientFactory;
import cz.koci.hackathon.shared.BaseFragment;
import cz.koci.hackathon.utils.PrefManager;


/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();

        String token = PrefManager.getToken();
        if (token == null) {
            token = Auth.getOAuth2Token();
            if (token != null) {
                PrefManager.setToken(token);
                initAndLoadData(token);
            } else if (loginWhenNoToken()) {
                showLoginFailedDialog();
            }
        } else {
            initAndLoadData(token);
        }

        String userId = Auth.getUid();
        String storedId = PrefManager.getUserId();
        if (userId != null && (storedId == null || !userId.equals(storedId))) {
            PrefManager.setUserId(userId);
        }
    }

    protected void showLoginFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(R.string.login_failed_retry);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Auth.startOAuth2Authentication(getContext(), getString(R.string.dropbox_app_key));
            }
        });

        builder.create().show();
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        //TODO thumbnails
        //GlideFactory.init(getApplicationContext(), DropboxClientFactory.getClient());
        onClientReady();
    }

    protected boolean loginWhenNoToken() {
        return true;
    }

    protected abstract void onClientReady();

    protected boolean hasToken() {
        return PrefManager.getToken() != null;
    }
}
