package de.tub.ise.ec;

import java.io.Serializable;

public class Message implements Serializable {

    String key,value,operation; //C,R,U,D
    int id=-1;

    public  Message(int _id, String _key,String _value){
        key = _key;
        value = _value;
        id=_id;
        operation = "U";
    }

    public  Message(int _id, String _key,String _value, String _operation){
        key = _key;
        value = _value;
        operation = _operation;
        id=_id;
    }

}
