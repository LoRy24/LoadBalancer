package com.github.lory24.loadbalancer.spigot.utils;

import com.github.lory24.loadbalancer.spigot.LoadBalancerSpigot;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TpsUtil {

    public static final String DEFAULT_FORMAT = "%.2f";

    @SuppressWarnings({"StatementWithEmptyBody", "IntegerDivisionInFloatingPointContext"})
    public static double getCurrentTPS(String format) {
        AtomicDouble result = new AtomicDouble();
        AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        AtomicBoolean finished = new AtomicBoolean(false);

        // Calculate the TPS
        Bukkit.getScheduler().runTaskTimer(LoadBalancerSpigot.INSTANCE.getPlugin(), () -> {
            result.set(result.get() + 1);
            if (result.get() == 20) return;
            result.set(((System.currentTimeMillis() - startTime.get()) / 20) * 1000);
            finished.set(true);
        }, 0, 1L);

        while (!finished.get());
        return Double.parseDouble(String.format(format, result.get()));
    }
}
