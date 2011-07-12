package com.herocraftonline.dthielke.heroblocks;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

public class PermissionManager {

    public enum Permission {
        RELOAD("reload"),
        REMOVE("remove"),
        CREATE("create"),
        INSPECT("inspect");

        private final String string;

        private Permission(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    private PermissionHandler security;

    public PermissionManager() {
        this.security = null;
    }
    
    public PermissionManager(PermissionHandler security) {
        this.security = security;
    }

    public boolean hasPermission(Player player, Permission permission) {
        if (security != null) {
            return security.has(player, "heroblocks." + permission);
        } else {
            return player.isOp();
        }
    }

}
