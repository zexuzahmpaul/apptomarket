package com.eportalsys.bridge;

/**
 * An Example of using AppInventor Bridge components.
 * This is equivalent of AI TextGroup
 * http://appinventor.googlelabs.com/learn/tutorials/textgroup/textgroup.html
 *
 * Permissions: <uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
 *
 * @author: M. Hossein Amerkashi
 *
 */

import com.google.devtools.simple.runtime.components.Component;
import com.google.devtools.simple.runtime.components.HandlesEventDispatching;
import com.google.devtools.simple.runtime.components.android.*;
import com.google.devtools.simple.runtime.events.EventDispatcher;

import java.util.StringTokenizer;


public class TextGroup extends Form implements HandlesEventDispatching {

    private Button btnStartTexting;
    private Texting texting;
    private TextBox txtCsvNumbers;
    private TextBox txtMessage;

    private Label lblStatus;
    private Label lblTotPhoneNumbers;


    // The equivalent to a "main" method for App Inventor apps is the $define method.
    void $define() {

        //Label for displaying info
        Label label = new Label(this);
        label.Text("Enter CSV list of numbers to text below");

        //Textbox for getting phone numbers in csv format
        txtCsvNumbers = new TextBox(this);
        txtCsvNumbers.Width(333);
        txtCsvNumbers.Hint("Enter Group of People in CSV");

        //Info label
        label = new Label(this);
        label.Text("Enter Message below");

        //Texbox for getting user's message
        txtMessage = new TextBox(this);
        txtMessage.Width(333);
        txtMessage.Hint("Message to Text");

        //Label to display total number of phone numbers
        lblTotPhoneNumbers = new Label(this);
        lblTotPhoneNumbers.Text("Total Numbers: 0");

        //Button to start the texting
        btnStartTexting = new Button(this);
        btnStartTexting.Text("Start Texting");

        //Label for displaying status
        lblStatus = new Label(this);
        lblStatus.Text("Status will be printed here");

        //And here is the texting component
        texting = new Texting(this);

        //Register for event
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "Click");

    }

    // Here is the event dispatcher for our app.  We need to Override the method for the Form
    // superclass
    @Override
    public boolean dispatchEvent(Component component, String id, String eventName,
                                 Object[] args) {

        //Trap the event
        if (component.equals(btnStartTexting) && eventName.equals("Click")) {
            startTexting();
            return true;
        }

        return false;
    }

    /**
     * Starts texting
     */
    private void startTexting() {

        //Split the entered numbers using comma delimeter
        StringTokenizer st = new StringTokenizer(txtCsvNumbers.Text(), ",");
        lblTotPhoneNumbers.Text("Total Numbers: " + st.countTokens());

        //set the message
        texting.Message(txtMessage.Text());

        //loop through each number and send message
        while (st.hasMoreTokens()) {
            texting.PhoneNumber(st.nextToken());
            texting.SendMessage();
        }
        lblStatus.Text("Sent " + st.countTokens() + " messages");
    }
}
