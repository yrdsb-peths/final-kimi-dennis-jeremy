import greenfoot.*;

public class MyWorld extends World {
    public MyWorld() {
        super(600, 400, 1);
        
        ppl p = new ppl();
        addObject(p , 300, 200);
    }
}
