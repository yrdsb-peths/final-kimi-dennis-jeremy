import greenfoot.*;

public class Fireball extends Actor
{
    static GreenfootImage[] frames = new GreenfootImage[9];

    static
    {
        for(int i = 0; i < 9; i++)
        {
            frames[i] =
                new GreenfootImage(
                    "FireBall/tile00" + i + ".png"
                );

            frames[i].scale(
                frames[i].getWidth() / 4,
                frames[i].getHeight() / 4
            );
        }
    }

    Enemy target;

    int frame = 0;
    int animationTimer = 0;

    int speed = 12;

    public Fireball(Enemy enemy)
    {
        target = enemy;

        setImage(frames[0]);
    }

    public void act()
    {
        moveToEnemy();

        if(getWorld() == null)
        {
            return;
        }

        animate();

        hitEnemy();
    }

    public void moveToEnemy()
{
    if(target == null || target.getWorld() == null)
    {
        getWorld().removeObject(this);
        return;
    }

    turnTowards(target.getX(), target.getY());

    move(speed);
}

    public void animate()
    {
        animationTimer++;

        if(animationTimer % 3 == 0)
        {
            frame++;

            if(frame >= frames.length)
            {
                frame = 0;
            }

            GreenfootImage img = new GreenfootImage(frames[frame]);

            img.rotate(-90); // 或 -90（看你素材）
            
            setImage(img);
        }
    }

    public void hitEnemy()
    {
      Enemy enemy =
            (Enemy)getOneIntersectingObject(
                Enemy.class
            );

        if(enemy != null)
        {
            World world = getWorld();

            world.removeObject(enemy);
            world.removeObject(this);
        }
    }
}
