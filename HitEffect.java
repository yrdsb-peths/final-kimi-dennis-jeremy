import greenfoot.*;

public class HitEffect extends Actor
{
    int timer = 10;

    public HitEffect()
    {
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.ORANGE);
        img.fillOval(0, 0, 20, 20);
        setImage(img);
    }

    public void act()
    {
        timer--;
        getImage().setTransparency(timer * 25);

        if(timer <= 0 && getWorld() != null)
            getWorld().removeObject(this);
    }
}
