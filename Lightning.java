import greenfoot.*;

public class Lightning extends Actor
{
    static GreenfootImage[] frames = new GreenfootImage[4];

    static
    {
        for(int i = 0; i < 4; i++)
        {
            frames[i] = new GreenfootImage("lightning/tile00" + i + ".png");

            frames[i].scale(
                frames[i].getWidth() / 3,
                frames[i].getHeight() / 3
            );
        }
    }

    int frame = 0;
    int timer = 0;

    int damage;

    boolean hasHit = false;

    public double worldX;
    public double worldY;

    public Lightning(double worldX, double worldY, int damage)
    {
        this.worldX = worldX;
        this.worldY = worldY;

        this.damage = damage;

        setImage(frames[0]);
    }

    public void act()
    {
        if(getWorld() == null) return;

        animate();

        if(getWorld() == null) return;

        if(!hasHit)
        {
            hitEnemy();
        }
    }

    public void animate()
    {
        timer++;

        if(timer % 3 == 0)
        {
            frame++;

            if(frame >= frames.length)
            {
                if(getWorld() != null)
                {
                    getWorld().removeObject(this);
                }

                return;
            }

            setImage(frames[frame]);
        }
    }

    public void hitEnemy()
    {
        if(getWorld() == null) return;

        GameWorld gw = (GameWorld)getWorld();

        for(Enemy e : gw.getObjects(Enemy.class))
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;

            if(Math.sqrt(dx * dx + dy * dy) < 30)
            {
                e.takeDamage(damage);

                hasHit = true;
                return;
            }
        }
    }
}