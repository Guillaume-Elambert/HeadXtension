package com.example.headxtension.Modele;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HeadXtensionDAO {

    private static String base = "BDD_GSBParam.db";
    private static int version = 1;
    private BdSQLiteOpenHelper accesBD;

    public HeadXtensionDAO(Context ct){
        accesBD = new BdSQLiteOpenHelper(ct, base, null, version);
    }

    /*--------------------------------   Start Folder   --------------------------------*/

    /**
     * Fonction qui ajoute un dossier dans la base de données
     * @param folder Le dossier à ajouter dans la base de données
     * @return L'ID de la ligne nouvellement insérée, ou -1 si une erreur est survenue
     */
    public long addFolder(Folder folder){
        long ret;
        SQLiteDatabase bd = accesBD.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name", folder.getName());
        ret = bd.insert("Folder", null, value);

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
        curseur = accesBD.getReadableDatabase().rawQuery("SELECT * FROM Folder WHERE id="+id+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            String name = curseur.getString(1);
            ArrayList<Credentials> credentialsArrayList = getFolderCredentials(id);
            folder = new Folder(id, name, credentialsArrayList);
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
        curseur = accesBD.getReadableDatabase().rawQuery(req,null);
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
        ArrayList<Credentials> credentialsArrayList;


        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            id = curseur.getInt(0);
            name = curseur.getString(1);
            credentialsArrayList = getFolderCredentials(id);

            foldersList.add(new Folder(id, name, credentialsArrayList));
            curseur.moveToNext();
        }

        return foldersList;
    }

    /*---------------------------------   End Folder   ---------------------------------*/




    /*---------------------------------   Start Credentials   --------------------------------*/

    /**
     * Fonction qui ajoute des informations de connexion dans la base de données
     * @param credentials Les informations de connexion à ajouter
     * @return L'ID de la ligne nouvellement insérée, ou -1 si une erreur est survenue
     */
    public long addCredentials(Credentials credentials){
        SQLiteDatabase bd = accesBD.getWritableDatabase();
        long ret;
        int folderId = getCredentialsFolder(credentials);

        ContentValues value = new ContentValues();
        value.put("name", credentials.getName());
        value.put("url", credentials.getUrl());
        value.put("username", credentials.getUsername());
        value.put("password", credentials.getPassword());

        if(folderId != -1) {
            value.put("id_Folder", folderId);
        }

        ret = bd.insert("Credentials", null, value);

        return ret;
    }

    /**
     * Fonction qui retourne des informations de connexion en passant en paramètre son identifiant
     * @param id L'identifiant des informations de connexion à retourner
     * @return Les informations de connexion souhaitées ou NULL si une erreur est survenue
     */
    public Credentials getCredentials(int id){
        Credentials credentials = null;
        Cursor curseur;
        curseur = accesBD.getReadableDatabase().rawQuery("SELECT * FROM Credentials WHERE id="+id+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            credentials = new Credentials(
                    curseur.getInt(0),
                    curseur.getString(1),
                    curseur.getString(2),
                    curseur.getString(3),
                    curseur.getString(4)
            );
        }
        curseur.close();
        return credentials;
    }

    /**
     * Fonction qui retourne la liste des informations de connexion dont une des données la composant est similaire à la chaîne de caractères passée en paramètre
     * @param name La chaîne de caractère à comparer avec les données composant les informations de connexion
     * @return La liste des informations de connexion dont au moins une des données la composant est similaire à la chaîne de caractères passée en paramètre ou NULL si une erreur est survenue
     */
    public ArrayList<Credentials> getCredentials(String name){
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

        String req = "SELECT * FROM Credentials "+conditionRequete+" ;";
        curseur = accesBD.getReadableDatabase().rawQuery(req,null);
        return cursorToCredentialsArrayList(curseur);
    }


    /**
     * Fonction qui retourne l'identifiant du dossier d'une information de connexion
     * @param credentials Les informations de connexions dont on veut le dossier
     * @return L'identifiant du dossier des informations de connexion souhaitées
     */
    public int getCredentialsFolder(Credentials credentials){
        int folderId = -1;
        Cursor curseur;
        curseur = accesBD.getReadableDatabase().rawQuery("SELECT id_Folder FROM Credentials WHERE id="+credentials.getId()+";",null);
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
    public ArrayList<Credentials> getFolderCredentials(int folderId){
        Cursor curseur;
        curseur = accesBD.getReadableDatabase().rawQuery("SELECT * FROM Credentials WHERE id_Folder="+folderId+";",null);
        return cursorToCredentialsArrayList(curseur);
    }

    /**
     * Fonction qui transforme un curseur (requête à résultat multiple sur la table Credentials) en ArrayList d'informations de connexion
     * @param curseur Le résultat d'une requête sur la table Credentials
     * @return La liste des informations de connexion contenues dans le curseur passé en paramètre
     */
    private ArrayList<Credentials> cursorToCredentialsArrayList(Cursor curseur) {
        ArrayList<Credentials> credentialsList = new ArrayList<>();
        int id;
        String name, url, username, password;

        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            id = curseur.getInt(0);
            name = curseur.getString(1);
            url = curseur.getString(2);
            username = curseur.getString(3);
            password = curseur.getString(4);

            credentialsList.add(
                    new Credentials(id,name, url, username, password)
            );

            curseur.moveToNext();
        }

        return credentialsList;
    }
    /*---------------------------------   End Credentials   ----------------------------------*/

}