package com.estrellamagica.fugaz.client;

import com.estrellamagica.fugaz.EstrellaMagicaFugaz;
import com.estrellamagica.fugaz.entity.ModEntities;
import com.estrellamagica.fugaz.particle.ModParticles;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EstrellaMagicaFugaz.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // La estrella se dibuja solo con particulas, la entidad en si no tiene modelo
        event.registerEntityRenderer(ModEntities.SHOOTING_STAR.get(), NoopRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.STAR_GLOW.get(), StarGlowParticle.Provider::new);
    }
}
