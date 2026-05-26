import greenfoot.*;

public class Bullet extends Actor
{
    Enemy target;
    LeonClovis player;

    int speed = 8;
    int damage = 10;

    public Bullet(Enemy enemy, LeonClovis leon)
    {
        target = enemy;
        player = leon;

        GreenfootImage img = new GreenfootImage(40,8);
        img.setColor(Color.WHITE);
        img.fillRect(0,0,40,8);
        img.setColor(Color.CYAN);
        img.fillRect(2,2,36,4);
        setImage(img);
    }

    public void act()
    {
        if(getWorld() == null) return;

        followEnemy();

        if(getWorld() == null) return;

        hitEnemy();

        if(getWorld() == null) return;

        removeAtEdge();
    }

    public void followEnemy()
    {
        if(target != null && target.getWorld() != null)
        {
            turnTowards(target.getX(), target.getY());
            move(speed);
        }
        else
        {
            getWorld().removeObject(this);
        }
    }

    public void hitEnemy()
    {
        Enemy enemy = (Enemy)getOneIntersectingObject(Enemy.class);

        if(enemy != null)
        {
            enemy.takeDamage(damage, player);

            if(getWorld() != null)
            {
                getWorld().removeObject(this);
            }

            return;
        }
    }

    public void removeAtEdge()
    {
        if(isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}