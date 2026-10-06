package com.estrellamagica.fugaz.item;

import com.estrellamagica.fugaz.EstrellaMagicaFugaz;
import com.estrellamagica.fugaz.StarColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EstrellaMagicaFugaz.MOD_ID);

    public static final Map<StarColor, RegistryObject<Item>> FRAGMENTS = new EnumMap<>(StarColor.class);
    public static final Map<StarColor, RegistryObject<Item>> RELICS = new EnumMap<>(StarColor.class);

    static {
        for (StarColor c : StarColor.values()) {
            FRAGMENTS.put(c, ITEMS.register(c.id + "_star_fragment",
                    () -> new Item(new Item.Properties())));
        }
        for (StarColor c : StarColor.values()) {
            RELICS.put(c, ITEMS.register(c.relicId,
                    () -> new StarRelicItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), c)));
        }
    }
}
