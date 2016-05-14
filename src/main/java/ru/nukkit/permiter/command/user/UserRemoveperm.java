package ru.nukkit.permiter.command.user;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.permiter.command.Cmd;
import ru.nukkit.permiter.command.CmdDefine;
import ru.nukkit.permiter.permissions.Users;
import ru.nukkit.permiter.util.Message;

/**
 * Created by Igor on 06.05.2016.
 */

@CmdDefine(command = "user", alias = "userperm", allowConsole = true, subCommands = {"\\S+", "removeperm|rmvperm|rperm|rp", "\\S+"}, permission = "permiter.admin", description = Message.CMD_USER_REMOVEPERM)
public class UserRemoveperm extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        String userName = args[0];
        if (!Users.isRegistered(userName)) return Message.USER_REMOVEPERM_NOTFOUND.print(sender, userName);
        String permStr = args[2];
        if (!Users.isPermissionSet(userName, permStr))
            return Message.USER_REMOVEPERM_NOTSET.print(sender, permStr, userName);
        Users.removePermission(userName, permStr);
        return Message.USER_REMOVEPERM_OK.print(sender, permStr, userName);
    }
}