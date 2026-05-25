import greenfoot.*;

public class Fireball extends Actor
{
    static GreenfootImage[] frames = new GreenfootImage[44];

    static
    {
        for(int i = 0; i < 44; i++)
        {
            String num = String.format("%03d", i); // 000, 001, ... 043
            frames[i] = new GreenfootImage("FireBall/tile" + num + ".png");
            frames[i].scale(120, 120);
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
            //img.rotate(90);
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
        // isAtEdge() 是 Greenfoot 内置方法，碰到边界就删除
        if(isAtEdge())
        {
            w.removeObject(this);
        }
    }
}