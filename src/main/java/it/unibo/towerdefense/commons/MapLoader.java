package it.unibo.towerdefense.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Loader for custom JSON map format with waypoints and building spots.
 */
public final class MapLoader {

    public MapData loadMap(final String filePath) throws IOException {
        final String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return parseJson(content);
    }

    public MapData loadFromClasspath(final String resourcePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Resource not found on classpath: " + resourcePath);
                return null;
            }
            final String content = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return parseJson(content);
        } catch (IOException e) {
            System.err.println("Error reading classpath resource: " + e.getMessage());
            return null;
        }
    }

    private MapData parseJson(final String content) {
        final MapData mapData = new MapData();
        mapData.setWidth(extractInt(content, "mapWidth", 800));
        mapData.setHeight(extractInt(content, "mapHeight", 600));
        mapData.setBackground(extractStringField(content, "background"));

        mapData.setWaypoints(extractDoubleArray2D(content, "waypoints"));
        mapData.setBuildingSpots(extractDoubleArray2D(content, "buildingSpots"));

        return mapData;
    }

    private int extractInt(final String json, final String field, final int fallback) {
        final Pattern p = Pattern.compile("\"" + field + "\"\\s*:\\s*(\\d+)");
        final Matcher m = p.matcher(json);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return fallback;
    }

    private String extractStringField(final String json, final String field) {
        final Pattern p = Pattern.compile("\"" + field + "\"\\s*:\\s*\"([^\"]*)\"");
        final Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private List<double[]> extractDoubleArray2D(final String json, final String field) {
        List<double[]> result = new ArrayList<>();
        final Pattern arrayStartPattern = Pattern.compile("\"" + field + "\"\\s*:\\s*\\[");
        final Matcher arrayStartMatcher = arrayStartPattern.matcher(json);
        
        if (!arrayStartMatcher.find()) return result;
        
        int startIdx = arrayStartMatcher.end() - 1;
        String arrayContent = extractBalancedArray(json, startIdx);
        if (arrayContent == null) return result;

        // Extract [x, y]
        final Pattern pairPattern = Pattern.compile("\\[\\s*([\\d.]+)\\s*,\\s*([\\d.]+)\\s*\\]");
        final Matcher pairMatcher = pairPattern.matcher(arrayContent);
        
        while (pairMatcher.find()) {
            try {
                double x = Double.parseDouble(pairMatcher.group(1));
                double y = Double.parseDouble(pairMatcher.group(2));
                result.add(new double[]{x, y});
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private String extractBalancedArray(final String json, final int start) {
        if (start < 0 || start >= json.length() || json.charAt(start) != '[') {
            return null;
        }
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            final char c = json.charAt(i);
            if (c == '[') {
                depth++;
            } else if (c == ']') {
                depth--;
                if (depth == 0) {
                    return json.substring(start + 1, i);
                }
            }
        }
        return null;
    }
}
