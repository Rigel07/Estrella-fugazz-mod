package com.estrellamagica.fugaz.entity;

import com.estrellamagica.fugaz.EstrellaMagicaFugaz;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EstrellaMagicaFugaz.MOD_ID);

    public static final RegistryObject<EntityType<ShootingStarEntity>> SHOOTING_STAR =
            ENTITIES.register("shooting_star", () ->
                    EntityType.Builder.<ShootingStarEntity>of(ShootingStarEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .fireImmune()
                            .noSave()
                            .clientTrackingRange(16)
                            .updateInterval(2)
                            .setShouldReceiveVelocityUpdates(true)
                            .build(new ResourceLocation(EstrellaMagicaFugaz.MOD_ID, "shooting_star").toString()));
}
