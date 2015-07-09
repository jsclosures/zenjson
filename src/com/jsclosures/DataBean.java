package com.jsclosures;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.commons.collections.iterators.IteratorEnumeration;

/**
A generalized bean to create a set of attributes, sub objects and collections of objects.

All attributes, sub structures, and collection keys are "case-insensitive".

Usage:

Constructors:

DataBean rec = new DataBean();
or
DataBean rec = new DataBean(String name,String value);   //provides a "name and value" pair.

DataBean rec = new DataBean();

Adding "Attributes"

rec.setValue("id","1");
rec.setValue("name","my name");
rec.setValue("price","100.00");
rec.setValue("active","true");

Adding "Attributes by type"

rec.setValue("id",1);
rec.setValue("price",100.00);
rec.setValue("active",true);

Getting "Attributes"

String id = rec.getString("id");
String name = rec.getString("name");

Safe "Type Transformations"

int id = rec.getInt("id");
boolean active = rec.getBoolean("active");
float price = rec.getFloat("price");

Testing "Attributes"

boolean result = rec.isValid("name");

 *This is short hand for testing the attribute exists and its value is not null.

Adding "Structures"

DataBean subRec = new DataBean();
rec.setStructure("mysubrec",subRec);

Getting "Structures"

DataBean subRec = rec.getStructure("mysubrec");


 Adding "Object"

 Object subObj = new Object();
 rec.setObject("mysubobj",subObj);

 Getting "Object"

 Object subObj = rec.getObject("mysubobj");

 Adding "Collection"

 ArrayList subArray = new ArrayList();
 rec.setCollection("mysubarray",subArray);

 Auto Create collection

 rec.addToCollection("mysubarray",new DataBean());

 Getting "Collection"

 ArrayList subArray = rec.getCollection("mysubarray");

 */

public class DataBean extends Object {
    /**
     * Create a "sparse" object that consumes resources once information is added to it.
     */
    public DataBean() {
        super();
    }

    /**
     * Create a generic "name value" pair.
     * @param name
     * @param value
     */
    public DataBean(String name, String value) {
        super();
        setValue(name, value);
    }
    /**
     * Create a generic set of "name value" pairs.
     * @param name1
     * @param value1
     * @param name2
     * @param value2
     */
    public DataBean(String name1, String value1,String name2,String value2) {
        super();
        setValue(name1, value1);
        setValue(name2, value2);
    }

    private HashMap structures;

    /**
     * Set the structure by name (case-insensitive).
     * @param name
     * @param value
     */
    public void setStructure(String name, DataBean value) {
        if (structures == null)
            structures = new HashMap();

        structures.put(name.toLowerCase(), value);
    }

    /**
     * Get the structure by name (case-insensitive)
     * @param name
     * @return
     */
    public DataBean getStructure(String name) {
        return (structures != null && structures.get(name.toLowerCase()) != null ?
                (DataBean) structures.get(name.toLowerCase()) : new DataBean());
    }

    private HashMap objects;

    /**
     * Set the object by name (case-insensitive).
     * @param name
     * @param value
     */
    public void setObject(String name, Object value) {
        if (objects == null)
            objects = new HashMap();

        objects.put(name.toLowerCase(), value);
    }

    /**
     * Get the object by name (case-insensitive)
     * @param name
     * @return
     */
    public Object getObject(String name) {
        return (objects != null && objects.get(name.toLowerCase()) != null ? (Object) objects.get(name.toLowerCase()) :
                null);
    }

    private HashMap collections;

    /**
     * Set the collection by name (case-insensitive).
     * @param name
     * @param value
     */
    public void setCollection(String name, ArrayList value) {
        if (collections == null)
            collections = new HashMap();

        collections.put(name.toLowerCase(), value);
    }

    public ArrayList getCollection(String name) {
        return (collections != null && collections.get(name.toLowerCase()) != null ?
                (ArrayList) collections.get(name.toLowerCase()) : null);
    }

    /**
     * Create the collection by name (case-insensitive) if it does not exist and add the object to the collection.
     * @param name
     * @param value
     */
    public void addToCollection(String name, Object value) {
        ArrayList entryList = getCollection(name);

        if (entryList == null) {
            setCollection(name, new ArrayList());
            entryList = getCollection(name);
        }

        entryList.add(value);
    }

    /**
     * Get the collections internal HashMap.
     * @return
     */
    public HashMap getCollections() {
        return (collections);
    }

    /**
     * Get the structures internal HashMap.
     * @return
     */
    public HashMap getStructures() {
        return (structures);
    }

    /**
     * Get all attribute names
     * @return
     */
    public Enumeration getColumnNames() {
        Enumeration result = null;

        if (internalValues != null)
            result = new IteratorEnumeration(internalValues.keySet().iterator());

        return (result);
    }

    private HashMap internalValues;

    /**
     * Set the value by name (case-insensitive).
     * @param name
     * @param value
     */
    public DataBean setValue(String name, Object value) {
        if (internalValues == null)
            internalValues = new HashMap();

        //if( value != null )
        internalValues.put(name.toLowerCase(), value);
        
        return( this );
    }

    /**
     *
     * @param name
     * @param value
     */
    public DataBean append(String name, String value) {
        setValue(name, getString(name).length() > 0 ? getString(name) + " " + value : value);
        
        return( this );
    }

    /**
     * Append the value by name (case-insensitive). Assumes attributes is a string. If smart is true then "unique" within the string is ensured.
     * @param name
     * @param value
     */
    public DataBean append(String name, String value, boolean smart) {
        if (smart) {
            if (getString(name).indexOf(value) < 0)
                append(name, value);
        } else
            append(name, value);
        
        return( this );
    }

    /**
     * Set the value by name (case-insensitive).
     * @param name
     * @param value
     */
    public DataBean setValue(String name, int value) {
        setValue(name, String.valueOf(value));
        
        return( this );
    }

    /**
     * Set the value by name (case-insensitive).
     * @param name
     * @param value
     */
    public DataBean setValue(String name, float value) {
        setValue(name, String.valueOf(value));
        
        return( this );
    }

    /**
     * Set the value by name (case-insensitive).
     * @param name
     * @param value
     */
    public DataBean setValue(String name, boolean value) {
        setValue(name, String.valueOf(value));
        
        return( this );
    }

    /**
     * Get the value by name (case-insensitive) or empty string if it does not exist.
     * @param name
     * @return
     */
    public String getString(String name) {
        return (internalValues != null && internalValues.get(name.toLowerCase()) != null ?
                internalValues.get(name.toLowerCase()).toString() : "");
    }

    /**
     * Get the value by name (case-insensitive) or defaultValue if it does not exist or is null.
     * @param name
     * @param defaultValue
     * @return
     */
    public String getString(String name, String defaultValue) {
        return (internalValues != null && internalValues.get(name.toLowerCase()) != null ?
                internalValues.get(name.toLowerCase()).toString() : defaultValue);
    }

    /**
     *Get the value by name (case-insensitive) or 0 if it does not exist.
     * @param name
     * @return
     */
    public float getFloat(String name) {
        return (internalValues != null && internalValues.get(name.toLowerCase()) != null ?
                Helper.parseFloat(internalValues.get(name.toLowerCase()).toString()) : 0);
    }

    /**
     * Get the value by name (case-insensitive) or false if it does not exist.
     * @param name
     * @return
     */
    public boolean getBoolean(String name) {
        return (internalValues != null && internalValues.get(name.toLowerCase()) != null ?
                Helper.parseBoolean(internalValues.get(name.toLowerCase()).toString()) : false);
    }

    /**
     * Get the value by name (case-insensitive) or -1 if it does not exist or is not an integer.
     * @param name
     * @return
     */
    public int getInt(String name) {
        return (internalValues != null && internalValues.get(name.toLowerCase()) != null ?
                Helper.parseInt(internalValues.get(name.toLowerCase()).toString()) : 0);
    }

    /**
     * Get the value by name (case-insensitive) or null if it does not exist.
     * @param name
     * @return
     */
    public Object getValue(String name) {
        return (internalValues != null && internalValues.get(name.toLowerCase()) != null ?
                internalValues.get(name.toLowerCase()) : null);
    }

    /**
     * Simple test to see if attribute exists and its value is not null.
     * @param name
     * @return
     */
    public boolean isValid(String name) {
        return (internalValues != null && internalValues.get(name.toLowerCase()) != null ?
                internalValues.get(name.toLowerCase()).toString().length() > 0 : false);
    }

    /**
     * Default always returns true.
     * @return
     */
    public boolean isValid() {
        return (true);
    }

    /**
     * Reset the internal storage structures
     */
    public void reset() {
        internalValues = null;
        collections = null;
        structures = null;
        objects = null;
    }
    /**
     * Get the objects internal storage structure.
     * @return
     */
    public HashMap getObjects(){
        return( objects );
    }
    /**
     * Get the attributes internal storage structure.
     * @return
     */
    public HashMap getValues() {
        return (internalValues);
    }
    /**
     * Detailed print of the contents of this structure.
     * @return
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        Enumeration cols = getColumnNames();

        String tStr;

        if (cols != null) {
            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();

                result.append(tStr);
                result.append(":");
                result.append(getString(tStr));
                result.append(" ; ");
            }
        }

        HashMap cHash = getCollections();

        if (cHash != null) {
            cols = new IteratorEnumeration(cHash.keySet().iterator());
            
            ArrayList subList;

            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();
                result.append(tStr);
                result.append(" collection:");

                subList = getCollection(tStr);

                for (int j = 0; j < subList.size(); j++) {

                    result.append(subList.get(j).toString());

                }
            }
        }
        
        HashMap sHash = getStructures();

        if (sHash != null) {
            cols = new IteratorEnumeration(sHash.keySet().iterator());
            
            DataBean subStructure;

            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();
                result.append(tStr);
                result.append(" structure:");

                subStructure = getStructure(tStr);
                result.append(subStructure.toString());
            }
        }
        
        HashMap oHash = getObjects();

        if (oHash != null) {
            cols = new IteratorEnumeration(oHash.keySet().iterator());
            
            Object subObject;

            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();
                result.append(tStr);
                result.append(" object:");

                subObject = getObject(tStr);
                result.append(subObject.toString());
            }
        }

        return (result.toString());
    }
    /**
     * Copy the current DataBean,  making a copy of all attributes and only a "collection" copy for structures, objects, and collections.
     * 
     * 
     * @return
     */
    public DataBean deepClone() {
        DataBean result = new DataBean();

        Enumeration cols = getColumnNames();

        String tStr;

        if (cols != null) {
            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();

                result.setValue(tStr,getString(tStr));
            }
        }

        HashMap cHash = getCollections();

        if (cHash != null) {
            cols = new IteratorEnumeration(cHash.keySet().iterator());
            
            ArrayList subList;

            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();
               
                subList = getCollection(tStr);

                for (int j = 0; j < subList.size(); j++) {

                    result.addToCollection(tStr,subList.get(j));
                }
            }
        }
        
        HashMap sHash = getStructures();

        if (sHash != null) {
            cols = new IteratorEnumeration(sHash.keySet().iterator());
            
            DataBean subStructure;

            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();

                subStructure = getStructure(tStr);
                result.setStructure(tStr,subStructure);
            }
        }
        
        HashMap oHash = getObjects();

        if (oHash != null) {
            cols = new IteratorEnumeration(oHash.keySet().iterator());
            
            Object subObject;

            while (cols.hasMoreElements()) {
                tStr = cols.nextElement().toString();
            
                subObject = getObject(tStr);
                result.setObject(tStr,subObject);
            }
        }

        return (result);
    }
}
/*end DataStructure*/
