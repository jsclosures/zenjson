# zenjson
Zero Experience Necessary serializing/deserializing JSON using a simple dynamic Java Object

Using the JSON library from https://en.wikipedia.org/wiki/Douglas_Crockford

Simple JSON Serializer

Requires on the the Crockford JSON Library

Use the DataBean to Serialize/Deserialize JSON

Simply Use the DataBean to create any JavaScript object with nested fields

DataBean test = new DataBean();

test.setValue("id", 1);

test.setValue("name", "name");

DataBean c5test = new DataBean();

c5test.setValue("id", "c5");

c5test.setValue("name", "c5name");

test.addToCollection("items", c5test);

 

String json = Helper.toJson(test);

System.out.println(json);

System.out.println(Helper.toJson(Helper.parseJSON(json)));

json = "{\"id\":1,\"name\":\"name\",\"items\":[{\"id\":11,\"name\":\"child's name\"}]}";

test = Helper.parseJSON(json);

System.out.println(test); json = Helper.toJson(test);

System.out.println(json);
