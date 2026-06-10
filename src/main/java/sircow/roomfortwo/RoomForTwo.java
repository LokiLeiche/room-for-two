package sircow.roomfortwo;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sircow.roomfortwo.client.EntityViewRenderHandler;
import sircow.roomfortwo.network.RoomForTwoNetwork;
import sircow.roomfortwo.roomfortwo.Tags;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class RoomForTwo {
    public static final Logger LOG = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Initialising {}", Tags.MOD_NAME);
        RoomForTwoNetwork.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new EntityViewRenderHandler());
        }
    }
}