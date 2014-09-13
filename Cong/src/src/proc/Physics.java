package src.proc;

import src.interfaces.EntityA;
import src.interfaces.EntityB;

public class Physics {
    
    // Detect collisions by checking for an intersection between differing entities
    public static boolean Collision(EntityA enta, EntityB entb) {
        if (enta.getBounds().intersects(entb.getBounds())) {
            return true;
        }

        return false;
    }

    public static boolean Collision(EntityB entb, EntityA enta) {
        if (entb.getBounds().intersects(enta.getBounds())) {
            return true;
        }

        return false;
    }
}
