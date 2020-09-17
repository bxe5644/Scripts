package scripts;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import scripts.utility.GUI;
import scripts.utility.Timing;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

@ScriptManifest(name = "Overfished", author = "Presumptuous", version = 1.0, info = "", logo = "")

public class Overfished extends Script {

    private GUI gui;

    //Script options
    private String location;
    private String style;
    private boolean banking;

    //Current state of script
    private String state;

    //GUI statistics
    private long startTime;
    private int fishCount;

    @Override
    public void onStart() throws InterruptedException {
        try {
            SwingUtilities.invokeAndWait(()-> {
                gui = new GUI();
                gui.open();
            });
        } catch (InvocationTargetException e) {
            log(e.getMessage());
            this.stop();
            return;
        }
        this.fishCount = 0;
        this.startTime = Timing.currentMs();
        this.location = gui.getFishingLocation();
        this.style = gui.getFishingStyle();
        this.banking = gui.isBanking();
        this.state = "Check tool(s)";
        this.getExperienceTracker().start(Skill.FISHING);
        this.camera.toTop();
    }

    @Override
    public int onLoop() throws InterruptedException {

        switch(this.state) {
            case "Check tool(s)":
                if(checkTools()) {
                    log("Fishing gear is present");
                } else {
                    log("Fishing gear is not present, ending script");
                    this.stop();
                }
                this.state = "Walk to location";
                break;

            case "Walk to location":
                checkLocation();
                log("Walking to " + this.location);
                break;
        }
        return 1000;
    }

    public void checkLocation() {
        Position barbarianVillage;
        Position draynor;
        Position lumbridgeSwamp = new Position(3240,3153,0);
        Position karamja;

        if(myPlayer().getPosition().distance(lumbridgeSwamp) > 10) {
            getWalking().webWalk(lumbridgeSwamp);
        }
    }

    public boolean checkTools() {
        log(style);
        if(this.style.equals("Bait")) {
            return inventory.contains("Fishing rod", "Bait");
        } else if(this.style.equals("Fly")) {
            return inventory.contains("Fly fishing rod", "Feathers");
        } else if(this.style.equals("Net")) {
            return inventory.contains("Small fishing net");
        } else if(this.style.equals("Harpoon") ) {
            return inventory.contains("Harpoon");
        } else if(this.style.equals("Cage")) {
            return inventory.contains("Lobster pot");
        } else {
            return false;
        }
    }

    @Override
    public void onExit() {
        this.stop();
    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setColor(new Color(0,0,0));
        g.fillRoundRect(7,344,507,130,0,0);
        g.setColor(new Color(255, 255,255));


        // Mouse Position Graphics
        Point mouse = getMouse().getPosition();
        g.setColor(new Color(255, 255,255));
        g.drawLine(mouse.x, mouse.y - 15, mouse.x, mouse.y);
        g.drawLine(mouse.x - 15, mouse.y, mouse.x, mouse.y);
        g.drawLine(mouse.x, mouse.y + 15, mouse.x, mouse.y);
        g.drawLine(mouse.x + 15, mouse.y, mouse.x, mouse.y);
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
}