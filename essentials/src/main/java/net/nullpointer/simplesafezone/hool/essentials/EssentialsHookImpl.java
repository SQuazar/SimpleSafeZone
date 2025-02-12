package net.nullpointer.simplesafezone.hool.essentials;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import net.nullpointer.simplesafezone.hook.essentials.EssentialsHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.CompletableFuture;

import static com.earth2me.essentials.I18n.tl;

public class EssentialsHookImpl implements EssentialsHook {
    private final Essentials essentials;

    public EssentialsHookImpl() {
        this.essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }

    public void home(Player player) {
        Trade trade = new Trade("esshook", essentials);
        User user = essentials.getUser(player);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        future.thenAccept(success -> {
            if (Boolean.TRUE.equals(success))
                user.sendMessage(tl("teleportHome"));
        });
        if (user.hasHome()) {
            if (!user.getHomes().isEmpty()) {
                Location home = user.getHome(user.getHomes().get(0));
                user.getAsyncTeleport().teleport(home, trade, PlayerTeleportEvent.TeleportCause.PLUGIN, future);
            }
        } else user.getAsyncTeleport().respawn(trade, PlayerTeleportEvent.TeleportCause.PLUGIN, future);
    }
}
