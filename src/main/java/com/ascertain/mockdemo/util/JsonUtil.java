package com.ascertain.mockdemo.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class JsonUtil {

    private static final Logger log = LogManager.getLogger(JsonUtil.class);

    public static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper()
                .configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
    }


    public static String loadJson(String path) {

        String jsonString = null;
        File file;
        InputStream is;
        BufferedReader buf = null;

        try {

            file = ResourceUtils.getFile(path);

            is = new FileInputStream(file);
            buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            jsonString = sb.toString();

        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (buf != null) buf.close();
            } catch (Exception ex) {
            }
        }

        return jsonString;
    }

    public static JsonNode getJsonFromString(String string) {

        JsonNode respObj;

        try {
            respObj = OBJECT_MAPPER.readTree(string);
        } catch (Exception e) {
            respObj = OBJECT_MAPPER.createObjectNode();
        }

        return respObj;
    }

}
