package com.example.headxtension.Controlleur;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.headxtension.Modele.HeadXtensionDAO;
import com.example.headxtension.Modele.Session;
import com.example.headxtension.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {
    private NavController navController;
    private Session session = Session.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        Log.d("entréeLogin",String.valueOf(session.getRegistrationState()));

        if(session.getRegistrationState()) {
            final TextInputLayout passwordLayout = view.findViewById(R.id.password_text_input);
            final EditText password = view.findViewById(R.id.passwordLogin);
            final MaterialButton btnConnexion = view.findViewById(R.id.btnConnexion);

            //ActivityNavigator activityNavigator = new ActivityNavigator(this);
            //activityNavigator.navigate(activityNavigator.createDestination().setIntent(new Intent(requireContext(), MainActivity.class)), null, null, null);
            //NavHostFragment.findNavController(this).navigate (new Intent(requireContext(), MainActivity.class));

            password.addTextChangedListener(new TextWatcher() {

                /*
                 * Entrée : l'utilisateur saisi du texte
                 */
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(regexMdp(password.getText().toString())){

                        if(!btnConnexion.isEnabled()){
                            btnConnexion.setEnabled(true);
                        }

                        btnConnexion.setOnClickListener(new View.OnClickListener() {

                            /*
                             * Entrée : l'utilisation clic sur le bouton de validation du formulaire
                             */
                            @Override
                            public void onClick(View view) {
                                if (HeadXtensionDAO.checkBDOpenable(requireActivity().getBaseContext(), password.getText().toString())) {
                                    session.setAuthenticationState(true);
                                    //startActivity(new Intent(requireActivity(), MainActivity.class));
                                    navController.navigate(R.id.action_LoginRegisterNavigation_to_MainActivity);
                                } else {
                                    passwordLayout.setError(getString(R.string.loginWrongPassword));
                                }
                            }
                        });

                    } else {
                        if(btnConnexion.isEnabled()){
                            btnConnexion.setEnabled(false);
                        }

                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Rien
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Rien
                }
            });
        } else {
                navController.navigate(R.id.action_LoginFragment_to_SigninFragment);
        }
    }


    /**
     * Fonction qui verifie si la chaîne de caractères passée en paramètre correspond au format suivant :
     *      - au moins 8 caractères
     *      - au moins 1 lettre minuscule
     *      - au moins 1 lettre majuscule
     *      - au moins 1 chiffre
     *      - au moins 1 caractère spécial
     *      - pas d'espaces
     * @param password le mot de passe saisi
     * @return vrai si le mot de passe passé en paramètre correspond au format
     */
    private boolean regexMdp(String password){
        boolean exec=false;
        Pattern mdpPatern = Pattern.compile("^" +
                "(?=.*[a-z])" +         //au moins 1 lettre minuscule
                "(?=.*[A-Z])" +         //au moins 1 lettre majuscule
                "(?=.*[0-9])" +         //au moins 1 chiffre
                "(?=.*[@#$%^&+=])" +    //au moins 1 caractère spécial
                "(?=\\S+$)" +           //pas d'espaces
                ".{8,}" +               //au moins 8 caractères
                "$");

        if(mdpPatern.matcher(password).matches()){
            exec = true;
        }
        return exec;
    }
}
