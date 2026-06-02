import greenfoot.*;

public class Enemy extends Actor
{
    private static final String HIT_SOUND = "Spider sounds.mp3";
    private static final int BASE_SIZE = 40;
    private static final int LEVEL_FIVE_SIZE = 60;
    private static final int LEVEL_FIVE_HEALTH_MULTIPLIER = 4;
    private static final int LEVEL_TEN_HEALTH_MULTIPLIER = 16;
    private static final int STRONG_WAVE = 5;
    private static final int STRONG_WAVE_SIZE_MULTIPLIER = 2;
    private static final int STRONG_WAVE_HEALTH_MULTIPLIER = 2;
    private static final int STRONG_WAVE_DAMAGE_MULTIPLIER = 2;
    private static final int TOUCH_DAMAGE_RANGE = 45;
    private static final int ATTACK_INTERVAL = 60;

    public int speed = 2;
    public int hp = 30;
    public int maxHp = 30;
    public int xpDrop = 3;
    public int coinDrop = 2;
    public int attackDamage = 5;
    public int damage = 5;

    public double worldX;
    public double worldY;

    private int attackCooldown = 0;
    private SimpleTimer damageTimer = new SimpleTimer();

    public Enemy(double worldX, double worldY)
    {
        this(worldX, worldY, 1);
    }

    public Enemy(double worldX, double worldY, int round)
    {
        this.worldX = worldX;
        this.worldY = worldY;

        speed = 2 + round / 5;
        setHealth(30 + (round - 1) * 8);
        damage = 5 + (round - 1) * 2;
        attackDamage = damage;
        xpDrop = 3 + round / 3;
        coinDrop = 2 + round / 5;
        setBodySize(BASE_SIZE);
    }

    public void applyLevelScaling(int heroLevel)
    {
        if(heroLevel >= 10)
        {
            setHealth(30 * LEVEL_TEN_HEALTH_MULTIPLIER);
            setBodySize(LEVEL_FIVE_SIZE);
        }
        else if(heroLevel >= 5)
        {
            setHealth(30 * LEVEL_FIVE_HEALTH_MULTIPLIER);
            setBodySize(LEVEL_FIVE_SIZE);
        }
    }

    public void applyWaveScaling(int waveNumber)
    {
        if(waveNumber >= STRONG_WAVE)
        {
            int waveBoosts = waveNumber / STRONG_WAVE;
            int healthMultiplier = 1;
            int damageMultiplier = 1;
            int sizeMultiplier = 1;

            for(int i = 0; i < waveBoosts; i++)
            {
                healthMultiplier *= STRONG_WAVE_HEALTH_MULTIPLIER;
                damageMultiplier *= STRONG_WAVE_DAMAGE_MULTIPLIER;
                sizeMultiplier *= STRONG_WAVE_SIZE_MULTIPLIER;
            }

            setHealth(maxHp * healthMultiplier);
            damage *= damageMultiplier;
            attackDamage = damage;
            setBodySize(BASE_SIZE * sizeMultiplier);
        }
    }

    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }

        followPlayer();

        if(getWorld() != null)
        {
            touchPlayer();
        }
    }

    private void followPlayer()
    {
        World currentWorld = getWorld();

        if(currentWorld instanceof GameWorld)
        {
            GameWorld world = (GameWorld)currentWorld;
            moveToward(world.getPlayerWorldX(), world.getPlayerWorldY());
            int screenX = (int)(world.screenCX + (worldX - world.getPlayerWorldX()));
            int screenY = (int)(world.screenCY + (worldY - world.getPlayerWorldY()));
            setLocation(screenX, screenY);
            turnTowards(world.screenCX, world.screenCY);
        }
        else if(currentWorld instanceof MyWorld)
        {
            MyWorld world = (MyWorld)currentWorld;
            Actor player = world.leon != null ? world.leon : world.kaine;

            if(player == null)
            {
                player = world.aurea;
            }

            if(player != null)
            {
                moveToward(player.getX(), player.getY());
                setLocation((int)worldX, (int)worldY);
            }
        }
    }

    private void moveToward(double targetX, double targetY)
    {
        double dx = targetX - worldX;
        double dy = targetY - worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if(distance > 0)
        {
            worldX += dx / distance * speed;
            worldY += dy / distance * speed;
        }
    }

    private void touchPlayer()
    {
        World currentWorld = getWorld();

        if(currentWorld instanceof GameWorld)
        {
            damageGameWorldPlayer((GameWorld)currentWorld);
        }
        else if(currentWorld instanceof MyWorld)
        {
            damageSelectedPlayerOnTouch((MyWorld)currentWorld);
        }
    }

    private void damageGameWorldPlayer(GameWorld world)
    {
        attackCooldown++;

        if(distanceBetween(world.getPlayerWorldX(), world.getPlayerWorldY(), worldX, worldY) < 40
            && attackCooldown >= ATTACK_INTERVAL)
        {
            world.damagePlayer(damage);
            attackCooldown = 0;
        }
    }

    private void damageSelectedPlayerOnTouch(MyWorld world)
    {
        if(world.leon != null && isCloseTo(world.leon))
        {
            damageSelectedPlayer(world);
        }
        else if(world.kaine != null && isCloseTo(world.kaine))
        {
            damageSelectedPlayer(world);
        }
        else if(world.aurea != null && isCloseTo(world.aurea))
        {
            damageSelectedPlayer(world);
        }
    }

    private boolean isCloseTo(Actor actor)
    {
        return distanceBetween(getX(), getY(), actor.getX(), actor.getY()) < TOUCH_DAMAGE_RANGE;
    }

    private void damageSelectedPlayer(MyWorld world)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            world.damageSelectedPlayer(damage);
            damageTimer.mark();
        }
    }

    public boolean takeDamage(int damage)
    {
        Greenfoot.playSound(HIT_SOUND);
        hp -= Math.max(0, damage);
        return hp <= 0;
    }

    public boolean takeDamage(int damage, Hero source)
    {
        boolean died = takeDamage(damage);

        if(died && source != null)
        {
            source.gainXP(xpDrop);
            source.gainCoin(coinDrop);

            if(getWorld() != null)
            {
                getWorld().removeObject(this);
            }
        }

        return died;
    }

    private void setHealth(int health)
    {
        maxHp = health;
        hp = maxHp;
    }

    private void setBodySize(int size)
    {
        try
        {
            GreenfootImage image = new GreenfootImage("enemy.png");
            image.scale(size, size);
            setImage(image);
        }
        catch(Exception exception)
        {
            GreenfootImage image = new GreenfootImage(size, size);
            image.setColor(Color.RED);
            image.fillOval(0, 0, size, size);
            setImage(image);
        }
    }

    private double distanceBetween(double x1, double y1, double x2, double y2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
