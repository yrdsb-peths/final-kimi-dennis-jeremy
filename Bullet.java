import greenfoot.*;

public class Bullet extends Actor
{
    Enemy target;

    int speed = 8;
    int damage = 10;

    public Bullet(Enemy enemy)
    {
        target = enemy;

        GreenfootImage img = new GreenfootImage(10,10);

        img.setColor(Color.YELLOW);
        img.fillOval(0,0,10,10);

        setImage(img);
    }

    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }

        followEnemy();

        if(getWorld() == null)
        {
            return;
        }

        hitEnemy();

        if(getWorld() == null)
        {
            return;
        }

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
            enemy.hp -= damage;
            
            getWorld().addObject(new HitEffect(), getX(), getY());
            
            if(enemy.hp <= 0)
            {
                if(enemy.getWorld() != null)
                {
                    enemy.getWorld().removeObject(enemy);
                }
            }

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