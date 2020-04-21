package com.example.headxtension.Modele;

import java.security.MessageDigest;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Base64;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import com.example.headxtension.R;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.content.ContentValues.TAG;

public class HeadXtensionDAO {

    private static int version = 1;
    private static SQLiteDatabase accesBD;

    public HeadXtensionDAO(Context ct, String password){
        SQLiteDatabase.loadLibs(ct);
        BdSQLiteOpenHelper bdOpener = new BdSQLiteOpenHelper(ct, ct.getString(R.string.dbName), null, version);
        accesBD = bdOpener.getWritableDatabase(password);
    }


    /**
     * Fonction qui vérifie la base de données existe
     * @return TRUE si la base de données existe sinon FALSE
     */
    public static boolean checkDBExist(Context ct){
        return BdSQLiteOpenHelper.checkDBExist(ct.getString(R.string.dbName), ct);
    }


    public static boolean checkBDOpenable(Context ct, String password){
        boolean exec = false;

        if(checkDBExist(ct)) {
            BdSQLiteOpenHelper bdOpener = new BdSQLiteOpenHelper(ct, ct.getString(R.string.dbName), null, version);

            if(accesBD != null && accesBD.isOpen()){
                accesBD.close();
            } else {
                SQLiteDatabase.loadLibs(ct);
            }

            try{
                bdOpener.getReadableDatabase(password).close();
                exec = true;
            } catch (SQLiteException e){
                Log.e(TAG,e.toString());
            }

        }

        return exec;
    }

    /*--------------------------------   Start Folder   --------------------------------*/

    /**
     * Fonction qui ajoute un dossier dans la base de données
     * @param folder Le dossier à ajouter dans la base de données
     * @return L'ID de la ligne nouvellement insérée, ou -1 si une erreur est survenue
     */
    public long addFolder(Folder folder){
        long ret;

        ContentValues value = new ContentValues();
        value.put("name", folder.getName());
        ret = accesBD.insert("Folder", null, value);

        return ret;
    }

    /**
     * Fonction qui retourne un dossier en passant en paramètre son identifiant
     * @param id L'identifiant du dossier à retourner
     * @return Le dossier souhaité ou NULL si une erreur est survenue
     */
    public Folder getFolder(int id){
        Folder folder = null;
        Cursor curseur;
        curseur = accesBD.rawQuery("SELECT * FROM Folder WHERE id="+id+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            String name = curseur.getString(1);
            ArrayList<Credential> credentialArrayList = getFolderCredentials(id);
            folder = new Folder(id, name, credentialArrayList);
        }
        curseur.close();
        return folder;
    }

    /**
     * Fonction qui retourne la liste des dossiers dont le nom est similaire à la chaîne de caractères passée en paramètre
     * @param name La chaîne de caractère à comparer avec le nom du dossier
     * @return La liste des dossiers dont le nom est similaire à la chaîne de caractères passée en paramètre ou NULL si une erreur est survenue
     */
    public ArrayList<Folder> getFolders(String name){
        Cursor curseur;
        String conditionRequete = "";

        if(name.length()>0) {
            String[] laRequeteEclatee = name.split(" ");
            int i = 0;
            for (String uneChaine : laRequeteEclatee) {

                if (i == 0) {
                    conditionRequete = "WHERE ";
                }

                conditionRequete += "" +
                        "name LIKE '%" + uneChaine + "%' OR ";

                if (i < laRequeteEclatee.length) {
                    conditionRequete += " OR ";
                }
                i++;
            }
        }
        String req = "SELECT * FROM Folder "+conditionRequete+";";
        curseur = accesBD.rawQuery(req,null);
        return cursorToFolderArrayList(curseur);
    }

    /**
     * Fonction qui transforme un curseur (requête à résultat multiple sur la table Folder) en ArrayList de dossiers
     * @param curseur Le résultat d'une requête sur la table Folder
     * @return La liste des dossiers contenus dans le curseur passé en paramètre
     */
    private ArrayList<Folder> cursorToFolderArrayList(Cursor curseur) {
        ArrayList<Folder> foldersList = new ArrayList<>();
        int id;
        String name;
        ArrayList<Credential> credentialArrayList;


        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            id = curseur.getInt(0);
            name = curseur.getString(1);
            credentialArrayList = getFolderCredentials(id);

            foldersList.add(new Folder(id, name, credentialArrayList));
            curseur.moveToNext();
        }

        return foldersList;
    }

    /*---------------------------------   End Folder   ---------------------------------*/




    /*---------------------------------   Start Credential   --------------------------------*/

    /**
     * Fonction qui ajoute des informations de connexion dans la base de données
     * @param credential Les informations de connexion à ajouter
     * @return L'ID de la ligne nouvellement insérée, ou -1 si une erreur est survenue
     */
    public long addCredential(Credential credential){
        long ret;
        int folderId = getCredentialFolder(credential);

        ContentValues value = new ContentValues();
        value.put("name", credential.getName());
        value.put("url", credential.getUrl());
        value.put("username", credential.getUsername());
        value.put("password", credential.getPassword());

        if(folderId != -1) {
            value.put("id_Folder", folderId);
        }

        ret = accesBD.insert("Credential", null, value);

        return ret;
    }

    /**
     * Fonction qui retourne des informations de connexion en passant en paramètre son identifiant
     * @param id L'identifiant des informations de connexion à retourner
     * @return Les informations de connexion souhaitées ou NULL si une erreur est survenue
     */
    public Credential getCredential(int id){
        Credential credential = null;
        Cursor curseur;
        curseur = accesBD.rawQuery("SELECT * FROM Credential WHERE id="+id+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            credential = new Credential(
                    curseur.getInt(0),
                    curseur.getString(1),
                    curseur.getString(2),
                    curseur.getString(3),
                    curseur.getString(4)
            );
        }
        curseur.close();
        return credential;
    }

    /**
     * Fonction qui retourne la liste des informations de connexion dont une des données la composant est similaire à la chaîne de caractères passée en paramètre
     * @param name La chaîne de caractère à comparer avec les données composant les informations de connexion
     * @return La liste des informations de connexion dont au moins une des données la composant est similaire à la chaîne de caractères passée en paramètre ou NULL si une erreur est survenue
     */
    public ArrayList<Credential> getCredentials(String name){
        Cursor curseur;
        String conditionRequete = "";
        if(name.length()>0) {
            String[] laRequeteEclatee = name.split(" ");
            int i = 0;
            for (String uneChaine : laRequeteEclatee) {

                if (i == 0) {
                    conditionRequete = "WHERE ";
                }

                conditionRequete += "" +
                        "name LIKE '%" + uneChaine + "%' OR " +
                        "url LIKE '%" + uneChaine + "%' OR " +
                        "username LIKE '%" + uneChaine + "%'";


                if (i < laRequeteEclatee.length) {
                    conditionRequete += " OR ";
                }
                i++;
            }
        }

        String req = "SELECT * FROM Credential "+conditionRequete+" ;";
        curseur = accesBD.rawQuery(req,null);
        return cursorToCredentialArrayList(curseur);
    }


    /**
     * Fonction qui retourne l'identifiant du dossier d'une information de connexion
     * @param credential Les informations de connexions dont on veut le dossier
     * @return L'identifiant du dossier des informations de connexion souhaitées
     */
    public int getCredentialFolder(Credential credential){
        int folderId = -1;
        Cursor curseur;
        curseur = accesBD.rawQuery("SELECT id_Folder FROM Credential WHERE id="+ credential.getId()+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            folderId = curseur.getInt(0);
        }
        curseur.close();
        return folderId;
    }

    /**
     * Fonction qui retourne la liste des informations de connexion contenues dans un dossier dont l'identiant est passé en paramètre
     * @param folderId L'identifiant du dossier dont on souhaite avoir les informations de connxion qu'il contient
     * @return La liste des informations de connexion contenues dans le dossier souhaité ou NULL si une erreur est survenue
     */
    public ArrayList<Credential> getFolderCredentials(int folderId){
        Cursor curseur;
        curseur = accesBD.rawQuery("SELECT * FROM Credential WHERE id_Folder="+folderId+";",null);
        return cursorToCredentialArrayList(curseur);
    }

    /**
     * Fonction qui transforme un curseur (requête à résultat multiple sur la table Credential) en ArrayList d'informations de connexion
     * @param curseur Le résultat d'une requête sur la table Credential
     * @return La liste des informations de connexion contenues dans le curseur passé en paramètre
     */
    private ArrayList<Credential> cursorToCredentialArrayList(Cursor curseur) {
        ArrayList<Credential> credentialList = new ArrayList<>();
        int id;
        String name, url, username, password;

        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            id = curseur.getInt(0);
            name = curseur.getString(1);
            url = curseur.getString(2);
            username = curseur.getString(3);
            password = curseur.getString(4);

            credentialList.add(
                    new Credential(id,name, url, username, password)
            );

            curseur.moveToNext();
        }

        return credentialList;
    }
    /*---------------------------------   End Credential   ----------------------------------*/


    private static String encrypt(String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encodedValue = c.doFinal(password.getBytes());
        return Base64.encodeToString(encodedValue, Base64.DEFAULT);
    }

    private static String decrypt(String inputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(inputString, Base64.DEFAULT);
        decodedValue = c.doFinal(decodedValue);
        return new String(decodedValue);
    }

    private static SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = password.getBytes("UTF-8");
        digest.update(passwordBytes, 0, passwordBytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }
}