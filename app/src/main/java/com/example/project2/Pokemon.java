package com.example.project2;

import java.util.List;

public class Pokemon {
    private String name;
    private int id;
    private int height;
    private int weight;
    private List<AbilitySlot> abilities;
    private Sprites sprites;
    private List<TypeSlot> types;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public List<AbilitySlot> getAbilities() {
        return abilities;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public List<TypeSlot> getTypes() {
        return types;
    }

    // Inner classes for nested objects
    public static class AbilitySlot {
        private Ability ability;
        private boolean is_hidden;
        private int slot;

        public Ability getAbility() {
            return ability;
        }

        public boolean isHidden() {
            return is_hidden;
        }

        public int getSlot() {
            return slot;
        }
    }

    public static class Ability {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class Sprites {
        private String front_default;

        public String getFrontDefault() {
            return front_default;
        }
    }

    public static class TypeSlot {
        private int slot;
        private Type type;

        public int getSlot() {
            return slot;
        }

        public Type getType() {
            return type;
        }
    }

    public static class Type {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
