package sircow.roomfortwo.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class RenderManagerMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void roomfortwo$hideLocalPlayerWhileSleeping(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (!(entityIn instanceof EntityPlayer)) return;
        if (!((EntityPlayer) entityIn).isPlayerSleeping()) return;

        EntityPlayerSP localPlayer = Minecraft.getMinecraft().player;

        if (localPlayer != null && entityIn == localPlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            cir.setReturnValue(false);
        }
    }
}
