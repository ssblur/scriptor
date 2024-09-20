package com.ssblur.scriptor.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;

import java.util.ArrayList;
import java.util.List;

public class ParticleQueue {
  public static final ParticleQueue INSTANCE = new ParticleQueue();

  /**
   * A particle to queue for placement in the world thread.
   * @param particleOptions This particle's ParticleOptions
   * @param x x position
   * @param y y position
   * @param z z position
   * @param vx x velocity
   * @param vy y velocity
   * @param vz z velocity
   */
  record Entry(ParticleOptions particleOptions, double x, double y, double z, double vx, double vy, double vz){}
  List<Entry> queue = new ArrayList<>();

  /**
   * Queue a particle for placement during the world render thread.
   * @param particleOptions This particle's ParticleOptions
   * @param x x position
   * @param y y position
   * @param z z position
   * @param vx x velocity
   * @param vy y velocity
   * @param vz z velocity
   */
  public static void queue(ParticleOptions particleOptions, double x, double y, double z, double vx, double vy, double vz) {
    INSTANCE.queue.add(new Entry(particleOptions, x, y, z, vx, vy, vz));
  }

  /**
   * Queue a particle for placement during the world render thread.
   * Queues with no velocity
   * @param particleOptions This particle's ParticleOptions
   * @param x x position
   * @param y y position
   * @param z z position
   */
  public static void queue(ParticleOptions particleOptions, double x, double y, double z) {
    queue(particleOptions, x, y, z, 0, 0, 0);
  }

  /**
   * Process out the particle queue.
   * Should only be called from within the level render thread.
   * @param instance The local level to add particles to
   */
  public void process(ClientLevel instance) {
    var level = Minecraft.getInstance().level;
    while(level != null && !queue.isEmpty()) {
      var item = queue.removeFirst();
      level.addParticle(item.particleOptions, item.x, item.y, item.z, item.vx, item.vy, item.vz);
    }

  }
}
