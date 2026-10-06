package com.estrellamagica.fugaz;

import com.estrellamagica.fugaz.entity.ModEntities;
import com.estrellamagica.fugaz.item.ModCreativeTabs;
import com.estrellamagica.fugaz.item.ModItems;
import com.estrellamagica.fugaz.particle.ModParticles;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EstrellaMagicaFugaz.MOD_ID)
public class EstrellaMagicaFugaz {
    public static final String MOD_ID = "estrella_magica_fugaz";

    public EstrellaMagicaFugaz() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModParticles.PARTICLES.register(bus);
        ModCreativeTabs.TABS.register(bus);
    }
}
