import greenfoot.*;

public class LightningSword extends Sword
{
    public LightningSword()
    {
        super();
        swordType = 2;
        loadSwordImage(false);
    }

    @Override
    protected GreenfootImage getSwordImage(boolean rightMode)
    {
        GreenfootImage img;
        if(rightMode)
        {
            img = new GreenfootImage("Lightning sword. 90 Right.png");
        }
        else
        {
            img = new GreenfootImage("00.png");
        }
        return img;
    }
}