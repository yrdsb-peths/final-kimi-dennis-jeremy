import greenfoot.*;

public class Fireball extends Actor
{
    static GreenfootImage[] frames = new GreenfootImage[9];

    static
    {
        for(int i = 0; i < 9; i++)
        {
            frames[i] = new GreenfootImage("FireBall/tile00" + i + ".png");
            frames[i].scale(
                frames[i].getWidth() / 4,
                frames[i].getHeight() / 4
            );
        }
    }

    int frame = 0;
    int animationTimer = 0;
    int speed = 6;
    int targetX, targetY;

    public Fireball(int targetX, int targetY)
    {
        this.targetX = targetX;
        this.targetY = targetY;
        setImage(frames[0]);
    }

    protected void addedToWorld(World world)
    {
        turnTowards(targetX, targetY);
    }

    public void act()
    {
        move(speed);
        if(getWorld() == null) return;
        animate();
        hitEnemy();
        removeIfOutOfBounds();
    }

    public void animate()
    {
        animationTimer++;
        if(animationTimer % 3 == 0)
        {
            frame = (frame + 1) % frames.length;
            GreenfootImage img = new GreenfootImage(frames[frame]);
            img.rotate(-90);
            setImage(img);
        }
    }

    public void hitEnemy()
    {
        Enemy enemy = (Enemy)getOneIntersectingObject(Enemy.class);
        if(enemy != null)
        {
            GameWorld gw = (GameWorld)getWorld();
            boolean died = enemy.takeDamage(gw.aureaSolvine.getDamage());
            if(died)
            {
                gw.aureaSolvine.gainXP(enemy.xpDrop);
                gw.aureaSolvine.gainCoin(enemy.coinDrop);
                if(enemy.getWorld() != null) gw.removeObject(enemy);
            }
            if(getWorld() != null) gw.removeObject(this);
        }
    }

    public void removeIfOutOfBounds()
    {
        if(getWorld() == null) return;
        World w = getWorld();
        if(getX() < -10 || getX() > w.getWidth() + 10 ||
           getY() < -10 || getY() > w.getHeight() + 10)
        {
            w.removeObject(this);
        }
    }
}