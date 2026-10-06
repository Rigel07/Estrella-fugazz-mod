package com.estrellamagica.fugaz.item;

import com.estrellamagica.fugaz.EstrellaMagicaFugaz;
import com.estrellamagica.fugaz.StarColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EstrellaMagicaFugaz.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + EstrellaMagicaFugaz.MOD_ID))
                    .icon(() -> new ItemStack(StarColor.BLUE.fragment()))
                    .displayItems((params, output) -> {
                        for (StarColor c : StarColor.values()) output.accept(c.fragment());
                        for (StarColor c : StarColor.values()) output.accept(c.relic());
                    })
                    .build());
}
