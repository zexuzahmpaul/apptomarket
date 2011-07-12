package com.eportalsys.bridge;

/*
 * An Example of using AppInventor Bridge components.
 * App Inventor Where's My Car example using Java Bridge
 */


import com.google.devtools.simple.runtime.components.Component;
import com.google.devtools.simple.runtime.components.HandlesEventDispatching;
import com.google.devtools.simple.runtime.components.android.*;
import com.google.devtools.simple.runtime.events.EventDispatcher;

/**
 * An Example of using AppInventor Bridge components.
 * App Inventor Where's My Car example using Java Bridge
 * Equivelant to:
 * http://appinventor.googlelabs.com/learn/tutorials/whereismycar/whereismycar.html
 * <p/>
 * Permissions:
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION" />
 *
 * @author M. Hossein Amerkashi
 */
public class WheresMyCar extends Form implements HandlesEventDispatching {

    private Label lblCurrAddr;
    private Label lblCurrGps;
    private Button btnRememberLoc;

    private Label lblParkedAddr;
    private Label lblRememberedGps;
    private Button btnShowDir;

    private LocationSensor locationSensor;
    private TinyDB tinyDB;
    private ActivityStarter activityStarter;


    // The equivalent to a "main" method for App Inventor apps is the $define method.
    void $define() {

        //init Non-visual components
        locationSensor = new LocationSensor(this);
        tinyDB = new TinyDB(this);
        activityStarter = new ActivityStarter(this);

        //Setup properties for our ActivityStarter
        activityStarter.Action("android.intent.action.VIEW");
        activityStarter.ActivityPackage("com.google.android.apps.maps");
        activityStarter.ActivityClass("com.google.android.maps.MapsActivity ");


        //init our visual components
        Label lblGenericLabel;
        lblGenericLabel = new Label(this);
        lblGenericLabel.Text("Your Current Location");
        lblGenericLabel.FontBold(true);

        lblCurrAddr = new Label(this);
        lblCurrAddr.Text("Address: unknown");

        lblCurrGps = new Label(this);
        lblCurrGps.Text("GPS 0, 0");

        btnRememberLoc = new Button(this);
        btnRememberLoc.Text("Remember My Current Location");

        lblGenericLabel.Text("Remember Place");

        lblParkedAddr = new Label(this);
        lblParkedAddr.Text("Address: unknown");

        lblRememberedGps = new Label(this);
        lblRememberedGps.Text("GPS 0, 0");

        btnShowDir = new Button(this);
        btnShowDir.Text("Show Directions from current remembered");


        //Disable buttons till we have an address
        btnShowDir.Enabled(false);
        btnRememberLoc.Enabled(false);

        //invoke method to see if we have already saved directions
        getTinyInitData();

        //Register for event
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "Click");
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "LocationChanged");

    }

    /**
     * Method to check and see if user has already saved the parked address
     */
    private void getTinyInitData() {
        String varTemp = (String) tinyDB.GetValue("address");
        if (varTemp == null || varTemp.equals("") || varTemp.length() == 0) {
            //value has not been set into TinyDb yet. Nothing to do
            return;
        }

        lblParkedAddr.Text(varTemp);

        //Now get the lat / lon and concatnate with comma, then display in the text
        varTemp = tinyDB.GetValue("lat") + ", " + tinyDB.GetValue("lon");
        lblRememberedGps.Text(varTemp);

        //since we now have address, we can enable our Directions button
        btnShowDir.Enabled(true);

    }

    // Here is the event dispatcher for our app.  We need to Override the method for the Form
    // superclass
    @Override
    public boolean dispatchEvent(Component component, String id, String eventName, Object[] args) {

        //Trap the events
        if (component.equals(btnRememberLoc) && eventName.equals("Click")) {
            saveGpsLocation();
            return true;

        } else if (component.equals(btnShowDir) && eventName.equals("Click")) {
            showDirections();
            return true;

        } else if (eventName.equals("LocationChanged")) {
            getGpsLocation((String) args[0], (String) args[0], (String) args[0]);
            return true;
        }


        return false;
    }

    /**
     * Method to show directions
     */
    private void showDirections() {

        //Set up our URL
        StringBuffer st;
        st = new StringBuffer("");
        st.append("http://maps.google.com/maps/?saddr=")
                .append(lblCurrGps.Text())
                .append("&daddr=")
                .append(lblRememberedGps.Text())
        ;

        //Startup the activity
        activityStarter.DataUri(st.toString());
    }

    /**
     * This method is invoked whenever the GPS location is changed
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param alt Altitude
     */
    private void getGpsLocation(String lat, String lon, String alt) {
        lblCurrAddr.Text(locationSensor.CurrentAddress());
        lblCurrGps.Text(lat + "," + lon);

        //Since we now have a location, enable the button
        btnRememberLoc.Enabled(true);
    }

    /**
     * Method to save current GPS location into our tinydb
     */
    private void saveGpsLocation() {
        //Display current address using location sensor
        lblCurrAddr.Text(locationSensor.CurrentAddress());
        lblCurrGps.Text(locationSensor.Latitude() + "," + locationSensor.Longitude());

        //now store into our tinydb
        tinyDB.StoreValue("address", locationSensor.CurrentAddress());
        tinyDB.StoreValue("lat", locationSensor.Latitude());
        tinyDB.StoreValue("lon", locationSensor.Longitude());

        //since we now have address, we can enable our Directions button
        btnShowDir.Enabled(true);

    }
}
