package cn.balalals.liveboost;

import cn.balalals.liveboost.websocket.WebSocketClient;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import javax.net.ssl.SSLException;
import java.util.Arrays;

public class LiveBoostCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;
    private WebSocketClient webSocketClient;

    LiveBoostCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("getName:"+command.getName());
        System.out.println("label:"+ label);
        System.out.println("args:"+ Arrays.toString(args));
        System.out.println("liveBoost命令执行");
        if(args.length >= 1) {
            String subCommand = args[0];
            System.out.println("subCommand:" + subCommand);
            if("start".equals(subCommand)) {
                try {
                    this.webSocketClient = new WebSocketClient(plugin.getConfig().getInt("roomId"));
                    webSocketClient.open();
                } catch (SSLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else if("stop".equals(subCommand)) {
                try {
                    webSocketClient.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}
