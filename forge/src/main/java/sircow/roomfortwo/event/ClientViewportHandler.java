package sircow.roomfortwo.event;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sircow.roomfortwo.util.BedOccupancyTracker;

@Mod.EventBusSubscriber(modid = "roomfortwo", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientViewportHandler {
    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Camera camera = event.getCamera();

        if (!(camera.entity() instanceof LivingEntity livingEntity)) return;
        if (!livingEntity.isSleeping()) return;
        if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) return;

        Direction direction = livingEntity.getBedOrientation();
        if (direction == null) return;

        float baseYaw = direction.toYRot();
        int slot = BedOccupancyTracker.getSlot(livingEntity.getId());

        float targetYaw, targetRoll;

        if (slot % 2 == 0) {
            targetYaw = baseYaw - 90.0F;
            targetRoll = -90.0F;
        }
        else {
            targetYaw = baseYaw + 90.0F;
            targetRoll = 90.0F;
        }

        event.setYaw(targetYaw);
        event.setPitch(0.0F);
        event.setRoll(targetRoll);
    }
}
