package sircow.roomfortwo.mixin;

import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBed.class)
public class BlockBedMixin {
    @Redirect(method = "onBlockActivated", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getValue(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", ordinal = 2))
    private Comparable<?> roomfortwo$use(IBlockState state, IProperty<?> property) {
        return Boolean.FALSE;
    }
}
