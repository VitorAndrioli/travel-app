package com.traveltrack.vitor.travelapp;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class DatabaseCreator {
    private Context currentContext;

    public DatabaseCreator() { }

    public void createDatabase(Context context) {
        this.currentContext = context;
        this.createCategories();
        try {
            this.createCurrencies();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    private void createCategories() {
        String[] categoryPortNames = {"passagem", "acomodação", "transporte", "alimentação", "laser", "outros"};
        String[] categoryNames = {"passage", "accommodation", "transportation", "alimentation", "leisure", "other"};

        for (int i=0; i< categoryNames.length; i++) {
            String uri = "android.resource://com.traveltrack.vitor.travelapp/drawable/" + categoryNames[i];
            Category category = new Category(categoryNames[i], categoryPortNames[i], uri);
            category.save();
        }
    }

    private void createCurrencies() throws IOException, XmlPullParserException {
        XmlResourceParser xpp = currentContext.getResources().getXml(R.xml.currencies);
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("currency")) {
                String name = xpp.getAttributeValue(null, "name");
                String code = xpp.getAttributeValue(null, "code");
                String symbol = xpp.getAttributeValue(null, "symbol");

                Currency currency = new Currency(name, code, symbol);
                currency.save();
            }
            eventType = xpp.next();
        }
        xpp.close();

    }

}
