import greenfoot.*;

public class Enemy extends Actor
{
    int hp = 30;
    int speed = 2;

    SimpleTimer damageTimer = new SimpleTimer();

    public Enemy()
    {
        GreenfootImage img = new GreenfootImage(40,40);
        img.setColor(Color.RED);
        img.fillOval(0,0,40,40);
        setImage(img);

        turn(Greenfoot.getRandomNumber(360));
    }

    public void act()
    {
        moveAround();
        touchLeon();
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

    public void takeDamage(int damage, LeonClovis player)
    {
        hp -= damage;

        if(hp <= 0)
        {
            player.addReward();
            getWorld().removeObject(this);
        }
    }

    public void touchLeon()
    {
        LeonClovis leon = (LeonClovis)getOneIntersectingObject(LeonClovis.class);

        if(leon != null && damageTimer.millisElapsed() > 1000)
        {
            leon.takeDamage(5);
            damageTimer.mark();
        }
    }
}