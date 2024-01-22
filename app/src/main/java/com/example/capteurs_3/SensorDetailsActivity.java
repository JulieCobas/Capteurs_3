package com.example.capteurs_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SensorDetailsActivity extends AppCompatActivity {

    /****************************** ACTIVATION DU LOGCAT *********************************************************************************************************/
    /**
     * Activer le TAG
     */
    private final static String TAG = "sensorList_SensorDetailsActivity"; // Avec le TAG par défaut sensorList_MainActivity

    /****************************** DECLARATION DES ELEMENTS DE L'INTERFACE***************************************************************************************/
    /**
     * Déclaration des éléments de l'interface
     */
    private ListView listViewCapteur2; //Permet d'avoir la liste des capteurs

    /**
     * DECLARATION DES VARIABLES QUI VONT ÊTRE UTILISEES PLUSIEURS FOIS
     ****************************************************************************************/
    private SensorManager sensorManager;
    private ArrayAdapter adapter;
    //private List<Sensor> listSensor;
    private Sensor selectedSensor;
    private ArrayList<String> listSensorNames2;
    private SensorEventListener mySensorEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_details);


        /** Récupération de l'id du champ ListView de l'interface */
        listViewCapteur2 = findViewById(R.id.ListCapteurs2); //Récupère le champ ListCapteurs par son id dans le layout


        /****************************** ACTIVATION SENSOR MANAGER + LISTE DES SENSORS TROUVES **********************************************/

        /** Initialisation de SensorManager pour accéder aux services de capteurs de l'appareil. */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        /****************************** AFFICHAGE LAYOUT - CREATION LISTE + ADAPTATEUR  ****************************************************/

        /** POSITION : Récupère la position du capteur sélectionné transmise par l'activité précédente */
        int position = getIntent().getIntExtra("position", 0);

        /** CAPTEUR SELECTIONNE : Sélectionne le capteur en fonction de la position reçue. */
        selectedSensor = sensorManager.getSensorList(Sensor.TYPE_ALL).get(position); //Sensor.TYPE_ALL est un argument qui inclus tous les types de capteurs physiques (accélérometre, gyroscope) et virtuels (luminosité ou de proximité)

        /** LISTE : Création d'une liste de l'ensemble des noms des capteurs actifs */
        listSensorNames2 = new ArrayList<>(); //Création de la liste de capteurs de type string

        /**  ADAPTATEUR : Création d'un ArrayAdapter pour gérer l'affichage des noms des capteurs dans une listeView pour un interface utilisateur .*/
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSensorNames2); //ArrayAdapteur sert de pont entre une ListView et les données à afficher
        //this -> Fait référence à l'instance de la classe actuelle utilisée comme contexte, soit MainActivity
        //android.R.layout.simple_list_item_1 -> Ressource de layout fournie par Android pour afficher une liste de texte simple
        listViewCapteur2.setAdapter(adapter); // Ajout/Modification de l'adaptateur à l'id du champ ListView pour afficher les noms des capteurs dans l'interface utilisateur.

        /****************************** SENSOR EVENT LISTENER - ECOUTER LES EVENEMENTS  ****************************************************/

        /** Création d'une instance de SensorEventListener pour écouter les évènements en continue des capteurs */
        mySensorEvent = new SensorEventListener() {

            /** Surveiller les valeurs du capteur en continue*/
            @Override
            public void onSensorChanged(SensorEvent event) { // Cette méthode est appelée chaque fois qu'une nouvelle donnée de capteur est disponible.
                updateSensorlistViewCapteur2(event);  // Mise à jour de la liste des informations du capteur.
            }

            /** Permet de surveiller les changements avec une précision supplémentaire */
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        // Enregistrement du SensorEventListener pour le capteur sélectionné avec un délai standard.
        sensorManager.registerListener(mySensorEvent, selectedSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void updateSensorlistViewCapteur2(SensorEvent event) {

        // Efface les données précédentes de la liste pour éviter la surchage
        listSensorNames2.clear();

        // Ajoutez les informations du capteur à la liste
        listSensorNames2.add("Sensor type : " + event.sensor.getType());
        listSensorNames2.add("Sensor Name : " + event.sensor.getName());
        listSensorNames2.add("Sensor vendor : " + event.sensor.getVendor());

        // Boucle pour ajouter toutes les valeurs actuelles du capteur à la liste
        for (int i = 0; i < event.values.length; i++) {
            listSensorNames2.add("Value " + (i + 1) + " : " + event.values[i]);
        }
        // Informe l'adaptateur que les données ont changé pour rafraîchir l'affichage du ListView.
        adapter.notifyDataSetChanged();

    }
/****************************** TRANSITION - CYCLE DE VIE DES ACTIVITES ************************************************************/

    /**
     * Désenregistre l'écouteur du capteur pour prévenir des fuites de mémoire lors de la destruction de l'activité
     */
    @Override
    protected void onDestroy() {
        // Assure le désenregistrement de l'écouteur de capteur pour éviter les fuites de mémoire.
        if (sensorManager != null && mySensorEvent != null) {
            sensorManager.unregisterListener(mySensorEvent);
        }

        super.onDestroy();
    }


    /**
     * Désenregistre l'écouteur de capteur pour économiser les ressources lorsque l'activité n'est plus visible.
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(mySensorEvent);
        super.onPause();
    }

    /**
     * Réenregistre l'écouteur de capteur quand l'activité redevient active.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(mySensorEvent, selectedSensor, SensorManager.SENSOR_DELAY_UI);

    }
}

