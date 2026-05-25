import greenfoot.*;

public class AureaSolvine extends Actor
{
    public int hp = 70;
    public int maxHp = 70;
    public int xp = 0;
    public int coin = 0;
    public int level = 1;
    public int speed = 4;
    public int stamina = 3;
    public int power = 3;
    public int xpToNextLevel = 10;

    public double worldX = 0;
    public double worldY = 0;

    public double moveX = 0;
    public double moveY = 0;

    public void act()
    {
        readInput();
        checkLevelUp();
    }

    public void readInput()
    {
        moveX = 0;
        moveY = 0;
        if(Greenfoot.isKeyDown("w")) moveY = -speed;
        if(Greenfoot.isKeyDown("s")) moveY =  speed;
        if(Greenfoot.isKeyDown("a")) moveX = -speed;
        if(Greenfoot.isKeyDown("d")) moveX =  speed;

        worldX += moveX;
        worldY += moveY;
    }

    public void gainXP(int amount) { xp += amount; }
    public void gainCoin(int amount) { coin += amount; }

    public void checkLevelUp()
    {
        if(xp >= xpToNextLevel)
        {
            xp -= xpToNextLevel;
            level++;
            xpToNextLevel = (int)(xpToNextLevel * 1.5);
            speed  += 1;
            stamina += 1;
            power  += 1;
            maxHp  += 10;
            hp      = maxHp;
        }
    }

    public int getDamage()
    {
        return 10 + (power - 3) * 5;
    }
}