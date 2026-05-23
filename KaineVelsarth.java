import greenfoot.*;

public class KaineVelsarth extends Actor
{
    private static final int SPEED = 3;

    private String[] inventory;
    private String[] equippedSwords;
    private int activeSwordIndex;
    private boolean spaceWasDown;

    public KaineVelsarth()
    {
        setUpInventory();
        activeSwordIndex = -1;
        spaceWasDown = false;

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
        handleSwordCycle();
    }

    protected void addedToWorld(World world)
    {
        showSwordStatus("Press SPACE to cycle swords.");
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
        showSwordStatus("Active Sword: " + equippedSwords[activeSwordIndex]);
    }

    private void showSwordStatus(String status)
    {
        World world = getWorld();

        if (world != null)
        {
            world.showText(status, 300, 330);
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
