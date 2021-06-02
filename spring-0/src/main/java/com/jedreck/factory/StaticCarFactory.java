package com.jedreck.factory;

import com.jedreck.entity.Car;

import java.util.HashMap;
import java.util.Map;

public class StaticCarFactory {
    private static final Map<Integer, Car> carMap;

    static {
        carMap = new HashMap<>();
        carMap.put(1, new Car(1, "奥迪"));
        carMap.put(2, new Car(2, "奥拓"));
    }

    public static Car getCar(Integer num) {
        return carMap.get(num);
    }

}
