package com.estrellamagica.fugaz.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

/** Particula luminosa a brillo maximo (se ve "encendida" incluso de noche). Color = (r,g,b) recibidos como velocidad. */
public class StarGlowParticle extends TextureSheetParticle {
    private final float startSize;

    protected StarGlowParticle(ClientLevel level, double x, double y, double z,
                               float r, float g, float b, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.lifetime = 35 + this.random.nextInt(30);
        this.quadSize = 1.0f + this.random.nextFloat() * 1.5f;
        this.startSize = this.quadSize;
        this.hasPhysics = false;
        this.gravity = 0f;
        this.xd = (this.random.nextDouble() - 0.5) * 0.02;
        this.yd = (this.random.nextDouble() - 0.5) * 0.02;
        this.zd = (this.random.nextDouble() - 0.5) * 0.02;
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        float k = 1f - (float) this.age / (float) this.lifetime;
        this.alpha = Math.max(0f, k);
        this.quadSize = this.startSize * (0.4f + 0.6f * k);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880; // brillo maximo
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public net.minecraft.client.particle.Particle createParticle(SimpleParticleType type, ClientLevel level,
                                                                      double x, double y, double z,
                                                                      double xs, double ys, double zs) {
            return new StarGlowParticle(level, x, y, z, (float) xs, (float) ys, (float) zs, sprites);
        }
    }
}
