package cz.koci.hackathon.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.dropbox.core.android.Auth;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import cz.koci.hackathon.R;
import cz.koci.hackathon.dashboard.DashboardActivity;
import cz.koci.hackathon.shared.BaseActivity;
import cz.koci.hackathon.utils.FingerprintHelper;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by petrw on 25.10.2017.
 */

public class LoginFragment extends DropboxFragment {
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    private static final String KEY_NAME = "hackathon2017";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    protected boolean loginWhenNoToken() {
        return true;
    }

    @Override
    protected void showLoginFailedDialog() {
        //V Loginu nechceme zobrazovat dialog, ale rovnou přesměrovat na dropbox
        Auth.startOAuth2Authentication(getContext(), getString(R.string.dropbox_app_key));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity activity = ((BaseActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(R.string.login_title);
    }

    @Override
    protected void onClientReady() {
        if (!tryFingerPrintCheck()) {
            proceedToDashboard();
        }
    }

    private void proceedToDashboard() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }


    private boolean tryFingerPrintCheck() {
        // If you’ve set your app’s minSdkVersion to anything lower than 23, then you’ll need to verify that the device is running Marshmallow
        // or higher before executing any fingerprint-related code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get an instance of KeyguardManager and FingerprintManager//
            keyguardManager =
                    (KeyguardManager) getContext().getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getContext().getSystemService(FINGERPRINT_SERVICE);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected()) {
                return false;
            }
            //Check whether the user has granted your app the USE_FINGERPRINT permission//
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

            //Check that the user has registered at least one fingerprint//
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                return false;
            }

            //Check that the lockscreen is secured//
            if (keyguardManager.isKeyguardSecure()) {
                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException | CertificateException | InvalidAlgorithmParameterException | NoSuchProviderException | KeyStoreException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                try {
                    if (initCipher()) {
                        //If the cipher is initialized successfully, then create a CryptoObject instance//
                        cryptoObject = new FingerprintManager.CryptoObject(cipher);

                        // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                        // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                        FingerprintHelper helper = new FingerprintHelper(getContext(), new FingerprintHelper.FingerprintCallback() {
                            @Override
                            public void authenticationFailed() {

                            }

                            @Override
                            public void authenticationSuccessfull() {
                                proceedToDashboard();
                            }
                        });
                        helper.startAuth(fingerprintManager, cryptoObject);
                    }
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException, InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException {
        // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
        keyStore = KeyStore.getInstance("AndroidKeyStore");

        //Generate the key//
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

        //Initialize an empty KeyStore//
        keyStore.load(null);

        //Initialize the KeyGenerator//
        keyGenerator.init(new

                //Specify the operation(s) this key can be used for//
                KeyGenParameterSpec.Builder(KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT |
                        KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build());

        //Generate the key//
        keyGenerator.generateKey();
    }

    //Create a new method that we’ll use to initialize our cipher//
    @TargetApi(Build.VERSION_CODES.M)
    public boolean initCipher() throws InvalidKeyException {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
}
