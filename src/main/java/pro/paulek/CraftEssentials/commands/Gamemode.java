package pro.paulek.CraftEssentials.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pro.paulek.CraftEssentials.ICraftEssentials;

import java.util.*;

@CommandAlias("gamemode|gm")
@CommandPermission("craftessentials.gamemode")
public class Gamemode extends BaseCommand {

    private Map<String, GameMode> legacyGamemodes = ImmutableMap.<String, GameMode>builder()
            .put("0", GameMode.SURVIVAL)
            .put("1", GameMode.CREATIVE)
            .put("2", GameMode.ADVENTURE)
            .put("3", GameMode.SPECTATOR)
            .put("sur", GameMode.SURVIVAL)
            .put("c", GameMode.CREATIVE)
            .put("a", GameMode.ADVENTURE)
            .put("spec", GameMode.SPECTATOR)
            .build();

    private ICraftEssentials plugin;

    public Gamemode(ICraftEssentials craftEssentials) {
        this.plugin = Objects.requireNonNull(craftEssentials);
    }

    @Default
    @Syntax("[creative/spectator/adventure/survival] <+player>")
    @Description("Changes gamemode for players")
    @CommandCompletion("@gamemodes @players")
    public void playerCommand(CommandSender commandSender, String[] args) {
        if(args.length < 1) {
            this.showCommandHelp();
            return;
        }
        GameMode targetGamemode = legacyGamemodes.getOrDefault(args[0], null);
        if(targetGamemode == null) {
            for(GameMode mode : GameMode.values()) {
                if(mode.name().equalsIgnoreCase(args[0])) {
                    targetGamemode = mode;
                    break;
                }
            }
        }
        if(targetGamemode == null) {
            for(GameMode mode : GameMode.values()) {
                if(mode.name().contains(args[0].toUpperCase())) {
                    targetGamemode = mode;
                    break;
                }
            }
        }
        if(targetGamemode == null) {
            this.showCommandHelp();
            return;
        }
        if(!commandSender.hasPermission("craftessentials.gamemode." + targetGamemode.name())) {
            commandSender.sendMessage("No permissions!");
            return;
        }
        Player player = null;
        if(commandSender instanceof Player) {
            player = (Player) commandSender;
        }
        Locale locale = plugin.getUser(player.getUniqueId()).getLocale();
        if(args.length == 1 && player != null) {
            player.setGameMode(targetGamemode);
            player.sendMessage(plugin.getI18n().format("gamemodeChanged", locale, plugin.getI18n().translate(targetGamemode.name(), locale)));
            return;
        }
        if(args.length < 2 && player == null) {
            this.showCommandHelp();
            return;
        }
        if(!commandSender.hasPermission("craftessentials.gamemode.others")) {
            commandSender.sendMessage("No permissions!");
            return;
        }
        if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
            if(!commandSender.hasPermission("craftessentials.gamemode.all")) {
                commandSender.sendMessage("No permissions!");
                return;
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setGameMode(targetGamemode);
                p.sendMessage("Your gamemode has changed to " + targetGamemode.name());
            }
            player.sendMessage("Gamemode changed.");
            return;
        }
        List<String> players = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
        for(String playerNick : players) {
            Player player1 = Bukkit.getPlayer(playerNick);
            if(player1 == null) {
                player.sendMessage("Target player " + playerNick + " is offline and gamemode could not be changed.");
                continue;
            }
            player1.setGameMode(targetGamemode);
            Locale locale1 = plugin.getUser(player1.getUniqueId()).getLocale();
            player1.sendMessage(plugin.getI18n().format("gamemodeChanged", locale1, plugin.getI18n().translate(targetGamemode.name(), locale1)));
        }
        player.sendMessage("Gamemode changed.");
    }

}
