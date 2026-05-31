import greenfoot.*;

public class Fireball extends Actor
{
    static GreenfootImage[] frames = new GreenfootImage[44];

    static
    {
        for(int i = 0; i < 44; i++)
        {
            String num = String.format("%03d", i);
            frames[i] = new GreenfootImage("FireBall/tile" + num + ".png");
        }
    }

    int frame = 0;
    int animationTimer = 0;

    double speed = 6;

    public double worldX;
    public double worldY;

    double velX;
    double velY;

    int damage;

    public Fireball(double startX, double startY, double targetX, double targetY, int damage)
    {
        worldX = startX;
        worldY = startY;
        this.damage = damage;

        double dx = targetX - startX;
        double dy = targetY - startY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist == 0)
        {
            dist = 1;
        }

        velX = dx / dist * speed;
        velY = dy / dist * speed;

        setImage(frames[0]);

        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotation((int)angle);
    }

    public void act()
    {
        if(getWorld() == null) return;

        worldX += velX;
        worldY += velY;

        World world = getWorld();

        if(world instanceof MyWorld)
        {
            setLocation((int)worldX, (int)worldY);
        }

        animate();
        checkHitEnemy();
        checkRange();
    }

    public void animate()
    {
        animationTimer++;

        if(animationTimer % 3 == 0)
        {
            frame = (frame + 1) % frames.length;

            int rot = getRotation();

            setImage(new GreenfootImage(frames[frame]));
            setRotation(rot);
        }
    }

    public void checkHitEnemy()
    {
        if(getWorld() == null) return;

        for(Enemy e : getWorld().getObjects(Enemy.class))
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;

            if(Math.sqrt(dx * dx + dy * dy) < 25)
            {
                e.takeDamage(damage);

                if(getWorld() != null)
                {
                    getWorld().removeObject(this);
                }

                return;
            }
        }
    }

    public void checkRange()
    {
        if(getWorld() == null) return;

        if(getWorld() instanceof MyWorld)
        {
            World world = getWorld();

            if(getX() < 0 || getX() > world.getWidth() || getY() < 0 || getY() > world.getHeight())
            {
                world.removeObject(this);
            }

            return;
        }

        GameWorld gw = (GameWorld)getWorld();

        double dx = worldX - gw.getPlayerWorldX();
        double dy = worldY - gw.getPlayerWorldY();

        if(Math.sqrt(dx * dx + dy * dy) > 1000)
        {
            gw.removeObject(this);
        }
    }
}
