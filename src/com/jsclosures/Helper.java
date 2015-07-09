package com.jsclosures;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

/**
A generalized utility class that provides easy to operations

 */

public class Helper extends DataBean {

    public Helper() {
        super();
    }

    public static boolean DEBUG = true;
    public static boolean LOCALTRACE = false;
    final static Logger logger = Logger.getLogger(Helper.class);
    
    public static void writeLog(Object caller,int level, String message) {
        if (DEBUG ) {
            if (message == null) {
                message = "null message";
            }
            if( caller != null ){
                message = caller.getClass().getName().concat(": ").concat(message);
            }
                    
            if(logger.isDebugEnabled()){
                logger.debug(message);
            }
            else {
                if( level == 1 )
                    logger.error(message);
                else
                    logger.warn(message);
            }
            
            if (LOCALTRACE) {
                try {
                    java.io.FileOutputStream errorLog =
                        new java.io.FileOutputStream(File.pathSeparator.equalsIgnoreCase(";") ? "c:\\temp\\rest.txt" :
                                                     "/tmp/rest.txt", true);

                    java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(errorLog));

                    bw.write(message, 0, message.length());
                    bw.write("\n");
                    bw.close();

                    errorLog.close();
                } catch (java.io.IOException e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    public static int parseInt(String v) {
        int result = -1;

        try {
            result = Integer.parseInt(v);
        } catch (Exception e) {

        }
        return (result);
    }
    
    public static boolean parseBoolean(String v) {
        boolean result = false;

        try {
            result = Boolean.parseBoolean(v);
        } catch (Exception e) {

        }
        return (result);
    }
    
    public static float parseFloat(String v) {
        float result = -1;

        try {
            result = Float.parseFloat(v);
        } catch (Exception e) {

        }
        return (result);
    }

    public static ArrayList<String> splitFields(String sSrc, String sDelim) {
        ArrayList<String> retval = new ArrayList<String>();
        int nd = sDelim.length();
        int ns = sSrc.length();
        int i = 0;
        int j = 0;
        boolean bf = false;
        char q = '\"';

        if (sDelim.length() != 0 && ns != 0) {
            while (j + nd <= ns) {
                if (sSrc.charAt(j) == q) {
                    bf = !bf;

                }
                if (sSrc.substring(j, j + nd).equals(sDelim) && !bf) {
                    retval.add(sSrc.substring(i, j));
                    j += nd;
                    i = j;
                } else {
                    j++;
                }
            }
            retval.add(sSrc.substring(i));
        }

        return retval;
    }

    public static ArrayList<String> simpleSplitFields(String sSrc, String sDelim) {
        ArrayList<String> retval = new ArrayList<String>();
        int nd = sDelim.length();
        int ns = sSrc.length();
        int i = 0;
        int j = 0;
        boolean bf = false;
        char q = '\"';

        if (sDelim.length() != 0) {
            while (j + nd <= ns) {
                if (sSrc.substring(j, j + nd).equals(sDelim)) {
                    retval.add(sSrc.substring(i, j));
                    j += nd;
                    i = j;
                } else {
                    j++;
                }
            }
            retval.add(sSrc.substring(i));
        }

        return retval;
    }

    public static DataBean readFromRESTURL(String url) {
        String json = readFromURL(url);

        return (parseJSON(json));
    }

    public static String readFromURL(String url) {
        StringBuffer result = new StringBuffer();
        try {
            URL u = new URL(url);
            InputStream in = u.openStream();
            InputStreamReader uin = new InputStreamReader(in);
            BufferedReader fin = new BufferedReader(uin);

            String buffer;
            while ((buffer = fin.readLine()) != null) {
                result.append(buffer);

            }

            uin.close();
            in.close();
        } catch (IOException e) {
            result.append("Error: " + e.toString() + " " + e.getMessage());
        }
        return (result.toString());
    }


    public static ArrayList<DataBean> cloneList(ArrayList<DataBean> src) {
        ArrayList<DataBean> result = new ArrayList<DataBean>(src.size());

        for (int i = 0, size = src.size(); i < size; i++)
            result.add(src.get(i));

        return (result);
    }

    public static String readPut(HttpServletRequest req) {
        StringBuffer result = new StringBuffer();

        try {
            InputStream in = req.getInputStream();
            DataInputStream din = new DataInputStream(in);
            int dSize = in.available();
            int size;
            int blockSize = 1024;

            byte buffer[] = new byte[blockSize];


            int i;
            for (;;) {
                size = din.read(buffer, 0, blockSize);

                for (i = 0; i < size; i++) {
                    result.append((char) buffer[i]);

                }
                if (size < 0) {
                    break;
                }
            }
        } catch (Exception e) {
            writeLog(Helper.class,1, "Read Error: " + e.toString());

        }

        return (result.toString());
    }

    public static DataBean parseJSON(String json) {
        DataBean result = new DataBean();

        try {
            JSONObject obj = new JSONObject(json);
            result = parseJSON(obj);
        } catch (Exception e) {
            result.setValue("error", "parse error: " + e.toString());
            //writeLog(1, result.getString("error"));
        }

        return (result);
    }


    public static DataBean parseJSON(JSONObject json) throws Exception {

        DataBean result = new DataBean();
        //writeLog(1,"internal parse:");

        String fNames[] = JSONObject.getNames(json);
        for (int i = 0; i < fNames.length; i++) {
            Object currentObject = ((JSONObject) json).get(fNames[i]);
            //writeLog(1,"fcolumn: " + fNames[i] + " instance of: " + currentObject.getClass().getName());

            if (currentObject instanceof String || currentObject instanceof JSONString ||
                currentObject instanceof Integer || currentObject instanceof Float || currentObject instanceof Double ||
                currentObject instanceof Boolean) {
                result.setValue(fNames[i], currentObject.toString());
                //writeLog(1,"column: " + fNames[i] + " value: " + result.getString(fNames[i]));
            } else if (currentObject instanceof JSONArray) {

                JSONArray currentArray = (JSONArray) currentObject;
                for (int j = 0, jsize = currentArray.length(); j < jsize; j++) {
                    if (currentArray.get(j) instanceof String || currentArray.get(j) instanceof JSONString) {
                        result.addToCollection(fNames[i], currentArray.get(j).toString());
                        //writeLog(1,"column: " + fNames[i] + " value: " + result.getString(fNames[i]));
                    } else
                        result.addToCollection(fNames[i], parseJSON((JSONObject) currentArray.get(j)));
                }
            } else if (currentObject instanceof JSONObject) {
                result.setStructure(fNames[i], parseJSON((JSONObject) currentObject));
            }

        }
        
        return (result);
    }

    public static DataBean readJSONFromString(String jsonData) {
        DataBean result = new DataBean();
        try {
            //writeLog(1, "parseing json");
            JSONObject obj = new JSONObject(jsonData);
            String fNames[] = JSONObject.getNames(obj);

            for (int i = 0; i < fNames.length; i++) {
                result.setValue(fNames[i], (String) obj.get(fNames[i]));
            }
        } catch (Exception e) {
            //writeLog(2, "json parse failed: " + e.toString());
            result.setValue("error","json parse failed: " + e.toString());
        }
        //writeLog(1, "after parsing json: " + result.toString());
        return (result);
    }

    public static int indexOf(ArrayList<DataBean> list, String column, String value) {
        int result = -1;

        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getString(column).equalsIgnoreCase(value)) {
                result = i;

                break;
            }

        }

        return (result);
    }


    public static int indexOf(ArrayList<DataBean> list, String column1, String value1, String column2, String value2) {
        int result = -1;

        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getString(column1).equalsIgnoreCase(value1) &&
                list.get(i).getString(column2).equalsIgnoreCase(value2)) {
                result = i;

                break;
            }

        }

        return (result);
    }
    /**
     * Get all the parameters from the HTTP request
     * @param req
     * @return
     */
    public static DataBean getParameters(HttpServletRequest req) {
        DataBean result = new DataBean();

        Enumeration parameterNames = req.getParameterNames();

        if (parameterNames != null) {
            String tStr;
            String vStr;
            while (parameterNames.hasMoreElements()) {
                tStr = parameterNames.nextElement().toString();
                vStr = req.getParameter(tStr);

                if (vStr != null) {
                    result.setValue(tStr, vStr);
                }
            }
        }

        return (result);
    }
    /**
     * Get the parameters as an array
     * @param req
     * @return
     */
    public static ArrayList getParameterList(HttpServletRequest req) {
        ArrayList result = new ArrayList();

        Enumeration parameterNames = req.getParameterNames();

        if (parameterNames != null) {
            String tStr;
            String vStr;
            while (parameterNames.hasMoreElements()) {
                tStr = parameterNames.nextElement().toString();
                vStr = req.getParameter(tStr);

                if (vStr != null) {
                    DataBean tmp = new DataBean();
                    tmp.setValue("name", tStr);
                    tmp.setValue("value", vStr);

                    result.add(tmp);
                }
            }
        }

        return (result);
    }
    
    public static String toJson(DataBean target) {
        StringBuilder result = new StringBuilder("{");
        Enumeration columNameList = target.getColumnNames();

        String tKey;
        boolean hasColumns = false;
        if (columNameList != null) {
            int k = 0;
            while (columNameList.hasMoreElements()) {
                tKey = columNameList.nextElement().toString();

                if (k++ > 0)
                    result.append(",");
                result.append("\"");
                result.append(tKey);
                result.append("\":\"");
                result.append(StringEscapeUtils.escapeJson(target.getString(tKey)));
                result.append("\"");
                if (!hasColumns)
                    hasColumns = true;
            }

        }

        if (target.getCollections() != null) {
            Enumeration cKeys = new IteratorEnumeration(target.getCollections().keySet().iterator());
            String cName;

            if (hasColumns) {
                result.append(",");
            }

            if (!hasColumns)
                hasColumns = true;
            

            ArrayList subList;
            DataBean subEntry;
            int counter = 0;
            while (cKeys.hasMoreElements()) {
                cName = cKeys.nextElement().toString();

                subList = target.getCollection(cName);
                if( counter++ > 0 ){
                    result.append(",");
                }
                result.append("\"");
                result.append(cName);
                result.append("\":[");

                for (int j = 0; j < subList.size(); j++) {
                    if (j > 0) {
                        result.append(",");
                    }

                    if (subList.get(j) instanceof DataBean) {
                        subEntry = (DataBean) subList.get(j);

                        result.append(toJson(subEntry));
                    } else {
                        result.append("\"");
                        result.append(subList.get(j).toString());
                        result.append("\"");
                    }
                }

                result.append("]");

            }
        }


        if (target.getStructures() != null) {
            Enumeration cKeys = new IteratorEnumeration(target.getStructures().keySet().iterator());
            String cName;

            
            DataBean subEntry;

            while (cKeys.hasMoreElements()) {
                cName = cKeys.nextElement().toString();

                subEntry = target.getStructure(cName);
                
                if (hasColumns) {
                    result.append(",");
                }
                else {
                    hasColumns = true;
                }
                result.append("\"");
                result.append(cName);
                result.append("\":");
                result.append(toJson(subEntry));
            }
        }

        result.append("}");

        return (result.toString());
    }


    public static void main(String[] args) {
        if( true ) {
            DataBean test = new DataBean();
        test.setValue("id", 1);
        test.setValue("name", "name");      
        
        DataBean c5test = new DataBean();
        c5test.setValue("id", "c5");
        c5test.setValue("name", "c5name");

        test.addToCollection("items", c5test);
        
            DataBean c55test = new DataBean();
            c55test.setValue("id", "c55");
            c55test.setValue("name", "c55name");

            test.setStructure("c55test", c55test);

        DataBean c6test = new DataBean();
        c6test.setValue("id", "c6");
        c6test.setValue("name", "c6name");

        c5test.addToCollection("children", c6test);
        
            DataBean c7test = new DataBean();
            c7test.setValue("id", "c7");
            c7test.setValue("name", "c7name");

            c5test.addToCollection("children", c7test);
            
            DataBean c8test = new DataBean();
            c8test.setValue("id", "c8");
            c8test.setValue("name", "c8name");

            c5test.addToCollection("sitems", c8test);
            
            DataBean c9test = new DataBean();
            c9test.setValue("id", "c9");
            c9test.setValue("name", "c9name");

            c5test.addToCollection("sitems", c9test);
            
            
            DataBean c10test = new DataBean();
            c10test.setValue("id", "c10");
            c10test.setValue("name", "c10name");

            test.addToCollection("sitems", c10test);
            
            DataBean c11test = new DataBean();
            c11test.setValue("id", "c11");
            c11test.setValue("name", "c11name");

            test.addToCollection("sitems", c11test);
            
            DataBean c12test = new DataBean();
            c12test.setValue("id", "c12");
            c12test.setValue("name", "c12name");

            test.addToCollection("bitems", c12test);
            
            DataBean c13test = new DataBean();
            c13test.setValue("id", "c13");
            c13test.setValue("name", "c13name");

            test.addToCollection("bitems", c13test);

        String json = Helper.toJson(test);
        
        System.out.println(json);
        
        System.out.println(Helper.toJson(Helper.parseJSON(json)));
        
        json = "{\"id\":1,\"name\":\"name\",\"items\":[{\"id\":11,\"name\":\"child's name\"}]}";
        
        test = Helper.parseJSON(json);
        
        System.out.println(test);
        
        json = Helper.toJson(test);
                
        System.out.println(json);
        }
        
        System.exit(0);

    }

}
/*end DataStructure*/
