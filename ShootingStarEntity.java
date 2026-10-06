package com.estrellamagica.fugaz.entity;

import com.estrellamagica.fugaz.StarColor;
import com.estrellamagica.fugaz.event.StarFx;
import com.estrellamagica.fugaz.particle.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class ShootingStarEntity extends Entity {
    private static final EntityDataAccessor<Integer> COLOR =
            SynchedEntityData.defineId(ShootingStarEntity.class, EntityDataSerializers.INT);

    /** Radio de la explosion al caer (TNT = 4). Pequeno: "un poco de destruccion". */
    private static final float EXPLOSION_RADIUS = 2.8f;

    private boolean impactStar = false;
    private int maxLife = 120;

    public ShootingStarEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public void configure(StarColor color, boolean impact, Vec3 velocity, int life) {
        this.entityData.set(COLOR, color.ordinal());
        this.impactStar = impact;
        this.maxLife = life;
        this.setDeltaMovement(velocity);
    }

    public StarColor getStarColor() {
        return StarColor.byIndex(this.entityData.get(COLOR));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLOR, 0);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 from = this.position();
        Vec3 to = from.add(this.getDeltaMovement());

        if (!this.level().isClientSide) {
            if (this.impactStar) {
                BlockHitResult hit = this.level().clip(new ClipContext(from, to,
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                if (hit.getType() != HitResult.Type.MISS) {
                    impact(hit.getLocation());
                    return;
                }
                if (to.y < this.level().getMinBuildHeight()) {
                    this.discard();
                    return;
                }
            }
            if (this.tickCount > this.maxLife) {
                // Las estrellas "visuales" se consumen en el cielo
                if (this.level() instanceof ServerLevel sl) {
                    StarFx.burst(sl, from, getStarColor(), 25, 3.0);
                }
                this.discard();
                return;
            }
        } else {
            spawnTrail(from, to);
        }
        this.setPos(to.x, to.y, to.z);
    }

    private void spawnTrail(Vec3 from, Vec3 to) {
        StarColor c = getStarColor();
        for (int i = 0; i < 8; i++) {
            double t = this.random.nextDouble();
            double x = Mth.lerp(t, from.x, to.x) + (this.random.nextDouble() - 0.5) * 0.7;
            double y = Mth.lerp(t, from.y, to.y) + (this.random.nextDouble() - 0.5) * 0.7;
            double z = Mth.lerp(t, from.z, to.z) + (this.random.nextDouble() - 0.5) * 0.7;
            this.level().addParticle(ModParticles.STAR_GLOW.get(), true, x, y, z, c.r(), c.g(), c.b());
        }
        // cabeza casi blanca, mas brillante
        float wr = Mth.lerp(0.6f, c.r(), 1f);
        float wg = Mth.lerp(0.6f, c.g(), 1f);
        float wb = Mth.lerp(0.6f, c.b(), 1f);
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(ModParticles.STAR_GLOW.get(), true,
                    to.x, to.y, to.z, wr, wg, wb);
        }
        this.level().addParticle(ParticleTypes.END_ROD, true, to.x, to.y, to.z, 0, 0, 0);
    }

    private void impact(Vec3 pos) {
        if (!(this.level() instanceof ServerLevel sl)) return;
        StarColor c = getStarColor();
        this.setPos(pos.x, pos.y, pos.z);

        // Destruccion moderada (respeta la gamerule mobGriefing)
        sl.explode(this, pos.x, pos.y, pos.z, EXPLOSION_RADIUS, Level.ExplosionInteraction.MOB);

        StarFx.burst(sl, pos.add(0, 1, 0), c, 90, 5.0);
        sl.playSound(null, pos.x, pos.y, pos.z, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.AMBIENT, 6f, 0.6f);
        sl.playSound(null, pos.x, pos.y, pos.z, SoundEvents.FIREWORK_ROCKET_LARGE_BLAST_FAR, SoundSource.AMBIENT, 6f, 0.7f);

        // Fragmentos
        int n = 2 + this.random.nextInt(4); // 2-5
        drop(sl, pos, new ItemStack(c.fragment(), n), false);

        // Objeto unico especial (probabilidad segun el color)
        if (this.random.nextFloat() < c.relicChance) {
            drop(sl, pos, new ItemStack(c.relic()), true);
        }
        this.discard();
    }

    private void drop(ServerLevel sl, Vec3 pos, ItemStack stack, boolean glow) {
        ItemEntity item = new ItemEntity(sl, pos.x, pos.y + 1.0, pos.z, stack);
        item.setDeltaMovement((this.random.nextDouble() - 0.5) * 0.2, 0.3, (this.random.nextDouble() - 0.5) * 0.2);
        if (glow) {
            item.setGlowingTag(true);
            item.setUnlimitedLifetime();
        }
        sl.addFreshEntity(item);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) { }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) { }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
