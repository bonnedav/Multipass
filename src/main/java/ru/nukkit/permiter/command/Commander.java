package ru.nukkit.permiter.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.permiter.command.group.*;
import ru.nukkit.permiter.command.perm.PermCheck;
import ru.nukkit.permiter.command.perm.PermHelp;
import ru.nukkit.permiter.command.perm.PermReload;
import ru.nukkit.permiter.command.user.*;
import ru.nukkit.permiter.util.Message;
import ru.nukkit.permiter.util.Paginator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commander {
    private static List<Cmd> commands = new ArrayList<Cmd>();
    private static PluginBase plugin;

    public static void init(PluginBase plg) {
        plugin = plg;

        // Group command
        addNewCommand(GroupInfo.class);
        addNewCommand(GroupAddGroup.class);
        addNewCommand(GroupCreate.class);
        addNewCommand(GroupRemove.class);
        addNewCommand(GroupRemovePerm.class);
        addNewCommand(GroupSetPerm.class);
        addNewCommand(GroupSetGroup.class);
        addNewCommand(GroupSetPrefix.class);
        addNewCommand(GroupSetSuffix.class);

        //Perm command
        addNewCommand(PermHelp.class);
        addNewCommand(PermReload.class);
        addNewCommand(PermCheck.class);

        //User command
        addNewCommand(UserAddgroup.class);
        addNewCommand(UserInfo.class);
        addNewCommand(UserRemovegroup.class);
        addNewCommand(UserRemoveperm.class);
        addNewCommand(UserSetgroup.class);
        addNewCommand(UserSetperm.class);

    }


    public static PluginBase getPlugin() {
        return plugin;
    }

    private static boolean isRegistered(String cmdStr) {
        for (Cmd cmd : commands)
            if (cmd.getCommand().equalsIgnoreCase(cmdStr)) return true;
        return false;
    }

    public static boolean addNewCommand(Class<? extends Cmd> cmd) {
        try {
            return addNewCommand(cmd.newInstance(), null);
        } catch (Exception e) {
            Message.debugMessage("Failed to register command: ", cmd.getName());
            return false;
        }
    }

    public static boolean addNewCommand(Class<? extends Cmd> cmd, Message description) {
        try {
            return addNewCommand(cmd.newInstance(), description);
        } catch (Exception e) {
            Message.debugMessage("Failed to register command: ", cmd.getName());
            return false;
        }
    }


    public static boolean addNewCommand(Cmd cmd) {
        return addNewCommand(cmd, null);
    }

    public static boolean addNewCommand(Cmd cmd, Message description) {
        if (cmd.getCommand() == null) return false;
        if (cmd.getCommand().isEmpty()) return false;
        if (!isRegistered(cmd.getCommand())) {
            CommandExecutor newCmd = new CommandExecutor(cmd.getCommand());
            newCmd.setDescription(description == null ? cmd.getDescription() : description.getText("NOCOLOR"));
            newCmd.setAliases(cmd.getAliases());
            plugin.getServer().getCommandMap().register(plugin.getName() + "_cmd", newCmd);
            Message.CMD_REGISTERED.debug(cmd.toString());
        }
        commands.add(cmd);
        return true;
    }

    public static boolean isPluginYml(String cmdStr) {
        return plugin.getDescription().getCommands().containsKey(cmdStr);
    }

    public static void printHelp(CommandSender sender, int page) {
        List<String> helpList = new ArrayList<String>();
        for (Cmd cmd : commands) {
            helpList.add(TextFormat.GREEN + cmd.getHelpString());
        }
        int pageHeight = (sender instanceof Player) ? 9 : 1000;
        Paginator.printPage(sender, helpList, Message.HLP_TITLE.getText('e', '6', (plugin.getName() + " " + plugin.getDescription().getVersion()))
                , "[%1% / %2%]", "No help", page, pageHeight, false);
    }

    public static String unsplit(String[] args) {
        return unsplit(args, 0);
    }

    public static String unsplit(String[] args, int num) {
        if (args.length <= num) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = num; i < args.length; i++) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(args[i]);
        }
        return sb.toString();
    }

    public static Player getPlayer(CommandSender sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

    public static boolean execute(CommandSender sender, String cmdLabel, String[] args) {
        for (Cmd cmd : commands) {
            if (!cmd.isCommand(cmdLabel)) continue;
            if (cmd.executeCommand(sender, args))
                return Message.debugMessage("Executed command:", sender.getName(), cmdLabel, cmd.toString(), new ArrayList<String>(Arrays.asList(args)).toString());
        }
        Message.debugMessage("Command not executed:", sender.getName(), cmdLabel, new ArrayList<String>(Arrays.asList(args)).toString());
        return Message.CMD_FAILED.print(sender, "/" + cmdLabel + " help");
    }

    public static String getCommandByAlias(String cmdLabel) {
        for (Cmd cmd : commands)
            if (cmd.isCommand(cmdLabel)) return cmd.getCommand();
        return null;
    }

    public static boolean isPluginCommand(String cmdLabel) {
        return getCommandByAlias(cmdLabel) != null;
    }
}
