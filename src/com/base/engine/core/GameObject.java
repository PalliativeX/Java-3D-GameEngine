package com.base.engine.core;

import com.base.engine.components.GameComponent;
import com.base.engine.core.math.Transform;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObject
{
    private static HashMap<String, ArrayList<GameObject>> allTaggedObjects = new HashMap<>(); // store all tagged to be able to find them by tag

    private ArrayList<GameObject> children;
    private ArrayList<GameComponent> components;
    private Transform transform;
    private CoreEngine engine;

    private boolean isActive; // whether we render this object or not

    private String tag;

    public GameObject()
    {
        children   = new ArrayList<>();
        components = new ArrayList<>();
        transform  = new Transform();
        engine = null;
        isActive = true;

        tag = "Untagged";
    }

    public GameObject(String tag)
    {
        // if object with same tag is already stored, we don't have to initialize ArrayList
        if (!allTaggedObjects.containsKey(tag)) {
            ArrayList<GameObject> objectStorage = new ArrayList<>();
            objectStorage.add(this);
            allTaggedObjects.put(tag, objectStorage);
        }
        else {
            allTaggedObjects.get(tag).add(this);
        }

        children   = new ArrayList<>();
        components = new ArrayList<>();
        transform  = new Transform();
        engine = null;
        isActive = true;
    }

    public GameObject addChild(GameObject child)
    {
        children.add(child);
        child.setEngine(engine);
        child.getTransform().setParent(transform);

        return this;
    }

    public GameObject addComponent(GameComponent component)
    {
        components.add(component);
        component.setParent(this);

        return this;
    }

    public void inputAll(float delta)
    {
        input(delta);

        for (GameObject child : children)
            child.inputAll(delta);
    }

    public void updateAll(float delta)
    {
        update(delta);

        for (GameObject child : children)
            child.updateAll(delta);
    }

    public void renderAll(Shader shader, RenderingEngine renderingEngine)
    {
        if (isActive) {
            render(shader, renderingEngine);

            for (GameObject child : children)
                child.renderAll(shader, renderingEngine);
        }
    }

    public void input(float delta)
    {
        transform.update();

        for (GameComponent component : components)
            component.input(delta);
    }

    public void update(float delta)
    {
        for (GameComponent component : components)
            component.update(delta);
    }

    public void render(Shader shader, RenderingEngine renderingEngine)
    {
        if (isActive) {
            for (GameComponent component : components)
                component.render(shader, renderingEngine);
        }
    }

    public ArrayList<GameObject> getAllAttached()
    {
        ArrayList<GameObject> result = new ArrayList<>();

        for (GameObject child : children) {
            result.addAll(child.getAllAttached());
        }
        result.add(this);

        return result;
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void setEngine(CoreEngine engine)
    {
        if (this.engine != engine) {
            this.engine = engine;

            for (GameComponent component : components)
                component.addToEngine(engine);

            for (GameObject child : children)
                child.setEngine(engine);
        }
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    // sets target bool for both GameObj and all children
    public void setActiveRecursively(boolean active)
    {
        isActive = active;

        for (GameObject child : children)
            child.setActiveRecursively(active);
    }


    // returning first element of ArrayList<GameObject> by tag
    public static GameObject findGameObjectWithTag(String tag)
    {
        return allTaggedObjects.get(tag).get(0);
    }

    // return all GameObjects by tag stored in ArrayList
    public static ArrayList<GameObject> findGameObjectsWithTag(String tag)
    {
        return allTaggedObjects.get(tag);
    }

    public void setTag(String tag)
    {
        if (tag.equals(this.tag))
            return;

        // if this obj had a tag we have to first remove it from hashmap
        if (!tag.equalsIgnoreCase("Untagged"))
            allTaggedObjects.get(tag).remove(this);

        // if object with same tag is already stored, we don't have to initialize ArrayList
        if (!allTaggedObjects.containsKey(tag)) {
            ArrayList<GameObject> objectStorage = new ArrayList<>();
            objectStorage.add(this);
            allTaggedObjects.put(tag, objectStorage);
        } else {
            allTaggedObjects.get(tag).add(this);
        }
    }

    public void removeTag()
    {
        if (allTaggedObjects.containsKey(tag)) {
            allTaggedObjects.remove(tag, this);
            tag = "Untagged";
        }
    }

    public String getTag() {
        return tag;
    }

}
