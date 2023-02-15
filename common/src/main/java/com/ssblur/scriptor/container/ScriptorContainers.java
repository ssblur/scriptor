package com.ssblur.scriptor.container;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.screen.WritingDeskContainerScreen;
import dev.architectury.platform.Platform;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class ScriptorContainers {
  private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ScriptorMod.MOD_ID, Registry.MENU_REGISTRY);

  public static final ResourceLocation WRITING_DESK_CONTAINER_ID = new ResourceLocation(ScriptorMod.MOD_ID, "writing_desk");
  public static final RegistrySupplier<MenuType<WritingDeskContainer>> WRITING_DESK_CONTAINER =
    CONTAINERS.register(
      WRITING_DESK_CONTAINER_ID,
      () -> MenuRegistry.ofExtended((id, inv, buffer) -> new WritingDeskContainer(id, inv))
    );

  public static void register() {
    CONTAINERS.register();

    if(Platform.getEnv() == EnvType.CLIENT) {
      MenuRegistry.registerScreenFactory(WRITING_DESK_CONTAINER.get(), WritingDeskContainerScreen::new);
    }
  }
}
