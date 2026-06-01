import greenfoot.*;

public class FuturisticSword extends Sword
{
    public FuturisticSword()
    {
        super();
        swordType = 1;
        loadSwordImage(false);
    }

    @Override
    protected GreenfootImage getSwordImage(boolean rightMode)
    {
        GreenfootImage img;
        if(rightMode)
        {
            img = new GreenfootImage("Futuristic sword. 90 right.png");
        }
        else
        {
            img = new GreenfootImage("20.png");
        }
        return img;
    }
}
