package ru.nukkit.permiter.command.user;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import ru.nukkit.permiter.command.Cmd;
import ru.nukkit.permiter.command.CmdDefine;
import ru.nukkit.permiter.permissions.User;
import ru.nukkit.permiter.permissions.Users;
import ru.nukkit.permiter.util.Message;
import ru.nukkit.permiter.util.Paginator;
import ru.nukkit.permiter.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 06.05.2016.
 */
@CmdDefine(command = "user", alias = "userperm", allowConsole = true, subCommands = "\\S+", permission = "permiter.admin", description = Message.CMD_USER)
public class UserInfo extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        if (args.length != 1) return false;
        String userName = args[0];

        if (!Users.isRegistered(userName)) return Message.PERM_USER_NOTREGISTER.print(sender, userName);

        boolean online = Server.getInstance().getPlayerExact(userName) != null;

        int page = args.length == 3 && args[2].matches("\\d+") ? Integer.parseInt(args[2]) : 1;

        User user = Users.getUser(userName);
        List<String> print = new ArrayList<>();

        List<String> ln = user.getGroupList();
        if (!ln.isEmpty()) print.add(Message.PERM_USER_GROUPS.getText(StringUtil.join(ln)));
        ln = user.getPermissionList();
        if (!ln.isEmpty()) {
            print.add(Message.PERM_USER_PERMS.getText());
            for (String s : ln)
                print.add(Message.color2(s));
        }
        Paginator.printPage(sender, print, Message.PERM_USER_INFO.getText(userName), page);
        return true;
    }


}
