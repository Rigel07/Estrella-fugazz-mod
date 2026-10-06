package com.estrellamagica.fugaz;

import com.estrellamagica.fugaz.item.ModItems;
import net.minecraft.world.item.Item;

public enum StarColor {
    //          color      id        reliquia                 prob. de reliquia
    BLUE  (0x3DA5FF, "blue",   "celestial_heart",       0.25f),
    PINK  (0xFF5FC8, "pink",   "stellar_petal",         0.15f),
    GREEN (0x4CFF7A, "green",  "cosmic_emerald_shard",  0.10f),
    YELLOW(0xFFE347, "yellow", "solar_spark",           0.05f);

    public final int rgb;
    public final String id;
    public final String relicId;
    /** Probabilidad de que la estrella suelte su objeto unico especial. */
    public final float relicChance;

    StarColor(int rgb, String id, String relicId, float relicChance) {
        this.rgb = rgb;
        this.id = id;
        this.relicId = relicId;
        this.relicChance = relicChance;
    }

    public float r() { return ((rgb >> 16) & 255) / 255f; }
    public float g() { return ((rgb >> 8) & 255) / 255f; }
    public float b() { return (rgb & 255) / 255f; }

    public Item fragment() { return ModItems.FRAGMENTS.get(this).get(); }
    public Item relic() { return ModItems.RELICS.get(this).get(); }

    public static StarColor byIndex(int i) {
        StarColor[] v = values();
        return v[Math.floorMod(i, v.length)];
    }
}
