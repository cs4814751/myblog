package com.leibro.utils;


import org.pegdown.PegDownProcessor;

import static org.pegdown.Extensions.HARDWRAPS;

/**
 * Created by leibro on 2017/1/7.
 */
public class MarkdownToHtml {
    private final static PegDownProcessor PEG_DOWN_PROCESSOR;

    static {
        PEG_DOWN_PROCESSOR = new PegDownProcessor(HARDWRAPS);
    }

    public static String markdownToHtml(String markdown) {
        return PEG_DOWN_PROCESSOR.markdownToHtml(markdown);
    }
}
