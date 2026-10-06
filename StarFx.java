package com.estrellamagica.fugaz.event;

import com.estrellamagica.fugaz.StarColor;
import com.estrellamagica.fugaz.particle.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class StarFx {
    /** Explosion de particulas brillantes visible desde lejos (hasta 200 bloques). */
    public static void burst(ServerLevel level, Vec3 pos, StarColor color, int count, double spread) {
        for (ServerPlayer p : level.players()) {
            if (p.position().distanceToSqr(pos) > 200 * 200) continue;
            for (int i = 0; i < count; i++) {
                double dx = (level.random.nextDouble() - 0.5) * spread;
                double dy = (level.random.nextDouble() - 0.2) * spread;
                double dz = (level.random.nextDouble() - 0.5) * spread;
                // con count=0 el cliente recibe (r,g,b) como "velocidad": lo usamos como color
                level.sendParticles(p, ModParticles.STAR_GLOW.get(), true,
                        pos.x + dx, pos.y + dy, pos.z + dz, 0,
                        color.r(), color.g(), color.b(), 1.0);
            }
        }
    }
}
