package com.example.down4din;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ProfileFragment extends Fragment {

    private DocumentReference doc;
    private EditText displayNameInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        displayNameInput = v.findViewById(R.id.displayNameInput);
        Button submitBtn = v.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { updateUser(); }
        });

        return v;
    }

    private void updateUser() {
        final String newName = displayNameInput.getText().toString().trim();
        if (!newName.equals("")) {
            // update user
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName).build();
            user.updateProfile(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),
                                    "Profile updated successfully",
                                    Toast.LENGTH_SHORT).show();
                            displayNameInput.setText("");
                            try {
                                ((MainActivity) getActivity()).updateNavHeader();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            // update entry only if we updated the name
                            updateNameInEntry(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),
                                    "We were unable to update your profile",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateNameInEntry(final FirebaseUser user) {
        // update entry if it exists
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        doc = db.collection(getResources().getString(R.string.db_collection))
                .document(user.getUid());
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> entry = documentSnapshot.getData();
                    try {
                        entry.put("name", user.getDisplayName());
                        doc.set(entry); // not gonna bother with OnSuccess & OnFailure listeners
                    } catch (NullPointerException e) {
                        Toast.makeText(getContext(), "We were unable to update your entry",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
