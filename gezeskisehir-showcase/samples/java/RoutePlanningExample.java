package com.example.gezeskisehir.showcase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/** OSRM Table API'den gelen süre matrisi için en yakın komşu sıralaması. */
public final class RoutePlanningExample {

    private RoutePlanningExample() {
    }

    public static List<Integer> createVisitOrder(JSONArray durations) throws JSONException {
        int pointCount = durations.length();
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[pointCount];

        int current = 0;
        order.add(current);
        visited[current] = true;

        while (order.size() < pointCount) {
            JSONArray currentRow = durations.getJSONArray(current);
            int nearest = -1;
            double shortestDuration = Double.MAX_VALUE;

            for (int candidate = 0; candidate < pointCount; candidate++) {
                if (visited[candidate] || currentRow.isNull(candidate)) {
                    continue;
                }

                double duration = currentRow.getDouble(candidate);
                if (duration < shortestDuration) {
                    shortestDuration = duration;
                    nearest = candidate;
                }
            }

            if (nearest < 0) {
                break;
            }

            visited[nearest] = true;
            order.add(nearest);
            current = nearest;
        }

        return order;
    }
}
