package scripts;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Message;
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

    //Constants
    private final String SHRIMP = "Raw shrimps";
    private final String ANCHOVIES = "Raw anchovies";

    private GUI gui;

    //Script options
    private String location;
    private String style;
    private boolean banking = false;

    //GUI statistics
    private long startTime;
    private int fishCount;

    //Current fishing spot
    NPC fishingSpot;

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
        this.getExperienceTracker().start(Skill.FISHING);
        this.camera.toTop();
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        if(message.getMessage().contains("You catch")) {
            fishCount += 1;
        }
    }

    @Override
    public void onExit() {
        this.stop();
    }

    @Override
    public int onLoop() throws InterruptedException {
        if(!checkTools()) {
            log("Fishing gear is not present, ending script");
            this.stop();
        }
        if(!inventory.isFull()) {
            walkToFishingSpot();
            fish();
        } else {
            Thread.sleep(random(0, 9846));
            emptyInventory();
        }
        return 1000;
    }

    public boolean checkTools() {
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

    public void walkToFishingSpot() {
        Position barbarianVillage;
        Position draynor;
        Position lumbridge = new Position(3240, 3153, 0);
        Position karamja;

        if (this.location.equals("Lumbridge Swamp") && myPlayer().getPosition().distance(lumbridge) > 10) {
            getWalking().webWalk(lumbridge);
            log("Walking to " + this.location);
        }
    }

    public void fish() throws InterruptedException {
        if(dialogues.isPendingContinuation()) {
            dialogues.clickContinue();
        }
        if(!myPlayer().isAnimating()) {
            fishingSpot = getNpcs().closest("Fishing spot");
            if (!fishingSpot.exists()) {
                fishingSpot = getNpcs().closest("Fishing spot");
            }
            if (!myPlayer().isAnimating()) {
                Thread.sleep(random(1, 4));
                fishingSpot.interact("Net");
                Timing.waitCondition(() -> myPlayer().isAnimating(), 200, 5000);
                mouse.moveOutsideScreen();
            }
        } else {
            antiBan();
        }
    }

    public void emptyInventory() throws InterruptedException {
        if (!banking) {
            inventory.dropAll(SHRIMP, ANCHOVIES);
            while (inventory.contains(SHRIMP, ANCHOVIES)) {
                inventory.dropAll(SHRIMP, ANCHOVIES);
            }
        } else if(Banks.LUMBRIDGE_UPPER.contains(myPosition())) {
            if (bank.isOpen()) {
                for(int i = 0; i < 28; i++) {
                    if(inventory.getItemInSlot(i) != null && !inventory.getItemInSlot(i).getName().equals("Small fishing net")) {
                        inventory.getItemInSlot(i).interact("Deposit-All");
                        Thread.sleep(500);
                    }
                }
            } else {
                bank.open();
            }
        } else {
                getWalking().webWalk(Banks.LUMBRIDGE_UPPER);
        }
    }

    public void antiBan() {
        if(Math.random() > .9) {
            camera.moveYaw(random(5,36));
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setColor(new Color(84,150,255));
        g.fillRoundRect(7,344,507,130,0,0);

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