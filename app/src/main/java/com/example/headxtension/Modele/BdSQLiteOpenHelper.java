package com.example.headxtension.Modele;

import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class BdSQLiteOpenHelper extends SQLiteOpenHelper  {

    private Context mContext;


    BdSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArrayList<String> listeFichiers;

        try {
            listeFichiers = getFichiersAssets("DB", new ArrayList<String>());

            if(listeFichiers.size()>0) {
                byte[] buffer;
                String laRequete;

                for (String unFichier : listeFichiers) {
                    try {
                        InputStream is = this.mContext.getAssets().open(unFichier);
                        buffer = new byte[is.available()];
                        is.read(buffer);
                        is.close();
                        laRequete = new String(buffer);
                        db.execSQL(laRequete);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Rien
    }

    /**
     * Parcours un dossier et ses sous dossier
     * Renvoie un ArrayList contenant l'ensemble des chemins des fichiers qui composent ce dossier
     * @param dossier La chaîne de caractère correspondant au dossier à parcourir
     * @param listeFichiers La liste des chemins des fichiers parcourus depuis le dossier définit au 1er appel
     * @return La liste des chemins des fichiers qui composent ce dossier
     * @throws Exception Impossible de lire le fichier
     */
    private ArrayList<String> getFichiersAssets(String dossier, ArrayList<String> listeFichiers) throws Exception {
        String[] listeDesFichiers = this.mContext.getAssets().list(dossier);
        if(listeDesFichiers != null && listeDesFichiers.length > 0){
            for(String unFichier : listeDesFichiers){
                getFichiersAssets(dossier+"/"+unFichier, listeFichiers);
            }
        } else {
            listeFichiers.add(dossier);
        }
        return listeFichiers;
    }

    /**
     * Fonction qui vérifie si une base de données existe
     * @param name Nom de la base de données
     * @return TRUE si la base de données existe sinon FALSE
     */
    static boolean checkDBExist(String name, Context context){
        return context.getDatabasePath(name).exists();
    }


}