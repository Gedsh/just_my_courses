package pan.alexander.notes;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import pan.alexander.notes.di.ApplicationComponent;
import pan.alexander.notes.di.DaggerApplicationComponent;
import pan.alexander.notes.di.RepositoryModule;
import pan.alexander.notes.di.RoomModule;

public class App extends MultiDexApplication {

    static {
        System.setProperty("rx2.purge-enabled", "false");
        System.setProperty("rx2.purge-period-seconds", "60");
        System.setProperty("rx2.computation-threads", "1");
    }

    public final static String LOG_TAG = "alexander.notes";

    private static App instance;

    private ApplicationComponent daggerComponent;
    private Handler handler;

    @Override
    public void onCreate() {

        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        instance = this;

        if (daggerComponent == null) {
            initDaggerComponents();
            initGlobalHandler();
        }
    }

    private void initDaggerComponents() {
        daggerComponent = DaggerApplicationComponent.builder()
                .roomModule(new RoomModule(instance))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    private void initGlobalHandler() {
        Looper looper = Looper.getMainLooper();
        if (looper != null) {
            handler = new Handler(looper);
        }
    }

    public static App getInstance() {
        return instance;
    }

    public ApplicationComponent getDaggerComponent() {
        return daggerComponent;
    }

    public Handler getHandler() {
        return handler;
    }

    @SuppressWarnings("unused")
    private void getSigningHash() {
        try {
            PackageManager packageManager = getPackageManager();
            String packageName = getPackageName();

            @SuppressLint("PackageManagerGetSignatures")
            Signature[] signatureArray = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;

            byte[] byteSign = signatureArray[0].toByteArray();
            byteSign = CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(byteSign)).getEncoded();

            byte[] digest = MessageDigest.getInstance("SHA").digest(byteSign);
            String hex = String.format("%064x", new BigInteger(1, digest));
            Log.d(LOG_TAG, hex);
        } catch (PackageManager.NameNotFoundException | CertificateException | NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Get signature exception", e);
        }
    }
}
