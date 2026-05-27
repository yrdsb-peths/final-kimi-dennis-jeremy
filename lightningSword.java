import greenfoot.*;

public class lightningSword extends Actor
{
    public lightningSword()
    {
        this(false);
    }

    public lightningSword(boolean lightningRightMode)
    {
        GreenfootImage image;

        if (lightningRightMode)
        {
            image = new GreenfootImage("Lightning sword. 90 Right.png");
            image.scale(70, 70);
        }
        else
        {
            image = new GreenfootImage("00.png");
            image.scale(40, 80);
            image.rotate(45);
        }

        setImage(image);
    }

    public void act()
    {
    }
}
