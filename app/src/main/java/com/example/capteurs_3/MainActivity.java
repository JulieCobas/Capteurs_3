package com.example.capteurs_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /****************************** ACTIVATION DU LOGCAT *********************************************************************************************************/
    /** Activer le TAG*/
    private final static String TAG = "sensorList_MainActivity"; // Avec le TAG par défaut sensorList_MainActivity

    /****************************** DECLARATION DES ELEMENTS DE L'INTERFACE***************************************************************************************/
    /** Déclaration des éléments de l'interface*/
    private ListView listViewCapteur1; //Permet d'avoir la liste des capteurs

    /** DECLARATION DES VARIABLES QUI VONT ÊTRE UTILISEES PLUSIEURS FOIS ****************************************************************************************/
    private SensorManager sensorManager;
    private ArrayAdapter adapter;
    private List<Sensor> listSensor;
    private ArrayList<String> listSensorNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /** Récupération de l'id du champ ListView de l'interface */
        listViewCapteur1 = findViewById(R.id.ListCapteurs); //Récupère le champ ListCapteurs par son id dans le layout


        /****************************** ACTIVATION SENSOR MANAGER + LISTE DES SENSORS TROUVES **********************************************/

        /** Initialisation de SensorManager pour accéder aux services de capteurs de l'appareil. */
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        /** Création d'une liste contenant tous les types de capteurs (physiques et virtuels) disponibles sur l'appareil. */
        listSensor = sensorManager.getSensorList(Sensor.TYPE_ALL); //Sensor.TYPE_ALL est un argument qui inclus tous les types de capteurs physiques (accélérometre, gyroscope) et virtuels (luminosité ou de proximité)

        /****************************** AFFICHAGE DANS LE LOGCAT ***************************************************************************/

        /** Affichage du nombre de capteurs actifs */
        Log.i(TAG, "Nombre de Sensor : " + listSensor.size()); // Aller dans le logcat et taper dans le filtre tag:listSensor

        /** Affichage de l'ensemble des capteurs de la liste si elle n'est pas vide */
        if (listSensor != null ){ //Vérifier si la liste des capteurs n'est pas vide pour éviter une NullPointerException
            for ( Sensor nextSensor : listSensor){ // Parcourir chaque capteur dans la liste des capteurs
                // "nextSensor" variable temporaire de type Sensor framework un capteur individuel de la liste à chaque itération
                Log.i(TAG, "ID Type de capteurs : " + nextSensor.getType()); //Affiche l'id numérique du type de capteur trouvé
                Log.i(TAG, "Nom du capteurs : " + nextSensor.getName()); //Affiche le nom du capteurs
                //Informations optionnelles importantes
                // Log.i(TAG, "Fabricant du capteur : " + nextSensor.getVendor()); //Affiche le nom du fabricant
                // Log.i(TAG, "Résolution du capteur : " + nextSensor.getResolution()); //Afficher la plus petite variation que le capteur peut détecter/précision élevée
            }
        }

        /****************************** AFFICHAGE LAYOUT - CREATION LISTE + ADAPTATEUR  ****************************************************/

        /** LISTE : Création d'une liste de l'ensemble des noms des capteurs actifs */
        listSensorNames = new ArrayList<>(); //Création de la liste de capteurs de type string
        for (Sensor capteur : listSensor){ //Parcourir les capteurs en fonction de la liste
            listSensorNames.add(capteur.getName()); //Ajouter dans la liste les noms des capteurs
        }

        /**  ADAPTATEUR : Création d'un ArrayAdapter pour gérer l'affichage des noms des capteurs dans une listeView pour un interface utilisateur .*/
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSensorNames); //ArrayAdapteur sert de pont entre une ListView et les données à afficher
        //this -> Fait référence à l'instance de la classe actuelle utilisée comme contexte, soit MainActivity
        //android.R.layout.simple_list_item_1 -> Ressource de layout fournie par Android pour afficher une liste de texte simple
        listViewCapteur1.setAdapter(adapter); // Ajout/Modification de l'adaptateur à l'id du champ ListView pour afficher les noms des capteurs dans l'interface utilisateur.


        /****************************** AFFICHAGE LAYOUT 2 - ONCLICKLISTENER & INTENT  *******************************************************/

        /** Associer un Listener à la ListView pour générer un intent pour chaque éléments de la liste */
       listViewCapteur1.setOnItemClickListener( //Associer un écouteur aux éléments de la liste pour gérer les clics sur chaque capteur.

                new AdapterView.OnItemClickListener() { // Création d'une nouvelle instance d'OnItemClickListener

                    /** Création d'une méthode déclanchée lorsqu'on presse sur un élément de la liste */
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // Réagit au clic sur un élément de la liste en fonction de sa position.
                        //AdapaterView<?> parent -> Vue de ListView à laquelle l'adaptateur est attaché.
                        //View view -> Apparence visuele spécifique de l'élément de la liste qui a été cliqué.
                        //int position -> L'index de l'élément cliqué dans la liste.
                        //long id -> Identifiant unique associé à l'élément de la liste

                        /** Création d'une intention pour démarrer une nouvelle activité vers un autre écran -> SensorDetailsActivity */
                        Intent intent1 = new Intent(MainActivity.this, SensorDetailsActivity.class);  //Intent qui passe de MainActivity à SensorDetailsActivity

                        /** Passage dans l'écran SensorDetailsActivity la position*/
                        intent1.putExtra("position", position);

                        /** Démarrer l'activité SensorDetailsActivity */
                        startActivity(intent1);
                    }
                }
       );
    }
}