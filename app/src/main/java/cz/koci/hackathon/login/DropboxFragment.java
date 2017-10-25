package cz.koci.hackathon.login;

/**
 * Created by vlado on 25/10/2017.
 */

import com.dropbox.core.android.Auth;

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

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        //TODO thumbnails
        //GlideFactory.init(getApplicationContext(), DropboxClientFactory.getClient());
        onClientReady();
    }

    protected abstract void onClientReady();

    protected boolean hasToken() {
        return PrefManager.getToken() != null;
    }
}
