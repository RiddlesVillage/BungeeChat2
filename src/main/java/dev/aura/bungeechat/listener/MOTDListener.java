package dev.aura.bungeechat.listener;

import java.util.List;
import java.util.stream.Collectors;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class MOTDListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        BungeeChatAccount bungeeChatAccount = BungeecordAccountManager.getAccount(player).get();
        List<String> motd = BungeecordModuleManager.MOTD_MODULE.getModuleSection().getStringList("message");

        String message = motd.stream().collect(Collectors.joining("\n"));
        MessagesService.sendMessage(player,
                PlaceHolderUtil.formatMessage(message, new BungeeChatContext(bungeeChatAccount)));
    }
}