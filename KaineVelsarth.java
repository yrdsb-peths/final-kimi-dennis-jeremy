import greenfoot.*;

public class KaineVelsarth extends Actor
{
    private static final int SPEED = 3;
    private static final int SWORD_X_OFFSET = 22;
    private static final int SWORD_Y_OFFSET = -6;

    private String[] inventory;
    private String[] equippedSwords;
    private int activeSwordIndex;
    private boolean spaceWasDown;
    private Actor activeSword;

    public KaineVelsarth()
    {
        setUpInventory();
        activeSwordIndex = -1;
        spaceWasDown = false;
        activeSword = null;

        GreenfootImage image = getImage();

        if (image != null)
        {
            image.scale(72, 72);
            setImage(image);
        }
    }

    public void act()
    {
        moveKaine();
        updateSwordPosition();
        handleSwordCycle();
    }

    protected void addedToWorld(World world)
    {
        world.showText("", 300, 330);
    }

    private void setUpInventory()
    {
        inventory = new String[3];
        inventory[0] = "Fire Sword";
        inventory[1] = "Futuristic Sword";
        inventory[2] = "Lightning Sword";

        equippedSwords = inventory;
    }

    private void moveKaine()
    {
        int x = getX();
        int y = getY();

        if (Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left"))
        {
            x -= SPEED;
        }

        if (Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right"))
        {
            x += SPEED;
        }

        if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up"))
        {
            y -= SPEED;
        }

        if (Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down"))
        {
            y += SPEED;
        }

        World world = getWorld();

        if (world != null)
        {
            x = Math.max(36, Math.min(world.getWidth() - 36, x));
            y = Math.max(36, Math.min(world.getHeight() - 96, y));
        }

        setLocation(x, y);
    }

    private void handleSwordCycle()
    {
        boolean spaceDown = Greenfoot.isKeyDown("space");

        if (spaceDown && !spaceWasDown)
        {
            activateNextSword();
        }

        spaceWasDown = spaceDown;
    }

    private void activateNextSword()
    {
        activeSwordIndex = (activeSwordIndex + 1) % equippedSwords.length;
        spawnActiveSword();
    }

    private void spawnActiveSword()
    {
        World world = getWorld();

        if (world == null)
        {
            return;
        }

        if (activeSword != null && activeSword.getWorld() != null)
        {
            world.removeObject(activeSword);
        }

        if (activeSwordIndex == 0)
        {
            activeSword = new fireSword();
        }
        else if (activeSwordIndex == 1)
        {
            activeSword = new futuristicSword();
        }
        else
        {
            activeSword = new lightningSword();
        }

        world.addObject(activeSword, getX() + SWORD_X_OFFSET, getY() + SWORD_Y_OFFSET);
    }

    private void updateSwordPosition()
    {
        if (activeSword != null && activeSword.getWorld() != null)
        {
            activeSword.setLocation(getX() + SWORD_X_OFFSET, getY() + SWORD_Y_OFFSET);
        }
    }

    public String getInventoryText()
    {
        return "Inventory: " + inventory[0] + ", " + inventory[1] + ", " + inventory[2];
    }

    public String getEquippedText()
    {
        return "Equipped: " + equippedSwords[0] + ", " + equippedSwords[1] + ", " + equippedSwords[2];
    }

    public String getStartingLoadoutText()
    {
        return "Player starts with all 3 swords equipped.";
    }
}
