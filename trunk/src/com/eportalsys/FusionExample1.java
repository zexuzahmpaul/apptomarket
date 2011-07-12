package com.eportalsys.bridge;

/**
 * An Example of using AppInventor Bridge components.
 * This example demos how to use fusion component
 *
 * Permissions:
 * <uses-permission android:name="android.permission.USE_CREDENTIALS" />
    <uses-permission android:name="android.permission.ACCOUNT_MANAGER" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.MANAGE_ACCOUNTS" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
 *
 * @author: M. Hossein Amerkashi
 *
 */

import com.google.devtools.simple.runtime.components.Component;
import com.google.devtools.simple.runtime.components.HandlesEventDispatching;
import com.google.devtools.simple.runtime.components.android.*;
import com.google.devtools.simple.runtime.events.EventDispatcher;

public class FusionExample1 extends Form implements HandlesEventDispatching {

    private FusiontablesControl fusiontablesControl;
    private TextBox txtQuery;
    Button btnSubmitQuery;

    private TextBox txtFusionResult;


    // The equivalent to a "main" method for App Inventor apps is the $define method.
    void $define() {
        fusiontablesControl = new FusiontablesControl(this);

        //Label for displaying info
        Label label = new Label(this);
        label.Text("Enter Fusion Query below");

        //Textbox for getting SQL query statement
        txtQuery = new TextBox(this);
        txtQuery.Width(333);
        txtQuery.Hint("Enter SQL Query Statement");

        //For testing, just enter some valid fusion sql query below
        txtQuery.Text("Select category from 906164");

        btnSubmitQuery = new Button(this);
        btnSubmitQuery.Text("Submit");

        //Info label
        label = new Label(this);
        label.Text("\nFusion Result:");

        //Fusion results will be displayed in this textobox
        txtFusionResult = new TextBox(this);
        txtFusionResult.Width(333);
        txtFusionResult.Height(333);
        txtFusionResult.Hint("Fusion Result");


        //Register for event
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "Click");
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "GotResult");

    }

    // Here is the event dispatcher for our app.  We need to Override the method for the Form
    // superclass
    @Override
    public boolean dispatchEvent(Component component, String id, String eventName,
                                 Object[] args) {

        //Trap the event
        if (component.equals(btnSubmitQuery) && eventName.equals("Click")) {
            submitQuery();
            return true;
        } else if (eventName.equals("GotResult")) {
            processFusionResult((String) args[0]);
            return true;
        }


        return false;
    }

    /**
     * Method to invoke submit query against FusionTable
     */
    private void submitQuery() {

        fusiontablesControl.Query(txtQuery.Text());
        fusiontablesControl.DoQuery();
    }

    /**
     * This method is invoked when we receive the fusion result
     */
    private void processFusionResult(String fusionResult) {
        txtFusionResult.Text(fusionResult);
    }
}
