package mygame.allObjects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Card {

    private int typ, cost;
    private float lastTaken, timeLoading;
    private Geometry node;
    private Picture pic;
    private final AssetManager assetManager;
    private String imgPass;

    public Card(int typ, int cost, float lastTaken, float timeLoading, AssetManager assetManager, String imgPass) {
        this.typ = typ;
        this.cost = cost;
        this.lastTaken = lastTaken;
        this.timeLoading = timeLoading;
        this.assetManager = assetManager;
        this.imgPass = imgPass;
        node = createCard(imgPass);
        pic = createCardpic(imgPass);

    }

    public boolean isValid(float now) {
        return (now - lastTaken >= timeLoading);
    }

    public void checkColor(float now, Card Cur) {

        if (Cur != null && Cur.typ == typ) {
            node.getMaterial().setColor("Color", ColorRGBA.Magenta);
        } else if (isValid(now)) {
            node.getMaterial().setColor("Color", ColorRGBA.White);
        } else {
            node.getMaterial().setColor("Color", ColorRGBA.DarkGray);
        }

    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public float getLastTaken() {
        return lastTaken;
    }

    public void setLastTaken(float lastTaken) {
        this.lastTaken = lastTaken;
    }

    public float getTimeLoading() {
        return timeLoading;
    }

    public void setTimeLoading(float timeLoading) {
        this.timeLoading = timeLoading;
    }

    public Geometry getNode() {
        return node;
    }

    public void setNode(Geometry node) {
        this.node = node;
    }

    public String getImgPass() {
        return imgPass;
    }

    public void setImgPass(String imgPass) {
        this.imgPass = imgPass;
    }

    private Geometry createCard(String pass) {
        Box box = new Box(2f, 2f, 0.01f);

        Geometry cube = new Geometry("card", box);

        Material Mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Mat.setColor("Color", ColorRGBA.DarkGray);

        Mat.setTexture("ColorMap", assetManager.loadTexture(pass));
        Mat.getAdditionalRenderState().setDepthWrite(false);
        Mat.getAdditionalRenderState().setDepthTest(false);
        Mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        cube.setQueueBucket(RenderQueue.Bucket.Transparent);
        cube.setMaterial(Mat);
        cube.setLocalTranslation(0, 0, 0);

        cube.updateLogicalState(0.1f);
        cube.updateGeometricState();

        return cube;

    }

    private Picture createCardpic(String pass) {

        Picture pic = new Picture("card");
        pic.setImage(assetManager, pass, true);
        pic.setWidth(40);
        pic.setHeight(40);
        return pic;

    }

    public Picture getPic() {
        return pic;
    }

    public void setPic(Picture pic) {
        this.pic = pic;
    }

    public static ArrayList<Card> loadCards(AssetManager assetManager, HashMap<Geometry, Card> hashingCard) throws FileNotFoundException {
        ArrayList<Card> vector = new ArrayList<>();
        FileInputStream file = new FileInputStream("assets/files/plantsCards.txt");
        Scanner scan = new Scanner(file);
        int ind = 3;
        while (scan.hasNextLine()) {
            int typ = scan.nextInt();
            int cost = scan.nextInt();
            int time = scan.nextInt();

            String pass = scan.next();
            Card card = new Card(typ, cost, -5, time, assetManager, pass);
            hashingCard.put(card.getNode(), card);
            card.getNode().setLocalTranslation(ind * 4 + 8f, 5f, -27);
            card.getPic().setPosition(ind * 40 + 200, 200);

            ind++;
            vector.add(card);

        }
        scan.close();
        return vector;

    }

}
