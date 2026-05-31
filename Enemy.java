import greenfoot.*;

public class Enemy extends Actor
{
    private static final int BASE_SIZE = 40;
    private static final int LEVEL_FIVE_SIZE = 60;
    private static final int LEVEL_FIVE_HEALTH_MULTIPLIER = 4;
    private static final int LEVEL_TEN_HEALTH_MULTIPLIER = 16;
    private static final int STRONG_WAVE = 5;
    private static final int STRONG_WAVE_HEALTH_MULTIPLIER = 3;
    private static final int STRONG_WAVE_DAMAGE = 10;
    private static final int STRONG_WAVE_SPEED = 3;

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
            speed = STRONG_WAVE_SPEED;
            damage = STRONG_WAVE_DAMAGE;
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
            && getOneIntersectingObject(
                LeonClovis.class) != null)
        {
            damageLeon(world.leon);
        }
        else if(world.kaine != null
            && getOneIntersectingObject(
                KaineVelsarth.class) != null)
        {
            damageKaine(world.kaine);
        }
        else if(world.aurea != null && getOneIntersectingObject(AureaSolvine.class) != null)
        {
            damageAurea(world.aurea);
        }
    }

    private void damageLeon(
        LeonClovis leon)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            leon.takeDamage(damage);
            damageTimer.mark();
        }
    }

    private void damageKaine(
        KaineVelsarth kaine)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            kaine.takeDamage(damage);
            damageTimer.mark();
        }
    }

    private void damageAurea(AureaSolvine aurea)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            aurea.takeHit(damage);
            damageTimer.mark();
        }
    }
}
