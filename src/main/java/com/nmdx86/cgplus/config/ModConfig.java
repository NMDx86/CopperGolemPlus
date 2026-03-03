package com.nmdx86.cgplus.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "copper_golem_plus")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public List<String> inputChests = new ArrayList<>();

    @ConfigEntry.Gui.Tooltip
    public List<String> outputChests = new ArrayList<>();

    @ConfigEntry.Gui.Tooltip
    public boolean allowReinforcedAsInput = true;

    @ConfigEntry.Gui.Tooltip
    public boolean allowReinforcedAsOutput = true;
}