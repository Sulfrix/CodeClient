package dev.dfonline.codeclient.dev;

import dev.dfonline.codeclient.CodeClient;
import dev.dfonline.codeclient.Feature;
import dev.dfonline.codeclient.config.Config;
import dev.dfonline.codeclient.location.Dev;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class Navigation extends Feature {
    public boolean onJump() {
        if (InteractionManager.shouldTeleportUp()) {
            Vec3d move = CodeClient.MC.player.getPos().add(0, 5, 0);
            if ((!NoClip.isIgnoringWalls()) && NoClip.isInsideWall(move)) move = move.add(0, 2, 0);
            CodeClient.MC.player.setPosition(move);
            return true;
        }
        return false;
    }

    public boolean onCrouch(boolean lastSneaking) {
        var player = CodeClient.MC.player;
        if (CodeClient.location instanceof Dev dev
                && Config.getConfig().TeleportDown
                && player != null
                && dev.isInDev(player.getPos())
                && player.getY() % 5 == 0
                && !lastSneaking && player.isSneaking()
                && (player.getPitch() >= 90 - Config.getConfig().DownAngle)
        ) {
            Vec3d move = CodeClient.MC.player.getPos().add(0, -5, 0);
            if (move.y < dev.getFloorY()) move = new Vec3d(move.x, dev.getFloorY(), move.z);
            if ((!NoClip.isIgnoringWalls()) && NoClip.isInsideWall(move)) move = move.add(0, 2, 0);
            CodeClient.MC.player.setPosition(move);
            return true;
        }
        return false;
    }

    @Nullable
    public Float jumpHeight() {
        if (CodeClient.location instanceof Dev dev
                && dev.isInDev(CodeClient.MC.player.getPos())
                && CodeClient.MC.player.getPitch() <= Config.getConfig().UpAngle - 90)
            return 0.91f;
        return null;
    }

    @Nullable
    public Float getAirSpeed() {
        if (CodeClient.location instanceof Dev dev && dev.isInDevSpace()) {
            var config = Config.getConfig().AirSpeed;
            if(config == 10) return null;
            return 0.026F * (CodeClient.MC.player.getMovementSpeed() * config);
        }
        return null;
    }
}
