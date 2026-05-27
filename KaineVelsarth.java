import greenfoot.*;

public class KaineVelsarth extends Actor
{
    private static final int STARTING_SPEED = 3;
    private static final int SWORD_X_OFFSET = 22;
    private static final int SWORD_Y_OFFSET = -6;
    private static final int STARTING_HP = 70;
    private static final int STARTING_XP = 0;
    private static final int STARTING_COIN = 0;
    private static final int STARTING_LEVEL = 1;
    private static final int STARTING_XP_TO_NEXT_LEVEL = 10;
    private static final int STARTING_SWORD_DAMAGE = 10;
    private static final int SWORD_HIT_COOLDOWN_MS = 400;
    private static final int SWORD_HIT_RANGE = 50;

    public int hp;
    public int maxHp;
    public int xp;
    public int coin;
    public int level;
    public int xpToNextLevel;
    public int swordDamage;
    public int speed;

    private String[] inventory;
    private String[] equippedSwords;
    private int activeSwordIndex;
    private boolean swordRightMode;
    private Actor activeSword;
    private SimpleTimer swordHitTimer = new SimpleTimer();

    public KaineVelsarth()
    {
        setUpStats();
        setUpInventory();
        activeSwordIndex = -1;
        swordRightMode = false;
        activeSword = null;

        GreenfootImage image = getImage();

        if (image != null)
        {
            image.scale(72, 72);
            setImage(image);
        }
    }

    public void act()
    {
        moveKaine();
        updateSwordPosition();
        handleSwordInput();
        checkLevelUp();
        displayStats();
    }

    protected void addedToWorld(World world)
    {
        world.showText("", 300, 330);
    }

    private void setUpStats()
    {
        maxHp = STARTING_HP;
        hp = STARTING_HP;
        xp = STARTING_XP;
        coin = STARTING_COIN;
        level = STARTING_LEVEL;
        xpToNextLevel = STARTING_XP_TO_NEXT_LEVEL;
        swordDamage = STARTING_SWORD_DAMAGE;
        speed = STARTING_SPEED;
    }

    private void setUpInventory()
    {
        inventory = new String[3];
        inventory[0] = "Fire Sword";
        inventory[1] = "Futuristic Sword";
        inventory[2] = "Lightning Sword";

        equippedSwords = inventory;
    }

    private void moveKaine()
    {
        int x = getX();
        int y = getY();

        if (Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left"))
        {
            x -= speed;
        }

        if (Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right"))
        {
            x += speed;
        }

        if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up"))
        {
            y -= speed;
        }

        if (Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down"))
        {
            y += speed;
        }

        World world = getWorld();

        if (world != null)
        {
            x = Math.max(36, Math.min(world.getWidth() - 36, x));
            y = Math.max(36, Math.min(world.getHeight() - 96, y));
        }

        setLocation(x, y);
    }

    private void handleSwordInput()
    {
        String key = Greenfoot.getKey();

        if (key == null)
        {
            return;
        }

        if (key.equalsIgnoreCase("space"))
        {
            activateNextSword();
        }
        else if (key.equalsIgnoreCase("tab") && activeSwordIndex >= 0)
        {
            swordRightMode = !swordRightMode;
            spawnActiveSword();
            hitEnemyWithSword();
        }
    }

    private void activateNextSword()
    {
        activeSwordIndex = (activeSwordIndex + 1) % equippedSwords.length;
        swordRightMode = false;
        spawnActiveSword();
    }

    private void spawnActiveSword()
    {
        World world = getWorld();

        if (world == null)
        {
            return;
        }

        if (activeSword == null || activeSword.getWorld() == null)
        {
            activeSword = createSwordForCurrentIndex();
            world.addObject(activeSword, getX() + SWORD_X_OFFSET, getY() + SWORD_Y_OFFSET);
            return;
        }

        Actor replacementSword = createSwordForCurrentIndex();
        activeSword.setImage(replacementSword.getImage());
    }

    private Actor createSwordForCurrentIndex()
    {
        if (activeSwordIndex == 0)
        {
            return new fireSword(swordRightMode);
        }

        if (activeSwordIndex == 1)
        {
            return new futuristicSword(swordRightMode);
        }

        return new lightningSword(swordRightMode);
    }

    private void updateSwordPosition()
    {
        if (activeSword != null && activeSword.getWorld() != null)
        {
            activeSword.setLocation(getX() + SWORD_X_OFFSET, getY() + SWORD_Y_OFFSET);
        }
    }

    private void hitEnemyWithSword()
    {
        if(activeSword == null || activeSword.getWorld() == null)
        {
            return;
        }

        if(swordHitTimer.millisElapsed() < SWORD_HIT_COOLDOWN_MS)
        {
            return;
        }

        Enemy enemy = getSwordHitEnemy();

        if(enemy == null)
        {
            return;
        }

        swordHitTimer.mark();

        boolean died = enemy.takeDamage(swordDamage);

        if(died)
        {
            gainXp(enemy.xpDrop);
            gainCoin(enemy.coinDrop);

            if(enemy.getWorld() != null)
            {
                enemy.getWorld().removeObject(enemy);
            }
        }
    }

    private Enemy getSwordHitEnemy()
    {
        World world = getWorld();

        if(world == null)
        {
            return null;
        }

        for(Enemy enemy : world.getObjects(Enemy.class))
        {
            if(distanceBetween(activeSword.getX(), activeSword.getY(), enemy.getX(), enemy.getY()) <= SWORD_HIT_RANGE)
            {
                return enemy;
            }
        }

        return null;
    }

    private double distanceBetween(int x1, int y1, int x2, int y2)
    {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String getInventoryText()
    {
        return "Inventory: " + inventory[0] + ", " + inventory[1] + ", " + inventory[2];
    }

    public String getEquippedText()
    {
        return "Equipped: " + equippedSwords[0] + ", " + equippedSwords[1] + ", " + equippedSwords[2];
    }

    public String getStartingLoadoutText()
    {
        return "Player starts with all 3 swords equipped.";
    }

    public int getHp()
    {
        return hp;
    }

    public int getMaxHp()
    {
        return maxHp;
    }

    public int getXp()
    {
        return xp;
    }

    public int getCoin()
    {
        return coin;
    }

    public void takeDamage(int damage)
    {
        hp = Math.max(0, hp - Math.max(0, damage));

        if(hp == 0)
        {
            Greenfoot.stop();
        }
    }

    public void gainXp(int amount)
    {
        xp += Math.max(0, amount);
    }

    public void gainCoin(int amount)
    {
        coin += Math.max(0, amount);
    }

    public void checkLevelUp()
    {
        while(xp >= xpToNextLevel)
        {
            xp -= xpToNextLevel;
            level++;
            xpToNextLevel = (int)(xpToNextLevel * 1.5);
            maxHp += 10;
            hp = maxHp;
            speed++;
            swordDamage += 5;
        }
    }

    public void displayStats()
    {
        World world = getWorld();

        if(world == null)
        {
            return;
        }

        GreenfootImage background = world.getBackground();
        background.setColor(Color.BLACK);
        background.fillRect(0, 0, 500, 100);

        background.setColor(Color.WHITE);
        background.fillRect(10, 10, 300, 25);

        background.setColor(Color.RED);
        background.fillRect(10, 10, hp * 300 / maxHp, 25);

        background.setColor(Color.WHITE);
        background.fillRect(10, 45, 300, 25);

        background.setColor(Color.BLUE);
        background.fillRect(10, 45, xp * 300 / xpToNextLevel, 25);

        world.showText(hp + " / " + maxHp + " HP", 390, 23);
        world.showText("LV " + level + "   " + xp + " / " + xpToNextLevel + " XP", 410, 58);
        world.showText("Coin: " + coin, 80, 85);
    }
}
