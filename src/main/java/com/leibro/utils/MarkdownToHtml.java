package com.leibro.utils;



import org.pegdown.PegDownProcessor;



/**
 * Created by leibro on 2017/1/7.
 */
public class MarkdownToHtml {

    public static String markdownToHtml(String markdown) {
        PegDownProcessor pegDownProcessor = new PegDownProcessor();
        return pegDownProcessor.markdownToHtml(markdown);
    }
}
