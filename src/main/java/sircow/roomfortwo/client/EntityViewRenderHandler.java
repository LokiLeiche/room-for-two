package sircow.roomfortwo.client;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sircow.roomfortwo.util.BedOccupancyTracker;

public class EntityViewRenderHandler {
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null) return;
        if (!player.isPlayerSleeping()) return;
        if (mc.gameSettings.thirdPersonView != 0) return;

        BlockPos bedPos = player.bedLocation;
        if (bedPos == null) return;

        IBlockState state = player.world.getBlockState(bedPos);
        if (!(state.getBlock() instanceof BlockBed)) return;

        EnumFacing direction = state.getValue(BlockHorizontal.FACING);

        float baseYaw = direction.getHorizontalAngle() - 180.0F;
        int slot = BedOccupancyTracker.getSlot(player.getEntityId());

        float pitchR = -90.0F;
        float yawR = 90.0F;
        float rollR = -90.0F;

        float pitchL = -pitchR;
        float yawL = -yawR;

        float targetPitch, targetYaw, targetRoll;

        if (slot % 2 == 0) {
            targetPitch = pitchR;
            targetYaw = baseYaw + yawR;
            GlStateManager.translate(-0.75F, -0.25F, 0.0F);
        }
        else {
            targetPitch = pitchL;
            targetYaw = baseYaw + yawL;
            GlStateManager.translate(0.75F, 0.25F, 0.0F);
        }
        targetRoll = rollR;

        targetYaw = (targetYaw % 360.0F + 360.0F) % 360.0F;

        player.rotationYaw = targetYaw;
        player.prevRotationYaw = targetYaw;
        player.rotationPitch = targetPitch;
        player.prevRotationPitch = targetPitch;

        player.rotationYawHead = targetYaw;
        player.prevRotationYawHead = targetYaw;
        player.renderYawOffset = targetYaw;
        player.prevRenderYawOffset = targetYaw;

        event.setPitch(targetPitch);
        event.setYaw(targetYaw);
        event.setRoll(targetRoll);

        GlStateManager.rotate(targetRoll, 1.0F, 0.0F, 0.0F);
    }
}
