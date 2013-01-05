/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webLogger.controller;

import java.nio.charset.Charset;

/**
 *
 * @author zzz
 */
public class Tools {
    
    public static String antiHakzor(String post) {
        post = post.replaceAll("<", "&lt");
        post = post.replaceAll(">", "&gt");
        post = post.replaceAll("\"", "&quot");
    return post;
    }
    
    public static String trim(String s, int to) {
        if (to > 0 && s!=null) {
            s = s.trim();
            if (s.length() > to) 
                return s.substring(0, to - 1);
        }   return s;
     }
    
    public static String reEncode( String input ) {
    Charset utf8 = Charset.forName("UTF-8");
    return new String(input.getBytes(), utf8 );
}

}
