import greenfoot.*;

public class KaineVelsarth extends Hero
{
    public int swordDamage = 10;

    static GreenfootImage kaineImage;

    static
    {
        kaineImage = new GreenfootImage("character/Kaine.png");

        if(kaineImage.getWidth() > 0)
        {
            kaineImage.scale(72, 72);
        }
    }

    public KaineVelsarth()
    {
        super();

        if(kaineImage.getWidth() > 0)
        {
            setImage(kaineImage);
        }
    }

    @Override
    protected void updateAnimation()
    {
        animateKaine();
    }

    private void animateKaine()
    {
        GreenfootImage image = new GreenfootImage(kaineImage);

        if(facingLeft)
        {
            image.mirrorHorizontally();
        }

        setImage(image);
    }

    @Override
    protected void onDeathAnimation()
    {
    }

    public String getStartingLoadoutText()
    {
        return "Player starts with all 3 swords equipped.";
    }

    public void takeDamage(int damage)
    {
        takeHit(damage);
    }

    public void gainXp(int amount)
    {
        gainXP(amount);
        checkLevelUp();
    }

    public void displayStats()
    {
        World world = getWorld();

        if(world == null)
        {
            return;
        }

        GreenfootImage background = world.getBackground();
        int hpBarWidth = Math.max(0, Math.min(300, hp * 300 / maxHp));
        int xpBarWidth = Math.max(0, Math.min(300, xp * 300 / xpToNextLevel));

        background.setColor(Color.WHITE);
        background.fillRect(10, 10, 300, 25);
        background.setColor(Color.RED);
        background.fillRect(10, 10, hpBarWidth, 25);

        background.setColor(Color.WHITE);
        background.fillRect(10, 45, 300, 25);
        background.setColor(Color.BLUE);
        background.fillRect(10, 45, xpBarWidth, 25);

        background.setColor(Color.BLACK);
        background.drawRect(10, 10, 300, 25);
        background.drawRect(10, 45, 300, 25);

        world.showText(hp + " / " + maxHp + " HP", 390, 23);
        world.showText("LV " + level + "   " + xp + " / " + xpToNextLevel + " XP", 410, 58);
        world.showText("Coin: " + coin, 80, 85);
    }
}
