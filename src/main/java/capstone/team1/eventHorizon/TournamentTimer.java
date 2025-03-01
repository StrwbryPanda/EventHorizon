package capstone.team1.eventHorizon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;


//class for the tournament timer that runs from the beginning to the end of the tournament
public class TournamentTimer extends BukkitRunnable {
    public static boolean isRunning;
    public static int totalTime;
    public static int remainingTime;
    private final EventHorizon plugin;
    static String timeDisplayed;

    public TournamentTimer(EventHorizon plugin) {
        this.plugin = plugin;
        this.remainingTime = totalTime;
    }
  
    @Override
    public void run() {
        //stops timer is the remaining is less than 0
      if(remainingTime < 0){
          Bukkit.broadcastMessage(ChatColor.RED + "Tournament is over");
          this.cancel();
          return;
      }

      //Display timer on action bar
      for (Player player : Bukkit.getOnlinePlayers()){
          timeDisplayed = formatTime(remainingTime);
          //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Tournament Time: " + ChatColor.AQUA + timeDisplayed));
      }
/*
      //display in chat every min or last 10 mins
        if(remainingTime % 60 ==0 || remainingTime <=10){
            Bukkit.broadcastMessage(ChatColor.RED + "[Tournament] " + ChatColor.AQUA + "Time Remaining: " + formatTime(remainingTime));
        }

 */
        remainingTime--;
    }



//format for timer
public static String formatTime(int seconds){
    int hours = seconds / 3600;
    int minutes = (seconds % 3600) / 60;
    int sec = seconds % 60;
    return String.format("%02d:%02d:%02d", hours, minutes, sec);
}

    public void startTimer() {
        //tournament will count down every second
        this.runTaskTimer(plugin, 0L, 20L);
        //Tournament will count down every second

        EventFrequencyTimer eventFrequencyTimer = new EventFrequencyTimer(plugin);
        eventFrequencyTimer.startTimer();
    }

}
