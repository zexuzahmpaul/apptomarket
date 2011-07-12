package com.eportalsys.bridge;

/**
 * An Example of using AppInventor Bridge components. This is equivalent of AI PaintPot
 * http://appinventor.googlelabs.com/learn/tutorials/paintpot/paintpot-part1.html
 * and
 * http://appinventor.googlelabs.com/learn/tutorials/paintpot/paintpot-part2.html
 *
 * @author: M. Hossein Amerkashi
 *
 */

import android.graphics.Color;
import com.google.devtools.simple.runtime.components.android.Button;
import com.google.devtools.simple.runtime.components.Component;
import com.google.devtools.simple.runtime.components.HandlesEventDispatching;
import com.google.devtools.simple.runtime.components.android.Canvas;
import com.google.devtools.simple.runtime.components.android.Form;
import com.google.devtools.simple.runtime.components.android.HorizontalArrangement;
import com.google.devtools.simple.runtime.components.android.Label;
import com.google.devtools.simple.runtime.events.EventDispatcher;

import java.util.Random;


public class PaintPot extends Form implements HandlesEventDispatching {

    private Canvas myCanvas;
    private Label lblStatus;
    private Button btnRed;
    private Button btnBlue;
    private Button btnGreen;

    private Button btnWipe;


    //Following is used to for displaying number of touches
    int numTouches;

    // The equivalent to a "main" method for App Inventor apps is the $define method.
    void $define() {

        //We are going to place the color buttons in a HorizontalArrangement
        HorizontalArrangement hr = new HorizontalArrangement(this);
        btnRed = new Button(hr);
        btnBlue = new Button(hr);
        btnGreen = new Button(hr);

        //set their color and text
        btnRed.BackgroundColor(Color.RED);
        btnBlue.BackgroundColor(Color.BLUE);
        btnGreen.BackgroundColor(Color.GREEN);

        btnRed.Text("Red");
        btnBlue.Text("Blue");
        btnGreen.Text("Green");

        //canvas into its own HorizontalArrangement
        hr = new HorizontalArrangement(this);
        myCanvas = new Canvas(hr);
        myCanvas.Width(400);
        myCanvas.Height(400);
        myCanvas.LineWidth(10);

        //Wipe and a label into its own HorizontalArrangement
        hr = new HorizontalArrangement(this);
        btnWipe = new Button(hr);
        btnWipe.Text("Wipe");

        lblStatus = new Label(hr);
        lblStatus.Text("  touchX/touchY:");

        // Register for events.  By the second argument can be any string.    The third argument must
        // exactly match the name of the event that you want to handle for that component.  When the event
        // happens, dispatchEvent will be called with these arguments.
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "Touched");
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "Click");
        EventDispatcher.registerEventForDelegation(this, "AppInventorInJava", "Dragged");
    }

    // Here is the event dispatcher for our app.  We need to Override the method for the Form
    // superclass
    @Override
    public boolean dispatchEvent(Component component, String id, String eventName,
                                 Object[] args) {

        if (component.equals(myCanvas) && eventName.equals("Touched")) {
            canvasTouced(((Float) args[0]).intValue(), ((Float) args[1]).intValue());
            return true;

        } else if (component.equals(myCanvas) && eventName.equals("Dragged")) {
            drawLine(((Float) args[2]).intValue(),
                    ((Float) args[3]).intValue(),
                    ((Float) args[4]).intValue(),
                    ((Float) args[5]).intValue());
            return true;

        } else if (component.equals(btnBlue) && eventName.equals("Click")) {
            myCanvas.PaintColor(COLOR_BLUE);
            return true;

        } else if (component.equals(btnGreen) && eventName.equals("Click")) {
            myCanvas.PaintColor(COLOR_GREEN);
            return true;

        } else if (component.equals(btnRed) && eventName.equals("Click")) {
            myCanvas.PaintColor(COLOR_RED);
            return true;

        } else if (component.equals(btnWipe) && eventName.equals("Click")) {
            myCanvas.Clear();
            return true;
        }

        return false;
    }


    /**
     * This method will get the touched touchX, touchY coordinates and will then create a circle
     * of random radius (between 1 to 33) with the color that was selected (RED, BLUE or GREEN).
     * It will also display the touched touchX,touchY coordinates.
     * @param x current x
     * @param y current y
     */
    private void canvasTouced(int x, int y) {

        myCanvas.DrawCircle(x, y, new Random().nextInt(33));
        lblStatus.Text("  touchX/touchY:" + x + "/" + y + " touches: " + ++numTouches);

    }

    /**
     * Method to draw line
     * @param prevX last touch x
     * @param prevY last touch y
     * @param touchX current x
     * @param touchY current y
     */
    private void drawLine(int prevX, int prevY, int touchX, int touchY) {
        myCanvas.DrawLine(prevX, prevY, touchX, touchY);
    }

}
