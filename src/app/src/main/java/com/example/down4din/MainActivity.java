package com.example.down4din;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        StatusDialog.StatusDialogListener {

    private FirebaseFirestore db;
    private FirebaseUser user;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            db = FirebaseFirestore.getInstance();

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorSecondary, null));
            setSupportActionBar(toolbar);

            drawerLayout = findViewById(R.id.drawerLayout);
            navView = findViewById(R.id.navView);
            navView.setNavigationItemSelectedListener(this);
            updateNavHeader();

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.openNav, R.string.closeNav
            );
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        new HomeFragment()).commit();
                navView.setCheckedItem(R.id.nav_home);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                openFrag(new HomeFragment());
                break;
            case R.id.nav_profile:
                openFrag(new ProfileFragment());
                break;
            case R.id.nav_messages:
                openFrag(new MessagesFragment());
                break;
            case R.id.nav_settings:
                openFrag(new SettingsFragment());
                break;
            case R.id.status:
                openStatusWindow();
                break;
            case R.id.clear:
                deleteEntry();
                break;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFrag(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer,
                frag
        ).commit();
    }

    public void updateNavHeader() {
        View header = navView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.email)).setText(user.getEmail());
        String name = user.getDisplayName();
        if (!(name == null || name.equals(""))) {
            ((TextView) header.findViewById(R.id.name)).setText(user.getDisplayName());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openStatusWindow() {
        StatusDialog dialog = new StatusDialog();
        dialog.show(getSupportFragmentManager(), "status dialog");
    }

    @Override
    public void updateEntry(String doing, String address) {
        if (user.getDisplayName() == null || user.getDisplayName().trim().equals("")) {
            Toast.makeText(this, "You have not set your display name",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> entry = new HashMap<>();
        entry.put("name", user.getDisplayName());
        entry.put("doing", doing);
        entry.put("address", address);

        db.collection(getResources().getString(R.string.db_collection))
                .document(user.getUid())
                .set(entry)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Updated successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "We were unable to update your status",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteEntry() {
        db.collection(getResources().getString(R.string.db_collection))
                .document(user.getUid())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Status cleared",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "We were unable to clear your status",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
