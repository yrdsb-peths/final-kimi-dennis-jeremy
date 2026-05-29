import greenfoot.*;

public class futuristicSword extends Actor
{
    public futuristicSword()
    {
        this(false);
    }

    public futuristicSword(boolean swordRightMode)
    {
        GreenfootImage image;

        if (swordRightMode)
        {
            image = new GreenfootImage("Futuristic sword. 90 right.png");
            image.scale(70, 70);
        }
        else
        {
            image = new GreenfootImage("20.png");
            image.scale(40, 80);
        }

        setImage(image);
    }

    public void act()
    {
    }
}
