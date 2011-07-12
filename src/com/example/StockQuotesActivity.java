package com.examples.StockQuotes;

import com.google.devtools.simple.runtime.components.Component;
import com.google.devtools.simple.runtime.components.HandlesEventDispatching;
import com.google.devtools.simple.runtime.components.android.Button;
import com.google.devtools.simple.runtime.components.android.Form;
import com.google.devtools.simple.runtime.components.android.Label;
import com.google.devtools.simple.runtime.components.android.TextBox;
import com.google.devtools.simple.runtime.components.android.Web;
import com.google.devtools.simple.runtime.events.EventDispatcher;

import android.app.Activity;
import android.os.Bundle;

public class StockQuotesActivity extends Form implements HandlesEventDispatching {
 
	private TextBox stockSymbalTextBox;
	private Button getQuoteBtn;
	private Label valueLabel;
	private Web web1;
	private final static String WEB_PREFIX = "http://finance.yahoo.com/d/quotes.csv?f=l1&s=";
	
	void $define() {
		
		stockSymbalTextBox = new TextBox(this);
		stockSymbalTextBox.Hint("Enter a stock symbol");
		
		getQuoteBtn = new Button(this);
		getQuoteBtn.Text("Get Stock Quote");
		
		valueLabel = new Label(this);
				
		web1 = new Web(this);
		
		EventDispatcher.registerEventForDelegation(this, "Webevent", "GotText");
		EventDispatcher.registerEventForDelegation(this, "Buttonclick", "Click");
		
	}
	
	@Override
	public boolean dispatchEvent(Component component, String id, String eventName, Object[] args) {
		
		if (component.equals(getQuoteBtn) && eventName.equals("Click")) {
			getQuoteWasClicked();
			return true;
		}
		if (component.equals(web1) && eventName.equals("GotText")) {
			gotTextReturned(args[0].toString(), Integer.parseInt(args[1].toString()), args[2].toString(), args[3].toString());
			return true;
		}
		return false;
		
	}

	private void gotTextReturned(String url, int responseCode, String responseType, String responseContent) {
		
		if (responseCode == 200) {
			valueLabel.Text("Current value: "+responseContent);
		} else {
			valueLabel.Text("Error getting stock quote.");
		}
		
	}

	private void getQuoteWasClicked() {
		
		web1.Url(WEB_PREFIX+stockSymbalTextBox.Text());
		web1.Get();
		
	}
	
}