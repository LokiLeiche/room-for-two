package sircow.roomfortwo.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.roomfortwo.util.BedOccupancyTracker;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
    @Inject(method = "trySleep", at = @At("RETURN"))
    private void roomfortwo$onStartSleeping(BlockPos bedLocation, CallbackInfoReturnable<EntityPlayer.SleepResult> cir) {
        if (cir.getReturnValue() != EntityPlayer.SleepResult.OK) return;

        EntityPlayer self = (EntityPlayer) (Object) this;
        if (!(self.world instanceof WorldServer)) return;

        BedOccupancyTracker.updateBedOccupancy((WorldServer) self.world, bedLocation, -1);
    }

    @Inject(method = "wakeUpPlayer", at = @At("HEAD"))
    private void roomfortwo$onStopSleeping(boolean immediately, boolean updateWorldFlag, boolean setSpawn, CallbackInfo ci) {
        EntityPlayer self = (EntityPlayer) (Object) this;
        if (!(self.world instanceof WorldServer)) return;
        BlockPos bedPos = self.bedLocation;

        if (bedPos != null) BedOccupancyTracker.updateBedOccupancy((WorldServer) self.world, bedPos, self.getEntityId());
    }
}
