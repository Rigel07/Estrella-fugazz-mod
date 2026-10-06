package com.estrellamagica.fugaz.event;

import com.estrellamagica.fugaz.EstrellaMagicaFugaz;
import com.estrellamagica.fugaz.StarColor;
import com.estrellamagica.fugaz.entity.ModEntities;
import com.estrellamagica.fugaz.entity.ShootingStarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EstrellaMagicaFugaz.MOD_ID)
public class StarSpawner {

    // ======== AJUSTA LA FRECUENCIA AQUI ========
    /** Cada cuantos ticks se "tira el dado" por jugador (200 = 10 s). */
    private static final int CHECK_INTERVAL = 200;
    /** Probabilidad de que aparezca una estrella en cada tirada (~7 por noche). */
    private static final double SPAWN_CHANCE = 0.15;
    /** De todas las estrellas, cuantas llegan a caer al suelo (~1 por noche o menos). */
    private static final double IMPACT_CHANCE = 0.12;
    // ===========================================

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel level)) return;
        if (level.dimension() != Level.OVERWORLD) return;
        if (level.getGameTime() % CHECK_INTERVAL != 0) return;

        long t = level.getDayTime() % 24000L;
        if (t < 13000L || t > 23000L) return; // solo de noche
        if (level.isThundering()) return;

        for (ServerPlayer player : level.players()) {
            if (player.isSpectator()) continue;
            if (level.random.nextDouble() < SPAWN_CHANCE) {
                spawnStar(level, player);
            }
        }
    }

    private static void spawnStar(ServerLevel level, ServerPlayer player) {
        RandomSource r = level.random;

        double ang = r.nextDouble() * Math.PI * 2;
        double startDist = 70 + r.nextDouble() * 50;
        double startY = Math.min(player.getY() + 90 + r.nextDouble() * 30, level.getMaxBuildHeight() - 5);
        Vec3 start = new Vec3(player.getX() + Math.cos(ang) * startDist, startY,
                player.getZ() + Math.sin(ang) * startDist);

        if (!level.isPositionEntityTicking(BlockPos.containing(start))) return;

        boolean impact = r.nextDouble() < IMPACT_CHANCE;
        Vec3 target;
        if (impact) {
            double a2 = r.nextDouble() * Math.PI * 2;
            double d = 25 + r.nextDouble() * 45;
            int tx = (int) Math.floor(player.getX() + Math.cos(a2) * d);
            int tz = (int) Math.floor(player.getZ() + Math.sin(a2) * d);
            BlockPos bp = new BlockPos(tx, 64, tz);
            if (!level.hasChunkAt(bp)) return;
            int ty = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, tx, tz);
            target = new Vec3(tx + 0.5, ty, tz + 0.5);
        } else {
            // cruza el cielo hacia el lado contrario y se apaga antes de tocar nada
            double a2 = ang + Math.PI + (r.nextDouble() - 0.5);
            target = new Vec3(player.getX() + Math.cos(a2) * startDist,
                    startY - 30 - r.nextDouble() * 20,
                    player.getZ() + Math.sin(a2) * startDist);
        }

        double speed = 1.2 + r.nextDouble() * 0.5;
        Vec3 velocity = target.subtract(start).normalize().scale(speed);
        int life = impact ? 600 : (int) (start.distanceTo(target) / speed * 0.9);

        StarColor color = StarColor.byIndex(r.nextInt(StarColor.values().length));

        ShootingStarEntity star = new ShootingStarEntity(ModEntities.SHOOTING_STAR.get(), level);
        star.setPos(start.x, start.y, start.z);
        star.configure(color, impact, velocity, life);
        level.addFreshEntity(star);
    }
}
