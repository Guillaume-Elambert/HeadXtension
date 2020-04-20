package com.example.headxtension.Controlleur;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.headxtension.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class SigninFragment extends Fragment {


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }


    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final EditText password = view.findViewById(R.id.passwordSignin);
        final EditText passwordCheck = view.findViewById(R.id.checkPasswordSignin);

        final TextInputLayout passwordLayout = view.findViewById(R.id.password_text_input);
        final TextInputLayout chackPasswordLayout =  view.findViewById(R.id.check_password_text_input);

        //Ajout d'un évennement de saisi sur le champ du mot de passe maître
        password.addTextChangedListener(new TextWatcher(){

            /*
             * Entrée : l'utilisateur saisi du texte
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

                /*
                 * Entrée : le mot de passe a une taille supérieure à 0
                 *          => on continu les vérifs
                 *
                 * Sinon => on remet le texte sur le contenu du mot de passe à la normale
                 */
                if(password.getText().toString().length()>0) {

                    MaterialButton btnValider = view.findViewById(R.id.btnValider);

                    /*
                     * Entrée : le mot de passe ne correspond pas au format requis
                     *          => affichage d'un message d'erreur sous le champ
                     *
                     * Sinon => on remet le texte sur le contenu du mot de passe à la normale
                     */
                    if (!regexMdp(password.getText().toString().trim())) {
                        passwordLayout.setError(getString(R.string.msgRegexMdp));
                        if(btnValider.isEnabled()){
                            btnValider.setEnabled(false);
                        }
                    } else {
                        TextInputLayout checkPwdLayout = view.findViewById(R.id.check_password_text_input);
                        passwordLayout.setHelperText(getString(R.string.msgRegexMdp));
                        if(password.getText().toString().trim().equals(passwordCheck.getText().toString().trim())){
                            Log.d("verif",String.valueOf(password.getText().toString().trim().equals(passwordCheck.getText().toString().trim())));
                            btnValider.setEnabled(true);
                            checkPwdLayout.setError("");
                        } else {
                            btnValider.setEnabled(false);

                            if(passwordCheck.getText().toString().length()>0) {
                                checkPwdLayout.setError("Vos mot de passe ne correspondent pas.");
                            } else {
                                checkPwdLayout.setError("");
                            }
                        }
                    }
                } else {
                    passwordLayout.setHelperText(getString(R.string.msgRegexMdp));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                //Rien
            }


            /*
             * Entrée : la saisi du mit de passe maître est finie
             *          => on ajoute un évennement de saisie sur le chemp de vérif du mdp maître
             */
            @Override
            public void afterTextChanged(Editable s){
                if(regexMdp(password.getText().toString().trim())) {
                    Log.d("debug regex", (String.valueOf(regexMdp(password.getText().toString().trim()))));
                    passwordCheck.addTextChangedListener(new TextWatcher() {

                        /*
                         * Entrée : l'utilisateur saisi du texte
                         */
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            MaterialButton btnValider = view.findViewById(R.id.btnValider);

                            /*
                             * Entrée : le mot de passe correspond au format requis
                             *          => on active le bouton de validation
                             *
                             * Sinon => on désactive le bouton
                             */
                            if (password.getText().toString().trim().equals(passwordCheck.getText().toString().trim())) {
                                btnValider.setEnabled(true);
                            } else {
                                btnValider.setEnabled(false);
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            //Rien
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            TextInputLayout checkPwdLayout = view.findViewById(R.id.check_password_text_input);
                            if(passwordCheck.getText().toString().length()>0 && !passwordCheck.getText().toString().trim().equals(password.getText().toString().trim())) {
                                checkPwdLayout.setError("Vos mot de passe ne correspondent pas.");
                            } else {
                                checkPwdLayout.setError("");
                            }
                        }

                    });
                }
            }

        });

        //ajout d'un évènement de clic sur le bouton de validation du formulaire
        view.findViewById(R.id.btnValider).setOnClickListener(new View.OnClickListener() {

            /*
             * Entrée : l'utilisation clic sur le bouton de validation du formulaire
             */
            @Override
            public void onClick(View view) {
                boolean isGood = false;

                //TODO : Verif bdd existe
                /*
                 * Entrée : la base de données n'existe pas (donc 1ere connexion)
                 *          => affiche le formulaire pour choisir le mdp maître
                 *
                 * Sinon => redirection page de connexion
                 */
                if(true){
                    String passwordStr = password.getText().toString().trim();
                    String passwordCheckStr = passwordCheck.getText().toString().trim();

                    /*
                     * Entrée : l'un des 2 champs de mot de passe n'a pas été renseigné
                     *          => on regarde lequel n'est pas renseigné
                     *
                     * Sinon => on continu les vérifs
                     */
                    if(passwordStr.length()==0 ||passwordCheckStr.length()==0){

                        /*
                         * Entrée : le mot de passe maître n'a pas été renseigné
                         *          => on affiche un message d'erreur concernant le passe maître
                         *
                         * Sinon => on affiche un message d'erreur concernant le champ de vérif du mdp maître
                         */
                        if(passwordStr.length()==0){
                            afficherBoiteDialogue(getString(R.string.titreErreur),getString(R.string.missingPassword),getString(R.string.btnOk), null, false);
                        } else {
                            afficherBoiteDialogue(getString(R.string.titreErreur),getString(R.string.missingPasswordCheck),getString(R.string.btnOk), null, false);
                        }


                    } else {

                        /*
                         * Entrée : Les 2 mots de passes saisis sont identiques
                         *          => on continu les vérifs
                         *
                         * Sinon => on affiche un message d'erreur
                         */
                        if (passwordStr.equals(passwordCheckStr)) {

                            /*
                             * Entrée : le mot de passe est conforme (8 car dont au moins 1 min., maj, chiffre et car. spé.)
                             *          => Redirection sur la page principale de l'application
                             */
                            if(regexMdp(passwordStr)){
                                afficherBoiteDialogue(getString(R.string.titreConfirmation),getString(R.string.msgConfirmation), getString(R.string.btnOk), getString(R.string.btnAnnuler), true);
                            } else {
                                passwordLayout.setError(getString(R.string.msgRegexMdp));
                            }
                        } else {
                            afficherBoiteDialogue(getString(R.string.titreErreurMdp),getString(R.string.msgErreurVerifMdp), getString(R.string.btnOk), null, false);
                        }
                    }

                    passwordCheck.setText("");
                    password.requestFocus();
                    /*
                     * Entrée : Curseur sur le champ de saisie du mot de passe
                     *          => on affiche le clavier
                     */
                    if(password.hasFocus()) {
                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(imm!=null) {
                            imm.toggleSoftInput(0, 0);
                        }
                    }

                } else {
                    NavHostFragment.findNavController(SigninFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });
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



    /**
     * Fonction qui affiche une boîte de dialogue
     * @param titre Le titre de la boîte de dialogue
     * @param message Message de la boîte de dialogue
     * @param btnValidation Le texte de validation de la boîte de dialogue
     * @param btnAnnulation Le texte d'annulation de la boîte de dialogue
     * @param redirection Vrai si appuyer sur le bouton de validation doit rediriger l'utilisateur
     */
    private void afficherBoiteDialogue(@NonNull String titre, @NonNull String message, @NonNull String btnValidation, @Nullable String btnAnnulation, boolean redirection){
        //Création boîte de dialogue
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        //on attribue un titre à notre boite de dialogue
        adb.setTitle(titre);

        //on prépare le contenu du la boîte de dialogue
        View v = getLayoutInflater().inflate(R.layout.alert_dialog_message, null);
        ((TextView) v.findViewById(R.id.alertDialogMessage)).setText(message);
        adb.setView(v);

        /*
         * Entrée : la boîte de dialogue à pour but de rediriger l'utilisateur
         *          => on ajoute un bouton qui ammènera la redirection de l'utilisateur
         *
         * Sinon => boîte de dialogue normale
         */
        if(redirection) {
            //on ajoute un bouton de validation
            adb.setPositiveButton(btnValidation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO : Initialisation de la base de données
                        NavHostFragment.findNavController(SigninFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    }
                }
            );
        } else {
            adb.setPositiveButton(btnValidation, null);
        }

        if(btnAnnulation != null){
            adb.setNegativeButton(btnAnnulation, null);
        }

        adb.show();
    }



    private static int calculatePasswordStrength(String password){

        //total score of password
        int iPasswordScore = 0;

        if( password.length() < 8 )
            return 0;
        else if( password.length() >= 10 )
            iPasswordScore += 2;
        else
            iPasswordScore += 1;

        //if it contains one digit, add 2 to total score
        if( password.matches("(?=.*[0-9]).*") )
            iPasswordScore += 2;

        //if it contains one lower case letter, add 2 to total score
        if( password.matches("(?=.*[a-z]).*") )
            iPasswordScore += 2;

        //if it contains one upper case letter, add 2 to total score
        if( password.matches("(?=.*[A-Z]).*") )
            iPasswordScore += 2;

        //if it contains one special character, add 2 to total score
        if( password.matches("(?=.*[~!@#$%^&*()_-]).*") )
            iPasswordScore += 2;

        return iPasswordScore;

    }
}
