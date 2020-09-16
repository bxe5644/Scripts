import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

@ScriptManifest(name = "Overfished", author = "Presumptuous", version = 1.0, info = "", logo = "")

public class Overfished extends Script {

    // Skill Variables
    private static final Skill fishing = Skill.FISHING;

    // Paint variables
    private long startTime;
    private int fishCount;

    private GUI gui = new GUI();

    @Override
    public void onStart() throws InterruptedException {
        try {
            SwingUtilities.invokeAndWait(()-> {
                gui = new GUI();
                gui.open();
            });
        } catch (InvocationTargetException e) {
            log(e.getMessage());
        }

        // Setting camera position to the top of the screen
        camera.toTop();

        // Initializing paint variables
        startTime = System.currentTimeMillis();

        // Initializing fish count
        fishCount = 0;

        // Initializing experience tracker
        getExperienceTracker().start(fishing);
    }

    @Override
    public void onExit() {
        log("Thank-you for using Overfished!");
        log("You've caught over " + fishCount + " fish.");
        this.stop();
    }

    @Override
    public int onLoop() throws InterruptedException {

        return 1000;
    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setColor(new Color(0,0,0));
        g.fillRoundRect(7,344,507,130,0,0);
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0,250,80);

        // Creating a cross to visualize mouse movement
        Point mP = getMouse().getPosition();

        g.setColor(new Color(255, 255,255));
        g.drawLine(mP.x, mP.y - 15, mP.x, mP.y);
        g.drawLine(mP.x - 15, mP.y, mP.x, mP.y);
        g.drawLine(mP.x, mP.y + 15, mP.x, mP.y);
        g.drawLine(mP.x + 15, mP.y, mP.x, mP.y);

        // Setting font styles for paint
        g.setFont(new Font("Century Gothic", Font.PLAIN, 12 ));

        // Timer and Fish-Count Statistics
        g.drawString("Overfished 1.0",60,15);
        g.drawString("Runtime : " + formatTime(System.currentTimeMillis() - startTime), 10, 40);
        g.drawString("Fish caught: " + fishCount, 10, 60);
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