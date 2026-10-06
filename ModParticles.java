package com.estrellamagica.fugaz.particle;

import com.estrellamagica.fugaz.EstrellaMagicaFugaz;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EstrellaMagicaFugaz.MOD_ID);

    public static final RegistryObject<SimpleParticleType> STAR_GLOW =
            PARTICLES.register("star_glow", () -> new SimpleParticleType(true));
}
