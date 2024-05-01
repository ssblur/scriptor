package com.ssblur.scriptor.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;

import java.util.ArrayList;
import java.util.List;

public class ParticleQueue {
  public static final ParticleQueue INSTANCE = new ParticleQueue();
  record Entry(ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i){}
  List<Entry> queue = new ArrayList<>();

  public static void queue(ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i) {
    INSTANCE.queue.add(new Entry(particleOptions, d, e, f, g, h, i));
  }

  public static void queue(ParticleOptions particleOptions, double d, double e, double f) {
    queue(particleOptions, d, e, f, 0, 0, 0);
  }

  public void process(ClientLevel instance) {
    var level = Minecraft.getInstance().level;
    while(level != null && !queue.isEmpty()) {
      var item = queue.remove(0);
      level.addParticle(item.particleOptions, item.d, item.e, item.f, item.g, item.h, item.i);
    }

  }
}
