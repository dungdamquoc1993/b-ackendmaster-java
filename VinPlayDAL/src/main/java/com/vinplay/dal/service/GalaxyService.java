package com.vinplay.dal.service;

import com.vinplay.vbee.common.models.minigame.galaxy.LSGDGalaxy;
import com.vinplay.vbee.common.models.minigame.galaxy.TopGalaxy;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface GalaxyService {
    public void logGalaxy(long var1, String var3, long var4, String var6, String var7, String var8, short var9, long var10, short var12, String var13) throws IOException, TimeoutException, InterruptedException;

    public int countLSDG(String var1, int var2);

    public List<LSGDGalaxy> getLSGD(String var1, int var2, int var3);

    public void addTop(String var1, int var2, long var3, int var5, String var6, int var7) throws IOException, TimeoutException, InterruptedException;

    public List<TopGalaxy> getTopGalaxy(int var1, int var2);

    public long getLastReferenceId();
}
