import greenfoot.*;

public class fireSword extends Actor
{
    public fireSword()
    {
        this(false);
    }

    public fireSword(boolean swordRightMode)
    {
        GreenfootImage image;

        if (swordRightMode)
        {
            image = new GreenfootImage("FireSword.90 right.png");
            image.scale(40, 40);
        }
        else
        {
            image = new GreenfootImage("10.png");
            image.scale(20, 40);
        }

        setImage(image);
    }

    public void act()
    {
    }
}
