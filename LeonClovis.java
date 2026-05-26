import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LeonClovis here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LeonClovis extends Actor
{
    int speed = 4;
    public int hp = 70;
    public int maxHp = 70;
    public int xp = 0;
    public int coin = 0;
    public int level = 1;
    public int xpToNextLevel = 10;
    public int stamina = 3;
    public int power = 3;
    GreenfootImage[] leonRight = new GreenfootImage[8];  
    GreenfootImage[] leonLeft = new GreenfootImage[8];
    GreenfootImage[] leonFront = new GreenfootImage[4];
    GreenfootImage[] leonBack = new GreenfootImage[4];

    String facing = "right";

    SimpleTimer animationTimer = new SimpleTimer();
    
    int imageIndex = 0;
    public LeonClovis()
    {
        for(int i = 0; i < leonRight.length; i++)
        {
            leonRight[i] = new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonRight[i].scale(50,50);
        }

        for(int i = 0; i < leonLeft.length; i++)
        {
            leonLeft[i] = new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonLeft[i].mirrorHorizontally();
            leonLeft[i].scale(50,50);
        }

        for(int i = 0; i < leonFront.length; i++)
        {
            leonFront[i] = new GreenfootImage("images/leon_move_front/leon" + i + ".png");
            leonFront[i].scale(50,50);
        }

        for(int i = 0; i < leonBack.length; i++)
        {
            leonBack[i] = new GreenfootImage("images/leon_move_back/leonBack" + i + ".png");
            leonBack[i].scale(50,50);
        }
    }

    public void act()
    {
        movePlayer();
        animateLeon();
        checkLevelUp();
    }

    public void movePlayer()
    {
        boolean moving = false;

        if (Greenfoot.isKeyDown("w"))
        {
            setLocation(getX(), getY() - speed);
            facing = "back";
            moving = true;
        }

        if (Greenfoot.isKeyDown("s"))
        {
            setLocation(getX(), getY() + speed);
            facing = "front";
            moving = true;
        }

        if (Greenfoot.isKeyDown("a"))
        {
            setLocation(getX() - speed, getY());
            facing = "left";
            moving = true;
        }

        if (Greenfoot.isKeyDown("d"))
        {
            setLocation(getX() + speed, getY());
            facing = "right";
            moving = true;
        }

        if(!moving)
        {
            imageIndex = 0;

            if(facing.equals("right"))
            {
                setImage(leonRight[0]);
            }
            else if(facing.equals("left"))
            {
                setImage(leonLeft[0]);
            }
            else if(facing.equals("front"))
            {
                setImage(leonFront[0]);
            }
            else if(facing.equals("back"))
            {
                setImage(leonBack[0]);
            }
        }
    }

    public void animateLeon()
    {
        if(animationTimer.millisElapsed() < 100)
        {
            return;
        }

        animationTimer.mark();

        if(facing.equals("right"))
        {
            setImage(leonRight[imageIndex]);
            imageIndex = (imageIndex + 1) % leonRight.length;
        }
        else if(facing.equals("left"))
        {
            setImage(leonLeft[imageIndex]);
            imageIndex = (imageIndex + 1) % leonLeft.length;
        }
        else if(facing.equals("front"))
        {
            imageIndex %= leonFront.length;
            setImage(leonFront[imageIndex]);
            imageIndex++;
        }
        else if(facing.equals("back"))
        {
            imageIndex %= leonBack.length;
            setImage(leonBack[imageIndex]);
            imageIndex++;
        }
    }
    public void gainXP(int amount) { xp += amount; }
    public void gainCoin(int amount) { coin += amount; }

    public void checkLevelUp()
    {
        if(xp >= xpToNextLevel)
        {
            xp -= xpToNextLevel;
            level++;
            xpToNextLevel = (int)(xpToNextLevel * 1.5);
            speed  += 1;
            stamina += 1;
            power  += 1;
            maxHp  += 10;
            hp      = maxHp;
        }
    }
}
