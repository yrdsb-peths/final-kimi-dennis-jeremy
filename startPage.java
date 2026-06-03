import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class startPage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class startPage extends World
{

    /**
     * Constructor for objects of class startPage.
     * 
     */
    public startPage()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1500, 750, 1); 
        GreenfootImage bg = new GreenfootImage("startpage.png");
        bg.scale(1500,750);
        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", true, false, 22));
        bg.drawString("(Except for Kaine)", 145, 265);
        setBackground(bg);
    }
    
    public void act()
    {
        String key = Greenfoot.getKey();
        if(key != null && (key.equalsIgnoreCase("space") || key.equals(" ")))
            Greenfoot.setWorld(new TitleScreen());
    }
}
