import greenfoot.*;

public class KaineCompanionSword extends Actor
{
    private static final String FIRE_SWORD_SOUND = "fire sword sound effect.mp3";
    private static final String FUTURISTIC_SWORD_SOUND = "Ufo sound.mp3";
    private static final String LIGHTNING_SWORD_SOUND = "lightning sound.mp3";

    public double worldOffX = 22; // Offset from player
    public double worldOffY = -6;

    int activeSwordIndex = 0;
    boolean swordRightMode = false;

    GreenfootImage[] swordImages;

    public KaineCompanionSword()
    {
        swordImages = new GreenfootImage[3];
        loadSwordImage(0, false);
    }

    public void act()
    {
        if(getWorld() == null) return;

        handleInput();

        // Follow player on screen
        GameWorld gw = (GameWorld)getWorld();
        int px = gw.screenCX;
        int py = gw.screenCY;
        setLocation(px + (int)worldOffX, py + (int)worldOffY);
    }

    private void handleInput()
    {
        String key = Greenfoot.getKey();
        if(key == null) return;

        if(key.equalsIgnoreCase("space"))
        {
            activeSwordIndex = (activeSwordIndex + 1) % 3;
            swordRightMode = false;
            loadSwordImage(activeSwordIndex, swordRightMode);
        }
        else if(key.equalsIgnoreCase("tab"))
        {
            swordRightMode = !swordRightMode;
            loadSwordImage(activeSwordIndex, swordRightMode);
        }
    }

    private void loadSwordImage(int index, boolean rightMode)
    {
        GreenfootImage img;
        if(index == 0)
        {
            img = rightMode
                ? new GreenfootImage("FireSword.90 right.png")
                : new GreenfootImage("10.png");
        }
        else if(index == 1)
        {
            img = rightMode
                ? new GreenfootImage("Futuristic sword. 90 right.png")
                : new GreenfootImage("20.png");
        }
        else
        {
            img = rightMode
                ? new GreenfootImage("Lightning sword. 90 Right.png")
                : new GreenfootImage("00.png");
        }
        if(img.getWidth() > 0)
        {
            img.scale(rightMode ? 40 : 20, 40);
            if(!rightMode && index == 0) img.rotate(45);
            setImage(img);
        }
    }

    public void playActiveSwordSound()
    {
        if(activeSwordIndex == 0)
        {
            Greenfoot.playSound(FIRE_SWORD_SOUND);
        }
        else if(activeSwordIndex == 1)
        {
            Greenfoot.playSound(FUTURISTIC_SWORD_SOUND);
        }
        else
        {
            Greenfoot.playSound(LIGHTNING_SWORD_SOUND);
        }
    }
}
