import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.text.DecimalFormat;

@ScriptManifest(name = "Feather Plucker", author = "Mascomorg", version = 1.0, info = "", logo = "")

public class FeatherPlucker extends Script {

    //Skill Variables
    private static final Skill attack = Skill.ATTACK;
    private static final Skill ranged = Skill.RANGED;
    private static final Skill defence = Skill.DEFENCE;
    private static final Skill health = Skill.HITPOINTS;
    private static final Skill strength = Skill.STRENGTH;
    private static final Skill prayer = Skill.PRAYER;

    //Paint variables
    private long startTime;
    private int killCount;
    private int featherCount;

    private NPC chicken = null;


    @Override
    public void onStart() {
        //Logging the start of the script
        log("Starting Feather Plucker...");

        //Setting camera position to the top of the screen
        camera.toTop();

        //Initializing paint variables
        startTime = System.currentTimeMillis();
        killCount = 0;
        featherCount = 0;

        //Initializing experience trackers
        getExperienceTracker().start(attack);
        getExperienceTracker().start(strength);
        getExperienceTracker().start(defence);
        getExperienceTracker().start(health);
        getExperienceTracker().start(ranged);
        getExperienceTracker().start(prayer);
    }

    @Override
    public void onExit() {
        log("Thank-you for using Feather Plucker!");
        this.stop();
    }

    @Override
    public int onLoop() throws InterruptedException {
        //Check if player is within the combat area
        if(myPlayer().getPosition().distance(new Position(3230,3297,0)) < 15) {
            //Check if player is in combat, if not, look for a fight, else wait
            if(myPlayer().getAnimation() == -1 && !myPlayer().isUnderAttack()) {
                log("Looking for a chicken to fight.");
                if(chicken == null) {
                    chicken = getNpcs().closest(npc -> npc.getName().equals("Chicken") && npc.getInteracting() == null
                            && !npc.isUnderAttack() && npc.getHealthPercent() != 0 && map.canReach(npc));
                    if(chicken != null) {
                        if(!chicken.isVisible()) {
                            log("Facing target.");
                            camera.toEntity(chicken);
                        }
                        log("Attacking chicken.");
                        chicken.interact("Attack");
                        mouse.moveOutsideScreen();
                        sleep(random(500, 2000));
                    } else {
                        return random(1500, 2500);
                    }
                } else {
                    return 100;
                }

            } else {
                Timing.waitCondition(()-> !chicken.exists(), 100, 1500);
                if(!chicken.exists()) {
                    killCount++;
                }
            }
        } else {
            log("Walking to the combat area.");
            walking.walk(new Position(3230, 3297, 0));
        }
        return 100; //The amount of time in milliseconds before the loop starts over
    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setColor(new Color(0,0,0));
        g.fillRoundRect(7,344,507,130,0,0);
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0,250,80);

        Point mP = getMouse().getPosition();

        g.setColor(new Color(255, 255,255));
        g.drawLine(mP.x, mP.y - 15, mP.x, mP.y);
        g.drawLine(mP.x - 15, mP.y, mP.x, mP.y);
        g.drawLine(mP.x, mP.y + 15, mP.x, mP.y);
        g.drawLine(mP.x + 15, mP.y, mP.x, mP.y);

        g.setFont(new Font("Century Gothic", Font.PLAIN, 12 ));

        // Timer and Kill-Count Statistics
        g.drawString("Chicken Slaughter 1.0",60,15);
        g.drawString("Runtime : " + formatTime(System.currentTimeMillis() - startTime), 10, 40);
        g.drawString("Kill count: " + killCount +" Chickens", 10, 60);

        // Strength Statistics
        g.drawString("Strength", 10, 360);
        g.drawString("Exp Gained: " + formatExp(getExperienceTracker().getGainedXP(strength)) + " (" + formatExp(getExperienceTracker().getGainedXPPerHour(strength)) + "/Hr)", 70, 360);
        g.drawString("Level (Gained): " + skills.getStatic(strength) + " (" + getExperienceTracker().getGainedLevels(strength) + ")",246,360);
        g.drawString("Next Level: " + formatTime(getExperienceTracker().getTimeToLevel(strength)),392, 360);

        // Health Statistics
        g.drawString("Health", 10,380);
        g.drawString("Exp Gained: " + formatExp(getExperienceTracker().getGainedXP(health)) + " (" + formatExp(getExperienceTracker().getGainedXPPerHour(health)) + "/Hr)", 70, 380);
        g.drawString("Level (Gained): " + skills.getStatic(health) + " (" + getExperienceTracker().getGainedLevels(health) + ")",246,380);
        g.drawString("Next Level: " + formatTime(getExperienceTracker().getTimeToLevel(health)),392, 380);

    }

    /**
     * This method is responsible for formatting experience gained into a neat, legible format.
     * @param experience
     * @return experience with legible formatting
     */
    public String formatExp(long experience) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(experience);

    }

    /**
     * This method is responsible for formatting run-time in a neat, legible format.
     * @param ms
     * @return run-time with legible formatting
     */
    public String formatTime(long ms){
        long s = ms / 1000;
        long m = s / 60;
        long h = m / 60;
        s %= 60; m %= 60; h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

}