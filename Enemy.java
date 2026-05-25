import greenfoot.*;

public class Enemy extends Actor
{
    int hp = 30;
    int speed = 2;

    public Enemy()
    {
        GreenfootImage img = new GreenfootImage(40, 40);

        img.setColor(Color.RED);
        img.fillOval(0, 0, 40, 40);

        setImage(img);

        turn(Greenfoot.getRandomNumber(360));
    }

    public void act()
    {
        moveAround();
        checkBulletHit();
    }

    public void moveAround()
    {
        move(speed);

        if(isAtEdge())
        {
            turn(180);
        }

        if(Greenfoot.getRandomNumber(100) < 2)
        {
            turn(Greenfoot.getRandomNumber(90) - 45);
        }
    }

    public void checkBulletHit()
    {
        Bullet bullet = (Bullet)getOneIntersectingObject(Bullet.class);

        if(bullet != null)
        {
            hp -= 10;

            getWorld().removeObject(bullet);

            if(hp <= 0)
            {
                getWorld().removeObject(this);
            }
        }
    }
}
