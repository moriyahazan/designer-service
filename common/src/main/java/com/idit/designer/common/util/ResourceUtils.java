package com.idit.designer.common.util;

import com.idit.designer.common.constants.DesignerServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class ResourceUtils {

    private static final Logger log = LoggerFactory.getLogger(ResourceUtils.class);

    public static String convertInputStreamToString(InputStream inputStream)
            throws IOException {

        String newLine = System.getProperty(DesignerServiceConstants.SYSTEM_LINE_SEPARATOR);
        String result;
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream)).lines()) {
            result = lines.collect(Collectors.joining(newLine));
        }
        return result;
    }
}
