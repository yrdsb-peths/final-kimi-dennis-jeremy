import greenfoot.*;

public class lightningSword extends Sword
{
    public lightningSword()
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
