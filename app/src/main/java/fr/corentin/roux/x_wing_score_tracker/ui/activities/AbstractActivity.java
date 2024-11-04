package fr.corentin.roux.x_wing_score_tracker.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class AbstractActivity extends AppCompatActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We set the view who will be use for display the datas
        this.initContentView();
        //Init Basic data
        this.initGame();
        //Bind the xml and the fields
        this.findView();
        //Init des datas de la page
        this.initDatas();
        //Init Adapters
        this.initAdapters();
        //Initialization of the listeners
        this.initListeners();
        //Init Default Value Post Listeners
        this.initDefaultValue();
    }

    protected void initDefaultValue() {
    }

    /**
     * Initilization The content view page
     */
    protected abstract void initContentView();

    /**
     * Initialization the basic data (service,game,...) before load the find view
     */
    protected void initGame() {
    }

    /**
     * Load/Map all component from the xml
     */
    protected abstract void findView();

    /**
     * Initialization all the adapters
     */
    protected void initAdapters() {
    }

    /**
     * Initilization Datas inside the component
     */
    protected void initDatas() {
    }

    /**
     * Initialization the listeners of the page
     */
    protected void initListeners() {
    }

}
