import greenfoot.*;

public class Enemy extends Actor
{
    private static final int BASE_SIZE = 40;
    private static final int LEVEL_FIVE_SIZE = 60;
    private static final int LEVEL_FIVE_HEALTH_MULTIPLIER = 4;
    private static final int LEVEL_TEN_HEALTH_MULTIPLIER = 16;
    private static final int STRONG_WAVE = 5;
    private static final int STRONG_WAVE_SIZE = BASE_SIZE * 2;
    private static final int STRONG_WAVE_HEALTH_MULTIPLIER = 2;
    private static final int TOUCH_DAMAGE_RANGE = 45;

    int speed = 2;
    int damage = 5;

    public int hp = 30;
    public int maxHp = 30;

    public int xpDrop = 3;
    public int coinDrop = 2;

    public double worldX;
    public double worldY;

    SimpleTimer damageTimer = new SimpleTimer();

    public Enemy(double worldX, double worldY)
    {
        this.worldX = worldX;
        this.worldY = worldY;

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
            setHealth(maxHp * STRONG_WAVE_HEALTH_MULTIPLIER);
            setBodySize(STRONG_WAVE_SIZE);
        }
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

    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }

        followPlayer();

        if(getWorld() == null)
        {
            return;
        }

        touchPlayer();
    }

    public void followPlayer()
    {
        World currentWorld = getWorld();

        if(currentWorld instanceof GameWorld)
        {
            GameWorld world = (GameWorld)currentWorld;

            moveToward(
                world.getPlayerWorldX(),
                world.getPlayerWorldY()
            );

            return;
        }

        if(currentWorld instanceof MyWorld)
        {
            MyWorld world = (MyWorld)currentWorld;

            Actor player =
                world.leon != null
                ? world.leon
                : world.kaine;

            if(player == null)
            {
                player = world.aurea;
            }

            if(player != null)
            {
                moveToward(
                    player.getX(),
                    player.getY()
                );
                setLocation((int)worldX, (int)worldY);
            }
        }
    }

    private void moveToward(
        double targetX,
        double targetY)
    {
        double dx = targetX - worldX;
        double dy = targetY - worldY;

        double distance =
            Math.sqrt(dx * dx + dy * dy);

        if(distance > 0)
        {
            worldX += dx / distance * speed;
            worldY += dy / distance * speed;
        }
    }

    public void takeDamage(int damage)
    {
        hp -= damage;

        if(hp <= 0)
        {
            World currentWorld = getWorld();

            if(currentWorld instanceof GameWorld)
            {
                GameWorld world =
                    (GameWorld)currentWorld;

                world.givePlayerReward(
                    xpDrop,
                    coinDrop
                );
            }
            else if(currentWorld instanceof MyWorld)
            {
                MyWorld world = (MyWorld)currentWorld;
                world.giveSelectedPlayerReward(xpDrop, coinDrop);
            }

            if(currentWorld != null)
            {
                currentWorld.removeObject(this);
            }

            if(getWorld() != null)
            {
                getWorld().removeObject(this);
            }
        }
    }

    public void touchPlayer()
    {
        World currentWorld = getWorld();

        if(currentWorld instanceof GameWorld)
        {
            damageGameWorldPlayer(
                (GameWorld)currentWorld
            );

            return;
        }

        if(currentWorld instanceof MyWorld)
        {
            damageSelectedPlayerOnTouch(
                (MyWorld)currentWorld
            );
        }
    }

    private void damageGameWorldPlayer(
        GameWorld world)
    {
        double dx =
            world.getPlayerWorldX()
            - worldX;

        double dy =
            world.getPlayerWorldY()
            - worldY;

        double distance =
            Math.sqrt(dx * dx + dy * dy);

        if(distance < 40
            && damageTimer.millisElapsed() > 1000)
        {
            world.damagePlayer(damage);
            damageTimer.mark();
        }
    }

    private void damageSelectedPlayerOnTouch(
        MyWorld world)
    {
        if(world.leon != null
            && isCloseTo(world.leon))
        {
            damageSelectedPlayer(world);
        }
        else if(world.kaine != null
            && isCloseTo(world.kaine))
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
        int dx = getX() - actor.getX();
        int dy = getY() - actor.getY();
        return Math.sqrt(dx * dx + dy * dy) < TOUCH_DAMAGE_RANGE;
    }

    private void damageSelectedPlayer(
        MyWorld world)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            world.damageSelectedPlayer(damage);
            damageTimer.mark();
        }
    }
}
