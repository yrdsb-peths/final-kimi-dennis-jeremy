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
    private static final double SWORD_DAMAGE_LEVEL_MULTIPLIER = 1.6;
    private static final int SWORD_HIT_COOLDOWN_MS = 400;
    private static final int SWORD_HIT_RANGE = 80;

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
    private SimpleTimer swordSwitchTimer = new SimpleTimer();

    public KaineVelsarth()
    {
        setUpStats();
        setUpInventory();

        activeSwordIndex = -1;
        swordRightMode = false;
        activeSword = null;

        GreenfootImage image = new GreenfootImage("Kaine.png");
        image.scale(72, 72);
        setImage(image);
    }

    protected void addedToWorld(World world)
    {
        activateNextSword();
    }

    public void act()
    {
        moveKaine();
        updateSwordPosition();
        handleSwordInput();
        hitEnemyWithSword();
        checkLevelUp();
        displayStats();
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

        if(Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left"))
        {
            x -= speed;
        }

        if(Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right"))
        {
            x += speed;
        }

        if(Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up"))
        {
            y -= speed;
        }

        if(Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down"))
        {
            y += speed;
        }

        World world = getWorld();

        if(world != null)
        {
            x = Math.max(36, Math.min(world.getWidth() - 36, x));
            y = Math.max(36, Math.min(world.getHeight() - 96, y));
        }

        setLocation(x, y);
    }

    private void handleSwordInput()
    {
        if(Greenfoot.isKeyDown("space")
            && activeSwordIndex >= 0
            && swordSwitchTimer.millisElapsed() > 250)
        {
            activateNextSword();
            swordSwitchTimer.mark();
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

        if(world == null)
        {
            return;
        }

        if(activeSword == null || activeSword.getWorld() == null)
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
        if(activeSwordIndex == 0)
        {
            return new fireSword(swordRightMode);
        }

        if(activeSwordIndex == 1)
        {
            return new futuristicSword(swordRightMode);
        }

        return new lightningSword(swordRightMode);
    }

    private void updateSwordPosition()
    {
        if(activeSword != null && activeSword.getWorld() != null)
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

        enemy.takeDamage(swordDamage);
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
            if(distanceBetween(getX(), getY(), enemy.getX(), enemy.getY()) <= SWORD_HIT_RANGE)
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

    public void takeDamage(int damage)
    {
        hp = Math.max(0, hp - Math.max(0, damage));

        if(hp == 0)
        {
            showGameOver();
            Greenfoot.stop();
        }
    }

    private void showGameOver()
    {
        World world = getWorld();

        if(world != null)
        {
            world.addObject(new GameOver(), world.getWidth() / 2, world.getHeight() / 2);
        }
    }

    public void gainXp(int amount)
    {
        xp += Math.max(0, amount);
        checkLevelUp();
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
            if(level % 5 == 0)
            {
                swordDamage = (int)Math.round(swordDamage * SWORD_DAMAGE_LEVEL_MULTIPLIER);
            }
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
        int hpBarWidth = Math.max(0, Math.min(300, hp * 300 / maxHp));
        int xpBarWidth = Math.max(0, Math.min(300, xp * 300 / xpToNextLevel));

        background.setColor(Color.WHITE);
        background.fillRect(10, 10, 300, 25);
        background.setColor(Color.RED);
        background.fillRect(10, 10, hpBarWidth, 25);

        background.setColor(Color.WHITE);
        background.fillRect(10, 45, 300, 25);
        background.setColor(Color.BLUE);
        background.fillRect(10, 45, xpBarWidth, 25);

        background.setColor(Color.BLACK);
        background.drawRect(10, 10, 300, 25);
        background.drawRect(10, 45, 300, 25);

        world.showText(hp + " / " + maxHp + " HP", 390, 23);
        world.showText("LV " + level + "   " + xp + " / " + xpToNextLevel + " XP", 410, 58);
        world.showText("Coin: " + coin, 80, 85);
    }
}
