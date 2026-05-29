import greenfoot.*;

public class GameOver extends Actor
{
    public GameOver()
    {
        GreenfootImage image = new GreenfootImage(420, 140);

        image.setColor(new Color(0, 0, 0, 180));
        image.fillRect(0, 0, image.getWidth(), image.getHeight());

        image.setColor(Color.RED);
        image.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
        image.drawRect(4, 4, image.getWidth() - 9, image.getHeight() - 9);

        GreenfootImage text = new GreenfootImage("GAME OVER", 64, Color.WHITE, new Color(0, 0, 0, 0));
        image.drawImage(text, (image.getWidth() - text.getWidth()) / 2, 38);

        setImage(image);
    }
}
