package ru.nukkit.permiter.data;

import ru.nukkit.permiter.permissions.Group;
import ru.nukkit.permiter.permissions.User;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Igor on 17.04.2016.
 */
public interface DataSource {

    void saveUser(User user);

    User loadUser(String playerName);

   // boolean isStored(String userName, String worldName);

    void saveGroups(Collection<Group> all);

    Map<String, Group> loadGroups();



}
