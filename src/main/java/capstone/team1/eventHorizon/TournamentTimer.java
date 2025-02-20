package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;


public class TournamentTimer extends BukkitRunnable {
    private final int totalTime = 3600; // Total time of  1hr in secs
    private int remainingTime;
    private final EventHorizon plugin;

    public TournamentTimer(EventHorizon plugin) {
        this.plugin = plugin;
        this.remainingTime = totalTime;
    }
  
    @Override
    public void run() {
        //Stops timer is the remaining is less than 0
      if(remainingTime <= 0){
          Bukkit.broadcastMessage(ChatColor.RED + "Tournament is over!");
          this.cancel();
          return;
      }
      //Display timer on action bar
      for (Player player : Bukkit.getOnlinePlayers()){
          player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                  ChatColor.YELLOW + "Tournament Time: " + ChatColor.AQUA + formatTime(remainingTime)));
      }
      //Display in chat every min or last 10 mins
        if(remainingTime % 60 ==0 || remainingTime <=10){
            Bukkit.broadcastMessage(ChatColor.RED + "[Tournament] " + ChatColor.AQUA + "Time Remaining: " + formatTime(remainingTime));
        }
        remainingTime--;
    }
//Format for timer
private String formatTime(int seconds){
    int hours = seconds / 3600;
    int minutes = (seconds % 3600) / 60;
    int sec = seconds % 60;
    return String.format("%02d:%02d:%02d", hours, minutes, sec);
}

    public void startTimer() {
        this.runTaskTimer(plugin, 0L, 20L);
        //Tournament will count down every second
    }

}
