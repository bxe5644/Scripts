package scripts.utility;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import scripts.Overfished;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class Paint extends Overfished {

    //Skill Constants
    private final Skill ATTACK = Skill.ATTACK;
    private final Skill STRENGTH = Skill.STRENGTH;
    private final Skill DEFENCE = Skill.DEFENCE;
    private final Skill HITPOINTS = Skill.HITPOINTS;
    private final Skill WOODCUTTING = Skill.WOODCUTTING;
    private final Skill FIREMAKING = Skill.FIREMAKING;
    private final Skill MINING = Skill.MINING;
    private final Skill SMITHING = Skill.SMITHING;
    private final Skill FISHING = Skill.FISHING;
    private final Skill COOKING = Skill.COOKING;

    //Item Counts
    private int fishCount = 0;
    private int logCount = 0;
    private int oreCount = 0;
    private int foodCount = 0;
    private int burnedCount = 0;

    //Timer
    private long startTime;

    //Skill(s) to Track
    ExperienceTracker experienceTracker;

    private Graphics2D g;

    //Cursor
    private Image cursor = new ImageIcon("cursor.gif").getImage();

    public Paint(Graphics2D g) {
        this.g = g;
        this.startTime = Timing.currentMs();

        getExperienceTracker().start(Skill.ATTACK);
        getExperienceTracker().start(Skill.STRENGTH);
        getExperienceTracker().start(Skill.DEFENCE);
        getExperienceTracker().start(Skill.HITPOINTS);
        getExperienceTracker().start(Skill.WOODCUTTING);
        getExperienceTracker().start(Skill.FIREMAKING);
        getExperienceTracker().start(Skill.MINING);
        getExperienceTracker().start(Skill.SMITHING);
        getExperienceTracker().start(Skill.FISHING);
        getExperienceTracker().start(COOKING);
    }

    public Graphics2D overfishedPaint(String... count) {
        g.setColor(new Color(0,0,0));
        g.fillRoundRect(7,344,507,130,0,0);
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0,250,80);

        // Setting font styles for paint
        g.setFont(new Font("Century Gothic", Font.PLAIN, 12 ));

        // Timer and Fish-Count Statistics
        g.drawString("scripts.Overfished 1.0",60,15);
        g.drawString("Runtime : " + formatTime(System.currentTimeMillis() - startTime), 10, 40);
        g.drawString("Fish caught: " + fishCount, 10, 60);

        Point mouse = getMouse().getPosition();
        g.drawImage(cursor, mouse.x, mouse.y, null);

        return g;
    }

    public String formatExp(long experience) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(experience);
    }

    public String formatTime(long ms){
        long s = ms / 1000;
        long m = s / 60;
        long h = m / 60;
        s %= 60; m %= 60; h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public void setFishCount(int fishCount) {
        this.fishCount = fishCount;
    }

    public void setLogCount(int logCount) {
        this.logCount = logCount;
    }

    public void setOreCount(int oreCount) {
        this.oreCount = oreCount;
    }

    private void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    private void setBurnedCount(int burnedCount) {
        this.burnedCount = burnedCount;
    }
}
