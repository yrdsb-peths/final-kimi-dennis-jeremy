import greenfoot.*;

public class KaineCompanionSword extends Actor
{
    public double worldOffX = 22; // 相对玩家的偏移
    public double worldOffY = -6;

    int activeSwordIndex = 0;
    boolean swordRightMode = false;

    GreenfootImage[] swordImages;

    public KaineCompanionSword()
    {
        swordImages = new GreenfootImage[3];
        loadSwordImage(0, false);
    }

    public void act()
    {
        if(getWorld() == null) return;

        handleInput();

        // 跟随玩家屏幕位置
        GameWorld gw = (GameWorld)getWorld();
        int px = gw.screenCX;
        int py = gw.screenCY;
        setLocation(px + (int)worldOffX, py + (int)worldOffY);
    }

    private void handleInput()
    {
        String key = Greenfoot.getKey();
        if(key == null) return;

        if(key.equalsIgnoreCase("space"))
        {
            activeSwordIndex = (activeSwordIndex + 1) % 3;
            swordRightMode = false;
            loadSwordImage(activeSwordIndex, swordRightMode);
        }
        else if(key.equalsIgnoreCase("tab"))
        {
            swordRightMode = !swordRightMode;
            loadSwordImage(activeSwordIndex, swordRightMode);
        }
    }

    private void loadSwordImage(int index, boolean rightMode)
    {
        GreenfootImage img;
        if(index == 0)
        {
            img = rightMode
                ? new GreenfootImage("FireSword.90 right.png")
                : new GreenfootImage("10.png");
        }
        else if(index == 1)
        {
            img = rightMode
                ? new GreenfootImage("Futuristic sword. 90 right.png")
                : new GreenfootImage("20.png");
        }
        else
        {
            img = rightMode
                ? new GreenfootImage("Lightning sword. 90 Right.png")
                : new GreenfootImage("00.png");
        }
        img.scale(rightMode ? 40 : 20, 40);
        if(!rightMode && index == 0) img.rotate(45);
        setImage(img);
    }
}
