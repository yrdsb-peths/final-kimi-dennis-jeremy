import greenfoot.*;

public abstract class Sword extends Weapon
{
    protected boolean rightMode = false;
    protected int swordType = 0; // 0=fire, 1=futuristic, 2=lightning

    public Sword()
    {
        super();
        loadSwordImage(false);
    }

    @Override
    public void act()
    {
        // Sword does not move itself; parent controls position
    }

    // Load sword image
    protected void loadSwordImage(boolean rightMode)
    {
        this.rightMode = rightMode;
        GreenfootImage img = getSwordImage(rightMode);
        if(img != null)
        {
            img.scale(rightMode ? 40 : 20, 40);
            if(!rightMode && swordType == 0) img.rotate(45);
            setImage(img);
        }
    }

    protected abstract GreenfootImage getSwordImage(boolean rightMode);

    // Toggle facing direction
    public void toggleDirection()
    {
        loadSwordImage(!rightMode);
    }

    @Override
    protected void onHitEnemy(Enemy e, double dx, double dy, double dist)
    {
        // Sword does not deal damage here; owner decides
    }
}