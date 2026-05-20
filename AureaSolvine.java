import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class AureaSolvine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AureaSolvine extends Actor
{
    public int hp = 70;
    public int xp = 0;
    public int coin = 0;
    int speed = 4;
    int Stamina = 3;
    int power = 3;
    int physicalDamage = 10;
    int elementDamage = 10;
    public void act()
    {
        // Add your action code here.
        movement();
    }
    
    public void movement()
    {
        if(Greenfoot.isKeyDown("w"))
        {
            setLocation(getX(), getY() - speed);
        }

        if(Greenfoot.isKeyDown("s"))
        {
            setLocation(getX(), getY() + speed);
        }

        if(Greenfoot.isKeyDown("a"))
        {
            setLocation(getX() - speed, getY());
        }

        if(Greenfoot.isKeyDown("d"))
        {
            setLocation(getX() + speed, getY());
        }
    }
}
