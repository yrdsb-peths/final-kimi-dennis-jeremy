import greenfoot.*;

public class Enemy extends Actor
{
    int speed = 2;
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

        GreenfootImage image = new GreenfootImage(40, 40);
        image.setColor(Color.RED);
        image.fillOval(0, 0, 40, 40);
        setImage(image);
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

            if(world.aureaSolvine == null)
            {
                return;
            }

            moveToward(world.aureaSolvine.worldX, world.aureaSolvine.worldY);
            return;
        }

        if(currentWorld instanceof MyWorld)
        {
            MyWorld world = (MyWorld)currentWorld;
            Actor player = world.leon != null ? world.leon : world.kaine;

            if(player != null)
            {
                moveToward(player.getX(), player.getY());
            }

            return;
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

    public boolean takeDamage(int damage)
    {
        hp -= damage;
        return hp <= 0;
    }

    public void touchPlayer()
    {
        World currentWorld = getWorld();

        if(currentWorld instanceof GameWorld)
        {
            GameWorld world = (GameWorld)currentWorld;

            if(world.aureaSolvine == null)
            {
                return;
            }

            damageAureaOnTouch(world);
            return;
        }

        if(currentWorld instanceof MyWorld)
        {
            damageSelectedPlayerOnTouch((MyWorld)currentWorld);
        }
    }

    private void damageAureaOnTouch(GameWorld world)
    {
        double dx = world.aureaSolvine.worldX - worldX;
        double dy = world.aureaSolvine.worldY - worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if(distance < 40 && damageTimer.millisElapsed() > 1000)
        {
            world.aureaSolvine.takeHit(5);
            damageTimer.mark();
        }
    }

    private void damageSelectedPlayerOnTouch(MyWorld world)
    {
        if(world.leon != null && getOneIntersectingObject(LeonClovis.class) != null)
        {
            damageLeon(world.leon);
        }
        else if(world.kaine != null && getOneIntersectingObject(KaineVelsarth.class) != null)
        {
            damageKaine(world.kaine);
        }
    }

    private void damageLeon(LeonClovis leon)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            leon.takeDamage(5);
            damageTimer.mark();
        }
    }

    private void damageKaine(KaineVelsarth kaine)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            kaine.takeDamage(5);
            damageTimer.mark();
        }
    }
}
