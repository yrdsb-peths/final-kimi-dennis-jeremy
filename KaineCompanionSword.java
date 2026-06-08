import greenfoot.*;

public class KaineCompanionSword extends Actor
{
    private static final int SWORD_WIDTH = 40;
    private static final int SWORD_RIGHT_WIDTH = 80;
    private static final int SWORD_HEIGHT = 80;
    private static final int HIT_COOLDOWN = 20;

    private static final String FIRE_SWORD_SOUND = "fire-sword.wav";
    private static final String FUTURISTIC_SWORD_SOUND = "ufo-sword.wav";
    private static final String LIGHTNING_SWORD_SOUND = "lightning-sword.wav";

    public double worldOffX = 22; // Offset from player
    public double worldOffY = -6;

    int activeSwordIndex = 0;
    boolean swordRightMode = false;

    GreenfootImage[] swordImages;
    private java.util.HashMap<Enemy, Integer> hitCooldowns = new java.util.HashMap<>();

    public KaineCompanionSword()
    {
        swordImages = new GreenfootImage[3];
        loadSwordImage(0, false);
    }

    public void act()
    {
        if(getWorld() == null) return;

        updateHitCooldowns();
        handleInput();

        // Follow player on screen
        GameWorld gw = (GameWorld)getWorld();
        int px = gw.player.getX();
        int py = gw.player.getY();
        setLocation(px + (int)worldOffX, py + (int)worldOffY);
        damageTouchingEnemies(gw);
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
            img.scale(rightMode ? SWORD_RIGHT_WIDTH : SWORD_WIDTH, SWORD_HEIGHT);
            if(!rightMode && index == 0) img.rotate(45);
            setImage(img);
        }
    }

    private void damageTouchingEnemies(GameWorld gw)
    {
        int damage = gw.player.getDamage() + (gw.swordLevel - 1) * 5;
        java.util.List<Enemy> enemies = new java.util.ArrayList<>(getIntersectingObjects(Enemy.class));

        for(Enemy enemy : enemies)
        {
            if(hitCooldowns.containsKey(enemy))
            {
                continue;
            }

            hitCooldowns.put(enemy, HIT_COOLDOWN);
            playActiveSwordSound();

            if(enemy.takeDamage(damage))
            {
                gw.player.gainXP(enemy.xpDrop);
                gw.player.gainCoin(enemy.coinDrop);

                if(enemy.getWorld() != null)
                {
                    enemy.getWorld().removeObject(enemy);
                }
            }
        }
    }

    private void updateHitCooldowns()
    {
        java.util.Iterator<java.util.Map.Entry<Enemy, Integer>> iterator = hitCooldowns.entrySet().iterator();

        while(iterator.hasNext())
        {
            java.util.Map.Entry<Enemy, Integer> entry = iterator.next();
            Enemy enemy = entry.getKey();
            int remaining = entry.getValue() - 1;

            if(remaining <= 0 || enemy.getWorld() == null)
            {
                iterator.remove();
            }
            else
            {
                entry.setValue(remaining);
            }
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
