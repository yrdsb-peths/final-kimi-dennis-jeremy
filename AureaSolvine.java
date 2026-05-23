import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class AureaSolvine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AureaSolvine extends Actor
{
    String[] inventory;

    public AureaSolvine()
    {
        setUpInventory();
    }

    public void act()
    {
    }

    private void setUpInventory()
    {
        inventory = new String[0];
    }

    public String getInventoryText()
    {
        return "Inventory: no weapons yet";
    }
}
