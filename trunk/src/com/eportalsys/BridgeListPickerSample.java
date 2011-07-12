package com.eportalsys.bridge;

import com.google.devtools.simple.runtime.components.Component;
import com.google.devtools.simple.runtime.components.HandlesEventDispatching;
import com.google.devtools.simple.runtime.components.android.*;
import com.google.devtools.simple.runtime.components.android.util.CsvUtil;
import com.google.devtools.simple.runtime.components.util.YailList;
import com.google.devtools.simple.runtime.events.EventDispatcher;

/**
 * Purpose of this class is to demonstrate using AI ListPicker component.
 * Your library should include AI runtime components and kawa library
 */
public class BridgeListPickerSample extends Form implements HandlesEventDispatching {


    private ListPicker listPicker;

    // The equivalent to a "main" method for App Inventor apps is the $define method.
    void $define() {

        HorizontalArrangement ha = new HorizontalArrangement(this);

        //Create new ListPicker
        listPicker = new ListPicker(ha);

        //set some properties on the picker
        listPicker.FontBold(true);
        listPicker.FontSize(16.0f);  //font size is a float
        listPicker.Text("ListPicker Button");

        //To populate picker, we have to use YailList. The YailList is a wrapper around the gnu.list.Pair class
        // used by the Kawa framework. YailList is the main list primitive used by App Inventor components.
        //To use YailList, kawa jar file has to be added to your list of library files
        YailList yailList = null;

        //Right now, for purpose sample, we are just hardcoding items and storing into strCsvElements string
        String strCsvElements = "item1, item2, item3, item4";
        try {
            //Using Util class of CsvUtil, invoke static method of fromCsvRow to convert strCsvElements into YailList
            yailList = CsvUtil.fromCsvRow(strCsvElements);

            //now, set the yailList into picker
            listPicker.Elements(yailList);
        } catch (Exception e) {
            //TODO: update for proper error generation
            e.printStackTrace();
        }

        // Register for events.  By the second argument can be any string.    The third argument must
        // exactly match the name of the event that you want to handle for that component.  When the event
        // happens, dispatchEvent will be called with these arguments.
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "Click");

    }

    // Here is the event dispatcher for our app.  We need to Override the method for the Form
    // superclass
    @Override
    public boolean dispatchEvent(Component component, String id, String eventName,
                                 Object[] args) {
        if (component.equals(listPicker) && eventName.equals("Click")) {
            openPicker();
            return true;

        }  //else if ..   TODO: add as needed

        // else clauses for additional events would go here TODO: add as needed
        return false;

    }

    /**
     * Method to open the listpicker
     */
    private void openPicker() {
        listPicker.Open();
    }

}
