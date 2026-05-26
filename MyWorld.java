import greenfoot.*;

public class MyWorld extends World {
    private boolean playerChosen;

    public MyWorld() {
        super(600, 400, 1);
        showTitleScreen();
    }

    public void act() {
        if (playerChosen) {
            return;
        }

        String key = Greenfoot.getKey();

        if (key == null) {
            return;
        }

        if (key.equalsIgnoreCase("k")) {
            spawnKaine();
        } else if (key.equalsIgnoreCase("l")) {
            spawnLeon();
        }
    }

    private void showTitleScreen() {
        showText("titlescreen", 300, 130);
        showText("Press K for Kaine", 300, 170);
        showText("Press L for Leon", 300, 200);
        showText("Click the world, then press a key", 300, 230);
    }

    private void clearTitleScreen() {
        showText("", 300, 130);
        showText("", 300, 170);
        showText("", 300, 200);
        showText("", 300, 230);
        showText("", 300, 305);
        showText("", 300, 330);
        showText("", 300, 355);
    }

    private void spawnKaine() {
        KaineVelsarth kaine = new KaineVelsarth();
        addObject(kaine, 300, 170);
        playerChosen = true;
        clearTitleScreen();
        showText(kaine.getStartingLoadoutText(), 300, 305);
        showText(kaine.getInventoryText(), 300, 330);
        showText(kaine.getEquippedText(), 300, 355);
    }

    private void spawnLeon() {
        LeonClovis leon = new LeonClovis();
        addObject(leon, 300, 170);
        playerChosen = true;
        clearTitleScreen();
        showText("", 300, 355);
    }
}
