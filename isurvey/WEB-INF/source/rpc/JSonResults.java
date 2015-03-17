/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rpc;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gary
 */
public class JSonResults {
    
    public String stringResult(Object obj){
        String result = null;
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory(){
            public List creatArrayContainer() {
                return new LinkedList();
            }
            public Map createObjectContainer() {
                return new LinkedHashMap();
            }                    
        };           
        try{
            Map json = (Map)parser.parse(obj.toString(), containerFactory);
            Iterator iter = json.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                if ("result".equals(entry.getKey().toString())){
                    result = (String) entry.getValue();
                }
            }
        }
        catch(ParseException pe){
            System.out.println(pe);
        }
        return result;
    }

    public Object objectResult(Object obj) {
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory(){
            public List creatArrayContainer() {
                return new LinkedList();
            }
            public Map createObjectContainer() {
                return new LinkedHashMap();
            }
        };
        try{
            Map json = (Map)parser.parse(obj.toString(), containerFactory);
            Iterator iter = json.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                if ("result".equals(entry.getKey().toString())){
                    return entry.getValue();
                }
            }
        }
        catch(ParseException pe){
            System.out.println(pe);
        }
        return null;
    }
}
