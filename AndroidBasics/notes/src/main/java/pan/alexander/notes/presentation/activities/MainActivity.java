package pan.alexander.notes.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import pan.alexander.notes.R;
import pan.alexander.notes.databinding.ActivityMainBinding;
import pan.alexander.notes.domain.account.User;
import pan.alexander.notes.presentation.fragments.NotesFragment;
import pan.alexander.notes.presentation.viewmodel.MainActivityViewModel;
import pan.alexander.notes.utils.KeyboardUtils;
import pan.alexander.notes.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding binding;
    private ImageView avatar;
    private TextView textViewUserEmail;
    private Button buttonSignInOut;
    private ActivityResultLauncher<Boolean> accountChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navNotes)
                .setDrawerLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragmentContentMain);
        if (navHostFragment == null) {
            throw new NullPointerException("MainActivity cannot find NavHostFragment");
        }
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        findNavHeaderViews();

        if (Utils.isGmsVersion(this)) {
            observeMainActivityActionRequest();
            observeAccountChanges();
            observeGetAccountResult();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        observeKeyboardVisibilityChanges();
    }

    private void findNavHeaderViews() {
        View navHeader = binding.navView.getHeaderView(0);
        if (navHeader != null) {
            avatar = navHeader.findViewById(R.id.imageUserPhoto);
            textViewUserEmail = navHeader.findViewById(R.id.textViewUserEmail);
            buttonSignInOut = navHeader.findViewById(R.id.buttonSignInOut);
        }
    }

    private void observeMainActivityActionRequest() {
        mainActivityViewModel.getMainActivityActionRequest().observe(this, action -> {
            if (action == MainActivityActionRequest.OPEN_NAV_DRAWER) {
                binding.drawerLayout.open();
            } else if (action == MainActivityActionRequest.LAUNCH_ACCOUNT_CHOOSER) {
                launchAccountChooser(false);
            }
            mainActivityViewModel.clearMainActivityActionRequest();
        });
    }

    private void observeAccountChanges() {
        mainActivityViewModel.getUserAccountLiveData().observe(this, this::setNavHeaderAvatarAndData);
    }

    private void observeGetAccountResult() {
        accountChooser = registerForActivityResult(new PickAccount(), result -> {
            if (result.second == PickAccount.ANONYMOUS_UPGRADE_MERGE_CONFLICT) {
                mainActivityViewModel.moveUserDataFromAnonymousToRegisteredAccount();
            } else if (result.first != null) {
                showSnackBar(result.first, Snackbar.LENGTH_LONG);
                mainActivityViewModel.getUserAccountError(result);
            } else {
                binding.drawerLayout.open();
                mainActivityViewModel.getUserAccountSuccess();
            }
        });
    }

    private void showSnackBar(String text, @BaseTransientBottomBar.Duration int duration) {

        if (text.isEmpty()) {
            return;
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragmentContentMain);
        if (navHostFragment == null) {
            throw new NullPointerException("MainActivity cannot find NavHostFragment");
        }

        for (Fragment fragment : navHostFragment.getChildFragmentManager().getFragments()) {
            if (fragment instanceof NotesFragment) {
                NotesFragment notesFragment = (NotesFragment) fragment;
                notesFragment.showSnackBar(text, duration);
                return;
            }
        }

        Snackbar.make(binding.getRoot(), text, duration).show();
    }

    private void setNavHeaderAvatarAndData(User user) {

        if (user == null || user.isAnonymous()) {

            setUserSignInOnClickListener();

            avatar.setImageResource(R.drawable.default_user_photo);
            textViewUserEmail.setText(R.string.nav_header_mail);
            buttonSignInOut.setText(R.string.sign_in);
        } else {
            Glide.with(this)
                    .load(user.getPhotoUri())
                    .error(R.drawable.default_user_photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatar);

            textViewUserEmail.setText(user.getEmail());
            buttonSignInOut.setText(R.string.sign_out);
            buttonSignInOut.setOnClickListener(v -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                mainActivityViewModel.signOutUser();
            });
        }
    }

    private void setUserSignInOnClickListener() {
        View.OnClickListener registerUserOnClickListener = v ->
                launchAccountChooser(true);
        avatar.setOnClickListener(registerUserOnClickListener);
        buttonSignInOut.setOnClickListener(registerUserOnClickListener);
    }

    private void launchAccountChooser(boolean allowAnonymousUsersAutoUpgrade) {
        if (accountChooser != null) {
            accountChooser.launch(allowAnonymousUsersAutoUpgrade);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopObservingKeyboardVisibilityChanges();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragmentContentMain);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = binding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void observeKeyboardVisibilityChanges() {
        KeyboardUtils.addKeyboardToggleListener(this, isVisible ->
                mainActivityViewModel.setKeyboardActivated(isVisible));
    }

    private void stopObservingKeyboardVisibilityChanges() {
        KeyboardUtils.removeAllKeyboardToggleListeners();
    }

}
