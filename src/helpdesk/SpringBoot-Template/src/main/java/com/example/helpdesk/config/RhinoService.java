package com.example.helpdesk.config;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Service;

@Service
public class RhinoService {
    public Object chat(String input){
        Context cx = Context.enter();
        try{
            Scriptable scriptable = cx.initStandardObjects();
            return cx.evaluateString(scriptable, input, "", 1, null);
        } finally {
            Context.exit();
        }
    }
}