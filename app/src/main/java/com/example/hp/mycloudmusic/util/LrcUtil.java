package com.example.hp.mycloudmusic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LrcUtil {

    public static List<String> parseLrcStr(String lrcContent) {
        List<String> lrcList = new ArrayList<>();
        String[] lines = lrcContent.split("\n");
        Collections.addAll(lrcList, lines);
        return lrcList;
    }
}
