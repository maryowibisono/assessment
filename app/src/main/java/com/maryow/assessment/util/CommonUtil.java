package com.maryow.assessment.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonUtil {

    public static ArrayList<String> distinctListString(ArrayList<String> sourceList){
        Set<String> hs = new HashSet<>();
        hs.addAll(sourceList);
        sourceList.clear();
        sourceList.addAll(hs);

        return sourceList;
    }

    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() < fromIndex){
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public static String removeHttp(String url){
        URI uri;
        String hostname;
        try {
            uri = new URI(url);
           hostname = uri.getHost();
            if (hostname != null) {
                hostname =  hostname.startsWith("www.") ? hostname.substring(4) : hostname;
            }
        } catch (URISyntaxException e) {
            hostname =  "";
        }

        return hostname;

    }


}
