import greenfoot.*;

public class fireSword extends Sword
{
    public fireSword()
    {
        super();
        swordType = 0;
        loadSwordImage(false);
    }

    @Override
    protected GreenfootImage getSwordImage(boolean rightMode)
    {
        GreenfootImage img;
        if(rightMode)
        {
            img = new GreenfootImage("FireSword.90 right.png");
        }
        else
        {
            img = new GreenfootImage("10.png");
        }
        return img;
    }
}
