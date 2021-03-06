package dev.aura.bungeechat.command;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.MessengerModule;
import dev.aura.bungeechat.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;

public class MessageCommand extends BaseCommand {
    public MessageCommand(MessengerModule messengerModule) {
        super("msg", messengerModule.getModuleSection().getStringList("aliases.message"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PermissionManager.hasPermission(sender, Permission.COMMAND_MESSAGE)) {
            if (args.length < 2) {
                MessagesService.sendMessage(sender, Message.INCORRECT_USAGE.get(sender, "/msg <player> <message>"));
            } else {
                Optional<BungeeChatAccount> targetAccount = AccountManager.getAccount(args[0]);

                if (!targetAccount.isPresent() || (targetAccount.get().isVanished()
                        && !PermissionManager.hasPermission(sender, Permission.COMMAND_VANISH_VIEW))) {
                    MessagesService.sendMessage(sender, Message.PLAYER_NOT_FOUND.get());
                    return;
                }

                CommandSender target = BungeecordAccountManager.getCommandSender(targetAccount.get()).get();

                if (target == sender) {
                    MessagesService.sendMessage(sender, Message.MESSAGE_YOURSELF.get());
                    return;
                }
                if (!targetAccount.get().hasMessangerEnabled()
                        && !PermissionManager.hasPermission(sender, Permission.BYPASS_TOGGLE_MESSAGE)) {
                    MessagesService.sendMessage(sender, Message.HAS_MESSAGER_DISABLED.get(target));
                    return;
                }

                String finalMessage = Arrays.stream(args, 1, args.length).collect(Collectors.joining(" "));

                MessagesService.sendPrivateMessage(sender, target, finalMessage);
                ReplyCommand.setReply(sender, target);
            }
        }
    }
}
