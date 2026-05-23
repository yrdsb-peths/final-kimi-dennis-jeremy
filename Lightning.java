import greenfoot.*;

public class Lightning extends Actor
{
    static GreenfootImage[] frames = new GreenfootImage[16];

    static
    {
        for(int i = 0; i < 16; i++)
        {
            frames[i] = new GreenfootImage("lightning/lightning" + i + ".png");
            frames[i].scale(
                frames[i].getWidth() / 3,
                frames[i].getHeight() / 3
            );
        }
    }

    int frame = 0;
    int timer = 0;
    int damage;

    public Lightning(int damage)
    {
        this.damage = damage;
        setImage(frames[0]);
    }

    public void act()
    {
        animate();
        if(getWorld() == null) return;
        hitEnemy();
    }

    public void animate()
    {
        timer++;
        if(timer % 3 == 0)
        {
            frame++;
            if(frame >= frames.length)
            {
                getWorld().removeObject(this);
                return;
            }
            setImage(frames[frame]);
        }
    }

    public void hitEnemy()
    {
        Enemy enemy = (Enemy)getOneIntersectingObject(Enemy.class);
        if(enemy != null)
        {
            GameWorld gw = (GameWorld)getWorld();
            boolean died = enemy.takeDamage(damage);
            if(died)
            {
                gw.aureaSolvine.gainXP(enemy.xpDrop);
                gw.aureaSolvine.gainCoin(enemy.coinDrop);
                gw.removeObject(enemy);
            }
        }
    }
}